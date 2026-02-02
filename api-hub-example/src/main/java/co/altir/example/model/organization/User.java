package co.altir.example.model.organization;

import co.altir.dbmanagement.dataaccess.openapidsl.schema.annotations.DslJpaEntity;
import co.altir.dbmanagement.dataaccess.openapidsl.schema.annotations.DslRequired;
import co.altir.example.model.common.Address;
import java.util.UUID;
import lombok.Data;
import lombok.experimental.Accessors;

// Source cell: Organization (organization)::A12
@Accessors(chain = true)
@Data
@DslJpaEntity("users")
public class User {

  private UUID id;

  @DslRequired() private String name;

  @DslRequired() private String email;

  @DslRequired() private String phone;

  @DslRequired() private Address address;
}
