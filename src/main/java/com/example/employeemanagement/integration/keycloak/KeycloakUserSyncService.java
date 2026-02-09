package com.example.employeemanagement.integration.keycloak;

import com.example.employeemanagement.models.dto.UserDto;
import com.example.employeemanagement.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KeycloakUserSyncService {

  private final KeycloakAdminClient keycloakAdminClient;

  /**
   * Syncs user to Keycloak on employee create: creates user (no password), then sets password (non-temporary).
   * Password is never logged or stored. Accept password only at create.
   */
  public void syncUserOnCreate(UserDto user, String password) {
    if (!hasValidEmail(user)) {
      return;
    }
    if (password == null || password.isBlank()) {
      return;
    }

    String userId = keycloakAdminClient.createUserInKeycloak(user.getEmail(), user.getName());
    if (userId != null) {
      keycloakAdminClient.setPassword(userId, password);
    }
  }

  /**
   * Syncs user to Keycloak on employee update (name only; email is the lookup key).
   */
  public void syncUserOnUpdate(UserDto user) {
    if (!hasValidEmail(user)) {
      return;
    }
    keycloakAdminClient.updateUserInKeycloak(user.getEmail(), user.getName());
  }

  /**
   * Removes user from Keycloak on employee delete.
   */
  public void syncUserOnDelete(String email) {
    if (StringUtils.isBlank(email)) {
      return;
    }
    keycloakAdminClient.deleteUserInKeycloak(email);
  }

  private static boolean hasValidEmail(UserDto user) {
    return user != null && !StringUtils.isBlank(user.getEmail());
  }
}
