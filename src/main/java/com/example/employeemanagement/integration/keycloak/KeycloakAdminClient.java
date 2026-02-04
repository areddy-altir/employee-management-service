package com.example.employeemanagement.integration.keycloak;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KeycloakAdminClient {

  @Value("${keycloak.auth-server-url}")
  private String serverUrl;

  @Value("${keycloak.realm}")
  private String realm;

  @Value("${keycloak.client-id}")
  private String clientId;

  @Value("${keycloak.client-secret}")
  private String clientSecret;

  private Keycloak keycloak;
  private UsersResource usersResource;

  @PostConstruct
  public void init() {
    keycloak =
        KeycloakBuilder.builder()
            .serverUrl(serverUrl)
            .realm(realm)
            .clientId(clientId)
            .clientSecret(clientSecret)
            .grantType("client_credentials")
            .build();
    usersResource = keycloak.realm(realm).users();
  }

  /**
   * Creates user in Keycloak with username, first name, last name, email only.
   * Sets a temporary password (forces change on first login).
   */
  public void createUserInKeycloak(String email, String name, String tempPassword) {
    UserRepresentation user = new UserRepresentation();
    user.setEnabled(true);
    user.setUsername(email);
    user.setEmail(email);
    user.setFirstName(nullToEmpty(name));
    user.setLastName("");

    usersResource.create(user);

    if (tempPassword != null && !tempPassword.isBlank()) {
      findUserIdByEmail(email).ifPresent(userId -> {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType("password");
        credential.setValue(tempPassword);
        credential.setTemporary(true);
        usersResource.get(userId).resetPassword(credential);
      });
    }
  }

  /**
   * Updates user in Keycloak: first name, last name only (username/email from lookup).
   */
  public void updateUserInKeycloak(String email, String name) {
    findUserIdByEmail(email).ifPresentOrElse(
        userId -> {
          UserResource userResource = usersResource.get(userId);
          UserRepresentation user = userResource.toRepresentation();
          user.setFirstName(nullToEmpty(name));
          user.setLastName("");
          userResource.update(user);
        },
        () -> {});
  }

  public void deleteUserInKeycloak(String email) {
    findUserIdByEmail(email).ifPresentOrElse(
        userId -> usersResource.get(userId).remove(),
        () -> {});
  }

  private Optional<String> findUserIdByEmail(String email) {
    List<UserRepresentation> users = usersResource.search(email, 0, 1);
    return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0).getId());
  }

  private static String nullToEmpty(String s) {
    return s == null ? "" : s;
  }
}
