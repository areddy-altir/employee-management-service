package co.altir.example.model.organization;

import co.altir.dbmanagement.dataaccess.openapidsl.schema.annotations.DslJpaEntity;
import co.altir.dbmanagement.dataaccess.openapidsl.schema.annotations.DslOneToOne;
import co.altir.dbmanagement.dataaccess.openapidsl.schema.annotations.DslRequired;
import co.altir.example.model.common.BaseAuditFields;
import java.util.UUID;
import lombok.Data;
import lombok.experimental.Accessors;

// Source cell: Organization (organization)::A24
@Accessors(chain = true)
@Data
@DslJpaEntity("employees")
public class Employee extends BaseAuditFields {

  private UUID id;

  @DslRequired() private Organization organization;

  @DslOneToOne(joinColumnName = "user_id")
  @DslRequired()
  private User user;
}
