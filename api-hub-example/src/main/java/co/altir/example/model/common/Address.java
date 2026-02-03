package co.altir.example.model.common;

import co.altir.dbmanagement.dataaccess.openapidsl.schema.annotations.DslRequired;
import lombok.Data;
import lombok.experimental.Accessors;

// Source cell: Common Fragments (common)::A10
@Accessors(chain = true)
@Data
public class Address {

  @DslRequired() private String line1;

  private String line2;

  @DslRequired() private String city;

  @DslRequired() private String state;

  @DslRequired() private String zip;

  @DslRequired() private String country;
}
