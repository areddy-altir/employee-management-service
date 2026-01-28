package co.altir.example.model.organization;

import co.altir.dbmanagement.dataaccess.openapidsl.schema.annotations.DslJpaEntity;
import co.altir.dbmanagement.dataaccess.openapidsl.schema.annotations.DslRequired;
import co.altir.example.model.common.BaseAuditFields;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.experimental.Accessors;

// Source cell: Organization (organization)::A2
@Accessors(chain = true)
@Data
@DslJpaEntity("organizations")
public class Organization extends BaseAuditFields {

  private UUID id;

  @DslRequired() private String name;

  @DslRequired() private String address;

  private String country;

  @DslRequired() private List<Employee> employees;
}
