package co.altir.example.model.organization;

import co.altir.dbmanagement.dataaccess.openapidsl.schema.annotations.DslJpaEntity;
import java.util.UUID;
import lombok.Data;
import lombok.experimental.Accessors;

// Source cell: Organization (organization)::A12
@Accessors(chain = true)
@Data
@DslJpaEntity("users")
public class User {

  private UUID id;

  private String name;

  private String email;

  private String phone;

  private String address;

  private String country;
}
