package co.altir.example.model.common;

import java.time.OffsetDateTime;
import lombok.Data;
import lombok.experimental.Accessors;

// Source cell: Common Fragments (common)::A2
/** Common base class with audit fields */
@Accessors(chain = true)
@Data
public class BaseAuditFields {

  private OffsetDateTime createdOn;

  private OffsetDateTime updatedOn;

  private String createdBy;

  private String updatedBy;

  private Boolean active;
}
