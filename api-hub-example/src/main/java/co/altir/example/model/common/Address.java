package co.altir.example.model.common;

import co.altir.dbmanagement.dataaccess.openapidsl.schema.annotations.DslJpaEmbeddable;
import co.altir.dbmanagement.dataaccess.openapidsl.schema.annotations.DslRequired;
import lombok.Data;
import lombok.experimental.Accessors;

// Source cell: Common Fragments (common)::A10
/** Reusable address value object */
@Accessors(chain = true)
@Data
@DslJpaEmbeddable
public class Address {

  @DslRequired() private String line1;

  private String line2;

  @DslRequired() private String city;

  @DslRequired() private String state;

  @DslRequired() private String country;

  @DslRequired() private String pincode;
}
