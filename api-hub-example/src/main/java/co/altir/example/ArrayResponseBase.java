package co.altir.example;

import co.altir.dbmanagement.dataaccess.openapidsl.schema.annotations.DslHasMore;
import co.altir.dbmanagement.dataaccess.openapidsl.schema.annotations.DslTotalHits;
import co.altir.dbmanagement.dataaccess.openapidsl.schema.annotations.DslTotalPages;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class ArrayResponseBase {

  @DslTotalHits private Integer totalHits;

  @DslTotalPages private Integer totalPages;

  @DslHasMore private Boolean hasMore;

  private String message;
}
