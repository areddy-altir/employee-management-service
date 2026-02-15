package com.example.employeemanagement.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * User-side Keycloak settings for OIDC (issuer-uri, token endpoints, etc).
 *
 * <p>This is not used for admin API calls; see {@link KeycloakAdminConfig}.
 */
@Validated
@ConfigurationProperties(prefix = "keycloak.user")
public record KeycloakUserConfig(@NotBlank String serverUrl, @NotBlank String realm) {}

