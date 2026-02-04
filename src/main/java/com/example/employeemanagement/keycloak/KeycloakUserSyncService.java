package com.example.employeemanagement.keycloak;

import com.example.employeemanagement.models.dto.UserDto;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KeycloakUserSyncService {

  private final KeycloakUserHelper keycloakUserHelper;

  /**
   * Syncs user to Keycloak on employee create: creates user with temporary password and logs it
   * for the admin to share.
   */
  public void syncUserOnCreate(UserDto user) {
    if (user == null || user.getEmail() == null || user.getEmail().isBlank()) {
      return;
    }
    String tempPassword = generateTemporaryPassword();
    keycloakUserHelper.createUserInKeycloak(
        user.getEmail(),
        user.getName(),
        tempPassword);
    log.info(
        "Temporary password for {} (share with user, they must change on first login): {}",
        user.getEmail(),
        tempPassword);
  }

  /**
   * Syncs user to Keycloak on employee update (name only; email is the lookup key).
   */
  public void syncUserOnUpdate(UserDto user) {
    if (user == null || user.getEmail() == null || user.getEmail().isBlank()) {
      return;
    }
    keycloakUserHelper.updateUserInKeycloak(user.getEmail(), user.getName());
  }

  /**
   * Removes user from Keycloak on employee delete.
   */
  public void syncUserOnDelete(String email) {
    if (email == null || email.isBlank()) {
      return;
    }
    keycloakUserHelper.deleteUserInKeycloak(email);
  }

  private static String generateTemporaryPassword() {
    return "Temp" + UUID.randomUUID().toString().replace("-", "").substring(0, 8) + "!";
  }
}
