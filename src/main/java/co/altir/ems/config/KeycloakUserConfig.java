package co.altir.ems.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * What it does: Reads user-side Keycloak configuration used for authentication/JWT validation.
 *
 * <p>What it accepts: configuration values under `keycloak.user.*` in `application.yml`
 * (e.g. server-url, realm).
 *
 * <p>What it returns: an immutable {@code KeycloakUserConfig} containing serverUrl + realm.
 *
 * <p>Used for: issuer-uri / realm information for JWT validation.
 *
 * <p>This is not used for admin API calls; see {@link KeycloakAdminConfig}.
 */
@Validated
@ConfigurationProperties(prefix = "keycloak.user")
public record KeycloakUserConfig(@NotBlank String serverUrl, @NotBlank String realm) {}

