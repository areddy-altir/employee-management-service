package com.example.employeemanagement.config;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Keycloak connection settings and client beans. Initialised at application startup.
 */
@Configuration
@EnableConfigurationProperties({KeycloakAdminConfig.class, KeycloakUserConfig.class})
public class KeycloakConfig {

  @Bean
  public Keycloak keycloak(KeycloakAdminConfig admin) {
    return KeycloakBuilder.builder()
        .serverUrl(admin.serverUrl())
        .realm(admin.realm())
        .clientId(admin.clientId())
        .clientSecret(admin.clientSecret())
        .grantType("client_credentials")
        .build();
  }

  @Bean
  public UsersResource keycloakUsersResource(Keycloak keycloak, KeycloakAdminConfig admin) {
    return keycloak.realm(admin.realm()).users();
  }
}
