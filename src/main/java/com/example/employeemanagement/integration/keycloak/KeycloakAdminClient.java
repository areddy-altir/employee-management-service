package com.example.employeemanagement.integration.keycloak;

import java.util.List;
import java.util.Optional;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
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

  /**
   * Creates user in Keycloak with username, first name, last name, email only. No password.
   * Set password via Keycloak Admin API (PUT .../users/{userId}/reset-password) with temporary: false.
   */
  public void createUserInKeycloak(String email, String name) {
    UserRepresentation user = new UserRepresentation();
    user.setEnabled(true);
    user.setUsername(email);
    user.setEmail(email);
<<<<<<< Updated upstream
    user.setFirstName(nullToEmpty(name));
    user.setLastName("NA");
=======
    NameParts parts = splitName(name);
    user.setFirstName(parts.firstName());
    user.setLastName(parts.lastName());
>>>>>>> Stashed changes

    usersResource.create(user);
  }

  /**
   * Updates user in Keycloak: first name, last name only (username/email from lookup).
   */
  public void updateUserInKeycloak(String email, String name) {
    findUserIdByEmail(email).ifPresentOrElse(
        userId -> {
          UserResource userResource = usersResource.get(userId);
          UserRepresentation user = userResource.toRepresentation();
<<<<<<< Updated upstream
          user.setFirstName(nullToEmpty(name));
          user.setLastName("NA");
=======
          NameParts parts = splitName(name);
          user.setFirstName(parts.firstName());
          user.setLastName(parts.lastName());
>>>>>>> Stashed changes
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

  /**
   * Splits a full name into first and last name.
   *
   * <p>If the name has multiple words, the last word becomes lastName and the preceding words become
   * firstName. If it has a single word, it becomes firstName and lastName is empty.
   */
  private static NameParts splitName(String fullName) {
    String normalized = nullToEmpty(fullName).trim().replaceAll("\\s+", " ");
    if (normalized.isBlank()) {
      return new NameParts("", "");
    }
    int lastSpace = normalized.lastIndexOf(' ');
    if (lastSpace < 0) {
      return new NameParts(normalized, "");
    }
    String first = normalized.substring(0, lastSpace).trim();
    String last = normalized.substring(lastSpace + 1).trim();
    return new NameParts(first, last);
  }

  private record NameParts(String firstName, String lastName) {}
}
