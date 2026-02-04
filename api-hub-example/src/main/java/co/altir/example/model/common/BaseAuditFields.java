package co.altir.example.model.common;

import co.altir.dbmanagement.dataaccess.openapidsl.schema.annotations.DslCreatedDate;
import co.altir.dbmanagement.dataaccess.openapidsl.schema.annotations.DslLastModifiedDate;
import co.altir.dbmanagement.dataaccess.openapidsl.schema.annotations.DslReadOnly;
import java.time.OffsetDateTime;
import lombok.Data;
import lombok.experimental.Accessors;

// Source cell: Common Fragments (common)::A2
/** Common base class with audit fields */
@Accessors(chain = true)
@Data
public class BaseAuditFields {

  @DslCreatedDate private OffsetDateTime createdOn;

  @DslLastModifiedDate private OffsetDateTime updatedOn;

  @DslReadOnly private String createdBy;

  @DslReadOnly private String updatedBy;

  private Boolean active;
}
