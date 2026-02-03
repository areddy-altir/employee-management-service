package com.example.employeemanagement.config;

import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KeycloakUserHelper {

  @Value("${keycloak.auth-server-url}")
  private String serverUrl;

  @Value("${keycloak.realm}")
  private String realm;

  @Value("${keycloak.client-id}")
  private String clientId;

  @Value("${keycloak.client-secret}")
  private String clientSecret;

  private Keycloak keycloak;

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
  }

  public void createUserInKeycloak(String email, String name, String phone) {
    createUserInKeycloak(email, name, phone, null, null);
  }

  /**
   * Creates user in Keycloak and sets a temporary password (forces change on first login).
   * Address is a single string (e.g. full formatted address including country).
   */
  public void createUserInKeycloak(
      String email,
      String name,
      String phone,
      String address,
      String tempPassword) {
    UserRepresentation user = new UserRepresentation();
    user.setEnabled(true);
    user.setUsername(email);
    user.setEmail(email);
    user.setFirstName(name != null ? name : "");
    user.setLastName("");
    user.singleAttribute("phone", phone != null ? phone : "");
    user.singleAttribute("address", address != null ? address : "");

    keycloak.realm(realm).users().create(user);

    if (tempPassword != null && !tempPassword.isBlank()) {
      List<UserRepresentation> users = keycloak.realm(realm).users().search(email, 0, 1);
      if (!users.isEmpty()) {
        String userId = users.get(0).getId();
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType("password");
        credential.setValue(tempPassword);
        credential.setTemporary(true);
        keycloak.realm(realm).users().get(userId).resetPassword(credential);
      }
    }
    log.info("Created user in Keycloak: {}", email);
  }

  public void updateUserInKeycloak(String email, String name, String phone) {
    updateUserInKeycloak(email, name, phone, null);
  }

  public void updateUserInKeycloak(String email, String name, String phone, String address) {
    List<UserRepresentation> users =
        keycloak.realm(realm).users().search(email, 0, 1);
    if (users.isEmpty()) {
      log.warn("User not found in Keycloak for update: {}", email);
      return;
    }
    String keycloakUserId = users.get(0).getId();
    UserRepresentation user =
        keycloak.realm(realm).users().get(keycloakUserId).toRepresentation();
    user.setFirstName(name != null ? name : "");
    user.singleAttribute("phone", phone != null ? phone : "");
    user.singleAttribute("address", address != null ? address : "");
    keycloak.realm(realm).users().get(keycloakUserId).update(user);
    log.info("Updated user in Keycloak: {}", email);
  }

  public void deleteUserInKeycloak(String email) {
    List<UserRepresentation> users =
        keycloak.realm(realm).users().search(email, 0, 1);
    if (users.isEmpty()) {
      log.warn("User not found in Keycloak for delete: {}", email);
      return;
    }
    String keycloakUserId = users.get(0).getId();
    keycloak.realm(realm).users().get(keycloakUserId).remove();
    log.info("Deleted user from Keycloak: {}", email);
  }
}
