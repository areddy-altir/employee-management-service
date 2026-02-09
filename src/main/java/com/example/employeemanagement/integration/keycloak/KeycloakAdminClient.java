package com.example.employeemanagement.integration.keycloak;

import java.util.List;
import java.util.Optional;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Component;

/**
 * Keycloak Admin API client: create, update, delete users. Uses the UsersResource bean from KeycloakConfig.
 */
@Component
public class KeycloakAdminClient {

  private final UsersResource usersResource;

  public KeycloakAdminClient(UsersResource keycloakUsersResource) {
    this.usersResource = keycloakUsersResource;
  }

  private static String nullToEmpty(String s) {
    return s == null ? "" : s;
  }

  private Optional<String> findUserIdByEmail(String email) {
    List<UserRepresentation> users = usersResource.search(email, 0, 1);
    return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0).getId());
  }

  /**
   * Creates user in Keycloak with username, first name, last name, email only. No password.
   * Caller must call {@link #setPassword(String, String)} to make the user loginable.
   *
   * @return Keycloak user id, or null if create failed
   */
  public String createUserInKeycloak(String email, String name) {
    UserRepresentation user = new UserRepresentation();
    user.setEnabled(true);
    user.setUsername(email);
    user.setEmail(email);
    user.setFirstName(nullToEmpty(name));
    user.setLastName("NA");
    user.setEmailVerified(true);
    user.setRequiredActions(List.of());

    usersResource.create(user);
    return findUserIdByEmail(email).orElse(null);
  }

  /**
   * Sets password for a Keycloak user (non-temporary). User can login immediately.
   * Never log or store the password.
   */
  public void setPassword(String userId, String password) {
    if (userId == null || password == null || password.isBlank()) {
      return;
    }
    CredentialRepresentation credential = new CredentialRepresentation();
    credential.setType("password");
    credential.setValue(password);
    credential.setTemporary(false);
    usersResource.get(userId).resetPassword(credential);
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
          user.setLastName("NA");
          userResource.update(user);
        },
        () -> {});
  }

  public void deleteUserInKeycloak(String email) {
    findUserIdByEmail(email).ifPresentOrElse(
        userId -> usersResource.get(userId).remove(),
        () -> {});
  }
}
