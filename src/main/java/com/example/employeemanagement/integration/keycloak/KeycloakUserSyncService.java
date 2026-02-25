package com.example.employeemanagement.integration.keycloak;

import com.example.employeemanagement.models.dto.UserDto;
import com.example.employeemanagement.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * What it does: Keeps application users (Employee.user) in sync with Keycloak users.
 *
 * <p>When an employee is created/updated/deleted, this service creates/updates/deletes the user in Keycloak.
 * It does not call Keycloak directly; it delegates to {@link KeycloakAdminClient}.
 *
 * <p>What it accepts:
 * <ul>
 *   <li>{@link UserDto} on create/update</li>
 *   <li>email (String) on delete</li>
 * </ul>
 *
 * <p>What it returns: void (side-effect sync).
 */
@Service
@RequiredArgsConstructor
public class KeycloakUserSyncService {

  private final KeycloakAdminClient keycloakAdminClient;

  private static boolean hasValidEmail(UserDto user) {
    return user != null && !StringUtils.isBlank(user.getEmail());
  }

  /**
   * Syncs user to Keycloak on employee create: creates user only (not password).
   * Set password via Keycloak Admin API (Step 2: get user id, Step 3: PUT reset-password with temporary: false).
   */
  public void syncUserOnCreate(UserDto user) {
    if (!hasValidEmail(user)) {
      return;
    }
    keycloakAdminClient.createUserInKeycloak(
        user.getEmail(),
        user.getFirstname(),
        user.getLastname());
  }

  /**
   * Syncs user to Keycloak on employee update (name only; email is the lookup key).
   */
  public void syncUserOnUpdate(UserDto user) {
    if (!hasValidEmail(user)) {
      return;
    }
    keycloakAdminClient.updateUserInKeycloak(
        user.getEmail(),
        user.getFirstname(),
        user.getLastname());
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
}
