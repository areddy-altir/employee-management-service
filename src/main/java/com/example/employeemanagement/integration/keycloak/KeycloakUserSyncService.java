package com.example.employeemanagement.integration.keycloak;

import com.example.employeemanagement.models.dto.UserDto;
import com.example.employeemanagement.util.StringUtils;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KeycloakUserSyncService {

  private final KeycloakAdminClient keycloakAdminClient;

  /**
   * Syncs user to Keycloak on employee create: creates user with temporary password
   * (forces change on first login).
   */
  public void syncUserOnCreate(UserDto user) {
    if (!hasValidEmail(user)) {
      return;
    }
    String tempPassword = generateTemporaryPassword();
    keycloakAdminClient.createUserInKeycloak(
        user.getEmail(),
        user.getName(),
        tempPassword);
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

  private static String generateTemporaryPassword() {
    return "Temp" + UUID.randomUUID().toString().replace("-", "").substring(0, 8) + "!";
  }
}
