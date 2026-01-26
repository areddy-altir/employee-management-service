package co.altir.example;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class ErrorField {

  private String objectName;

  private String field;

  private String message;
}
