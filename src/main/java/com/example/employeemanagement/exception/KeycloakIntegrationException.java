package com.example.employeemanagement.exception;

/**
 * Does: Represents any failure while calling Keycloak (Admin API / OIDC).
 * Accepts: status (int), action (String), keycloakBody (String), cause (Throwable).
 * Returns: Not applicable (exception). Exposes status/action/keycloakBody via getters.
 *
 * <p>Intended to be handled by a dedicated exception handler so Keycloak failures can be formatted
 * consistently and separately from core business exceptions.
 */
public final class KeycloakIntegrationException extends RuntimeException {

  private final int status;
  private final String action;
  private final String keycloakBody;

  public KeycloakIntegrationException(int status, String action, String keycloakBody, Throwable cause) {
    super("Keycloak failed: " + action + " (status=" + status + ")", cause);
    this.status = status;
    this.action = action;
    this.keycloakBody = keycloakBody;
  }

  public int getStatus() {
    return status;
  }

  public String getAction() {
    return action;
  }

  public String getKeycloakBody() {
    return keycloakBody;
  }
}

