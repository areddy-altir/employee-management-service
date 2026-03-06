package co.altir.ems.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * What it does: Reads Keycloak Admin configuration values from `application.yml` and stores them in a structured object.
 *
 * <p>What it accepts: configuration values under `keycloak.admin.*`.
 *
 * <p>What it returns: an immutable {@code KeycloakAdminConfig} containing:
 * serverUrl, realm, clientId, clientSecret. This is injected into other beans (e.g. {@link KeycloakConfig}).
 */
@Validated //config values are valid
@ConfigurationProperties(prefix = "keycloak.admin")//read properties from application.yml and map them to the class
public record KeycloakAdminConfig(
    @NotBlank String serverUrl,
    @NotBlank String realm,
    @NotBlank String clientId,
    @NotBlank String clientSecret) {}

