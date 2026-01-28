package co.altir.example.model.organization;

import co.altir.dbmanagement.dataaccess.openapidsl.schema.annotations.DslJpaEntity;
import co.altir.dbmanagement.dataaccess.openapidsl.schema.annotations.DslRequired;
import co.altir.example.model.common.BaseAuditFields;
import java.util.UUID;
import lombok.Data;
import lombok.experimental.Accessors;

// Source cell: Organization (organization)::A12
@Accessors(chain = true)
@Data
@DslJpaEntity("users")
public class User extends BaseAuditFields {

  private UUID id;

  @DslRequired() private String name;

  @DslRequired() private String email;

  @DslRequired() private String phone;

  private String address;

  private String country;

  /**
   * Keycloak User ID - Links this user to a Keycloak identity This is the "sub" claim from the JWT
   * token
   */
  private String keycloakUserId;
}
