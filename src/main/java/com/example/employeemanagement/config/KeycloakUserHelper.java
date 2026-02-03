package com.example.employeemanagement.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KeycloakUserHelper {

  private final String serverUrl;
  private final String realm;
  private final String adminRealm;
  private final String adminUsername;
  private final String adminPassword;
  private final String clientId;

  private volatile Keycloak keycloak;

  public KeycloakUserHelper(
      @Value("${keycloak.server-url}") String serverUrl,
      @Value("${keycloak.realm}") String realm,
      @Value("${keycloak.admin.realm:master}") String adminRealm,
      @Value("${keycloak.admin.username}") String adminUsername,
      @Value("${keycloak.admin.password}") String adminPassword,
      @Value("${keycloak.client-id:admin-cli}") String clientId) {
    this.serverUrl = serverUrl;
    this.realm = realm;
    this.adminRealm = adminRealm;
    this.adminUsername = adminUsername;
    this.adminPassword = adminPassword;
    this.clientId = clientId;
  }

  private Keycloak getKeycloak() {
    if (keycloak == null) {
      synchronized (this) {
        if (keycloak == null) {
          keycloak =
              KeycloakBuilder.builder()
                  .serverUrl(serverUrl)
                  .realm(adminRealm)
                  .username(adminUsername)
                  .password(adminPassword)
                  .clientId(clientId)
                  .build();
        }
      }
    }
    return keycloak;
  }

  private UsersResource usersResource() {
    return getKeycloak().realm(realm).users();
  }

  private static Map<String, List<String>> attributes(String phone, String address, String country) {
    Map<String, List<String>> attrs = new HashMap<>();
    attrs.put("phone", Collections.singletonList(phone != null ? phone : ""));
    attrs.put("address", Collections.singletonList(address != null ? address : ""));
    attrs.put("country", Collections.singletonList(country != null ? country : ""));
    return attrs;
  }

  /** Create a user in Keycloak with email, name, phone, address, country and temporary password. */
  public void createUserInKeycloak(
      String email,
      String name,
      String phone,
      String addressStr,
      String country,
      String tempPassword) {
    UserRepresentation user = new UserRepresentation();
    user.setUsername(email);
    user.setEmail(email);
    user.setFirstName(name != null ? name : "");
    user.setLastName("");
    user.setEnabled(true);
    user.setAttributes(attributes(phone, addressStr, country));

    CredentialRepresentation cred = new CredentialRepresentation();
    cred.setType(CredentialRepresentation.PASSWORD);
    cred.setValue(tempPassword);
    cred.setTemporary(true);
    user.setCredentials(Collections.singletonList(cred));

    usersResource().create(user);
  }

  /** Update an existing Keycloak user by email. */
  public void updateUserInKeycloak(
      String email, String name, String phone, String addressStr, String country) {
    List<UserRepresentation> found = usersResource().search(email, true);
    if (found.isEmpty()) {
      return;
    }
    UserResource userResource = usersResource().get(found.get(0).getId());
    UserRepresentation user = userResource.toRepresentation();
    user.setFirstName(name != null ? name : "");
    user.setAttributes(attributes(phone, addressStr, country));
    userResource.update(user);
  }

  /** Delete a Keycloak user by email. */
  public void deleteUserInKeycloak(String email) {
    List<UserRepresentation> found = usersResource().search(email, true);
    if (!found.isEmpty()) {
      usersResource().get(found.get(0).getId()).remove();
    }
  }
}
