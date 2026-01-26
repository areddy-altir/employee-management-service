package co.altir.example;

import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;

/** Common error response object */
@Accessors(chain = true)
@Data
public class ErrorResponse {

  private String errorCode;

  private String message;

  private List<ErrorField> errorFields;
}
