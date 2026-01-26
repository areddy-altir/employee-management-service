package co.altir.example.model.organization;

import co.altir.dbmanagement.dataaccess.openapidsl.schema.annotations.DslJpaEntity;
import co.altir.dbmanagement.dataaccess.openapidsl.schema.annotations.DslOneToOne;
import java.util.UUID;
import lombok.Data;
import lombok.experimental.Accessors;

// Source cell: Organization (organization)::A24
@Accessors(chain = true)
@Data
@DslJpaEntity("employees")
public class Employee {

  private UUID id;

  private Organization organization;

  @DslOneToOne(joinColumnName = "user_id")
  private User user;
}
