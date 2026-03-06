package co.altir.ems.integration.keycloak;

import co.altir.ems.exception.KeycloakIntegrationException;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Component;

/**
 * Keycloak Admin API client: create, update, delete users. Uses the UsersResource bean from KeycloakConfig.
 *
 * <p>What it does:
 * <ul>
 *   <li>Create a user in Keycloak (without password)</li>
 *   <li>Update first name / last name</li>
 *   <li>Delete a user</li>
 *   <li>Find user by email</li>
 *   <li>Normalize Keycloak errors into {@link KeycloakIntegrationException}</li>
 * </ul>
 *
 * <p>What it accepts:
 * <ul>
 *   <li>Constructor: {@link UsersResource} (Keycloak Users API for a specific realm)</li>
 *   <li>Methods: email (String), firstName (String), lastName (String) depending on operation</li>
 * </ul>
 *
 * <p>What it returns:
 * <ul>
 *   <li>Public methods return void</li>
 *   <li>Internally may return Optional userId and generic {@code <T>} from {@code callKeycloak}</li>
 * </ul>
 *
 * <p>Note: We do not use a separate KeycloakExceptionMapper class in this project.
 * This class "normalizes" Keycloak exceptions into {@link KeycloakIntegrationException}.
 * The final API response mapping (HttpStatus + ErrorCode + ErrorResponseDto) is done in
 * {@link co.altir.ems.exception.KeycloakExceptionHandler}.
 */
@Component
public class KeycloakAdminClient {

  private final UsersResource usersResource;

//    Using Admin Client, backend can:
//    Create users
//    Delete users
//    Update users
//    Reset passwords
//    Assign roles
//    Search users
//    Manage realms
//     manage Keycloak from your backend.

  public KeycloakAdminClient(UsersResource keycloakUsersResource) {
    this.usersResource = keycloakUsersResource;
  }

  private static String nullToEmpty(String s) {
    return s == null ? "" : s;
  }

  private static String safeReadBody(Response resp) {
    if (resp == null) {
      return "";
    }
    try {
      return resp.readEntity(String.class);
    } catch (Exception ignored) {
      return "";
    }
  }

  private <T> T callKeycloak(String action, Supplier<T> fn) {
    try {
      return fn.get();
    } catch (WebApplicationException ex) {
      int status = ex.getResponse() != null ? ex.getResponse().getStatus() : 500;
      String body = safeReadBody(ex.getResponse());
      // Wrap Keycloak HTTP errors so they can be handled in one place (@ControllerAdvice).
      String msg =
          "Keycloak error during " + action + " (status=" + status + ")" + (body.isBlank() ? "" : (", " + body));
      throw new KeycloakIntegrationException(status, action, body, ex);
    } catch (ProcessingException ex) {
      // Wrap network/IO errors (timeouts, connection refused, DNS, etc).
      throw new KeycloakIntegrationException(503, action, "", ex);
    }
  }

  private Optional<String> findUserIdByEmail(String email) {
    List<UserRepresentation> users = callKeycloak(
        "search user by email " + email,
        () -> usersResource.search(email, 0, 1));
    return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0).getId());
  }

  /**
   * Creates user in Keycloak with username, first name, last name, email only. No password.
   * Set password via Keycloak Admin API (PUT .../users/{userId}/reset-password) with temporary: false.
   *
   * <p>Accepts: email, firstName, lastName.
   * Returns: void.
   */
  public void createUserInKeycloak(String email, String firstName, String lastName) {
    String kcFirst = nullToEmpty(firstName).trim();
    String kcLast = nullToEmpty(lastName).trim();
    callKeycloak("create user " + email, () -> {
      UserRepresentation user = new UserRepresentation();
      user.setEnabled(true);
      user.setUsername(email);
      user.setEmail(email);
      user.setFirstName(kcFirst);
      user.setLastName(kcLast);
      usersResource.create(user);
      return null;
    });
  }

  /**
   * Updates user in Keycloak: first name, last name only (username/email from lookup).
   *
   * <p>Accepts: email, firstName, lastName.
   * Returns: void.
   */
  public void updateUserInKeycloak(String email, String firstName, String lastName) {
    findUserIdByEmail(email).ifPresentOrElse(
        userId -> {
          UserResource userResource = usersResource.get(userId);
          UserRepresentation user = userResource.toRepresentation();
          String kcFirst = nullToEmpty(firstName).trim();
          String kcLast = nullToEmpty(lastName).trim();
          user.setFirstName(kcFirst);
          user.setLastName(kcLast);
          callKeycloak("update user " + userId, () -> {
            userResource.update(user);
            return null;
          });
        },
        () -> {});
  }

  /**
   * Removes user from Keycloak (lookup by email).
   *
   * <p>Accepts: email.
   * Returns: void.
   */
  public void deleteUserInKeycloak(String email) {
    findUserIdByEmail(email).ifPresentOrElse(
        userId -> callKeycloak("delete user " + userId, () -> {
          usersResource.get(userId).remove();
          return null;
        }),
        () -> {});
  }


}
