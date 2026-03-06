package co.altir.ems.config;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Keycloak connection settings and client beans. Initialised at application startup.
 *
 * <p>Two beans are used:
 * <ul>
 *   <li>{@link #keycloak(KeycloakAdminConfig)}: builds a Keycloak Admin client using service-account credentials.</li>
 *   <li>{@link #keycloakUsersResource(Keycloak, KeycloakAdminConfig)}: provides realm Users API access.</li>
 * </ul>
 */
@Configuration
@EnableConfigurationProperties({KeycloakAdminConfig.class, KeycloakUserConfig.class})
public class KeycloakConfig {

  @Bean
  /**
   * What it does: Creates a Keycloak Admin client connection using service-account (client_credentials).
   *
   * <p>What it accepts: server URL, realm, clientId, clientSecret (from {@link KeycloakAdminConfig}).
   * Keycloak validates the client + secret and issues an access token.
   *
   * <p>What it returns: a {@link Keycloak} admin client used to manage realm resources
   * (create/update/delete users, manage roles, reset passwords, etc).
   */
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
  /**
   * What it does: Gives access to the Users section inside the configured realm (Users Admin API).
   *
   * <p>What it accepts: the already-created {@link Keycloak} admin connection + realm from {@link KeycloakAdminConfig}.
   *
   * <p>What it returns: a {@link UsersResource} that allows create/update/delete/search users in that realm.
   */
  public UsersResource keycloakUsersResource(Keycloak keycloak, KeycloakAdminConfig admin) {
    return keycloak.realm(admin.realm()).users();
  }
}
