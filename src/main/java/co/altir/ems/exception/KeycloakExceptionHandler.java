package co.altir.ems.exception;

import co.altir.ems.response.ApiResponses;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import co.altir.ems.models.dto.ErrorResponseDto;

/**
 * Does: Handles KeycloakIntegrationException separately and converts it to API ErrorResponseDto.
 * Accepts: KeycloakIntegrationException (status/action/body/cause).
 * Returns: ResponseEntity&lt;ErrorResponseDto&gt; with mapped HttpStatus and existing ErrorCode.
 *
 * <p>We keep Keycloak failures separate from core business exceptions so they can be identified and
 * diagnosed quickly (status, action, Keycloak response body).
 */
@Slf4j
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class KeycloakExceptionHandler {

  // This is the "response mapping" layer for Keycloak failures:
  // KeycloakAdminClient throws KeycloakIntegrationException -> this handler converts it to API ErrorResponseDto.
  @ExceptionHandler(KeycloakIntegrationException.class)
  /**
   * Does: Maps Keycloak status codes (400/401/403/404/409/others) to API status + ErrorCode.
   * Accepts: KeycloakIntegrationException.
   * Returns: ResponseEntity&lt;ErrorResponseDto&gt;.
   */
  public ResponseEntity<ErrorResponseDto> handleKeycloak(KeycloakIntegrationException ex) {
    int status = ex.getStatus();
    HttpStatus httpStatus = switch (status) {
      case 400 -> HttpStatus.BAD_REQUEST;
      case 401 -> HttpStatus.UNAUTHORIZED;
      case 403 -> HttpStatus.FORBIDDEN;
      case 404 -> HttpStatus.NOT_FOUND;
      case 409 -> HttpStatus.UNPROCESSABLE_ENTITY;
      default -> HttpStatus.INTERNAL_SERVER_ERROR;
    };

    ErrorCode code = switch (httpStatus) {
      case BAD_REQUEST -> ErrorCode.EMP_400;
      case UNAUTHORIZED -> ErrorCode.UNAUTHORIZED;
      case FORBIDDEN -> ErrorCode.FORBIDDEN;
      case NOT_FOUND -> ErrorCode.EMP_404;
      case UNPROCESSABLE_ENTITY -> ErrorCode.EMP_422;
      default -> ErrorCode.BUSINESS_ERROR;
    };

    String msg = ex.getMessage();
    String body = ex.getKeycloakBody();
    if (body != null && !body.isBlank()) {
      msg = msg + " | keycloak=" + body;
    }

    Map<String, Object> errorFields = new HashMap<>();
    errorFields.put("keycloak.action", ex.getAction());
    if (body != null && !body.isBlank()) {
      errorFields.put("keycloak.body", body);
    }
    errorFields.put("keycloak.status", String.valueOf(status));

    log.error("KeycloakIntegrationException. action={}, status={}, msg={}", ex.getAction(), status, msg);
    return ApiResponses.withMsgErrorCodeAndFields(httpStatus, msg, code, errorFields);
  }
}

