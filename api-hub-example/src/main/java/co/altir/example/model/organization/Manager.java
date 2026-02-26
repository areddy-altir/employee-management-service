package co.altir.example.model.organization;

import co.altir.dbmanagement.dataaccess.openapidsl.schema.annotations.DslJpaEntity;
import co.altir.dbmanagement.dataaccess.openapidsl.schema.annotations.DslManyToOne;
import co.altir.dbmanagement.dataaccess.openapidsl.schema.annotations.DslOneToOne;
import co.altir.dbmanagement.dataaccess.openapidsl.schema.annotations.DslRequired;
import co.altir.example.model.common.BaseAuditFields;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.experimental.Accessors;

// Source cell: Organization (organization)::A34
@Accessors(chain = true)
@Data
@DslJpaEntity("managers")
public class Manager extends BaseAuditFields {

  private UUID id;

  @DslManyToOne @DslRequired() private Organization organization;
  @DslOneToOne @DslRequired() private User user;

  private List<Employee> employees;
}
