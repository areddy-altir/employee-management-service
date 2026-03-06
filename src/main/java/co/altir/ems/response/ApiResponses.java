package co.altir.ems.response;

import co.altir.ems.exception.ResponseCode;
import co.altir.ems.models.dto.ErrorFieldDto;
import co.altir.ems.models.dto.ErrorResponseDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

/** Builds standardized error responses using fn-db–generated ErrorResponseDto / ErrorFieldDto. */
public final class ApiResponses {

  private ApiResponses() {}

  /** Converts Map<String, Object> to List<ErrorFieldDto> (supports dotted keys). */
  private static List<ErrorFieldDto> convertMapToErrorFields(Map<String, Object> errors) {
    if (errors == null || errors.isEmpty()) {
      return null;
    }
    List<ErrorFieldDto> list = new ArrayList<>();
    for (Map.Entry<String, Object> entry : errors.entrySet()) {
      String path = entry.getKey();
      Object value = entry.getValue();
      if (value instanceof String) {
        String[] parts = path.split("\\.", 2);
        if (parts.length == 2) {
          list.add(new ErrorFieldDto().objectName(parts[0]).field(parts[1]).message((String) value));
        } else {
          list.add(new ErrorFieldDto().objectName(null).field(path).message((String) value));
        }
      } else if (value instanceof Map) {
        @SuppressWarnings("unchecked")
        Map<String, Object> nested = (Map<String, Object>) value;
        for (Map.Entry<String, Object> ne : nested.entrySet()) {
          String nestedPath = path + "." + ne.getKey();
          String[] parts = nestedPath.split("\\.", 2);
          String msg = ne.getValue() != null ? ne.getValue().toString() : "";
          if (parts.length == 2) {
            list.add(new ErrorFieldDto().objectName(parts[0]).field(parts[1]).message(msg));
          } else {
            list.add(new ErrorFieldDto()
                    .objectName(null).field(nestedPath).message(msg));
          }
        }
      }
    }
    return list.isEmpty() ? null : list;
  }

  /** Single message + error code (no field errors). *///BusinessException

  //Not found bad request
  public static ResponseEntity<ErrorResponseDto> withMsgAndErrorCode(
      HttpStatus status, String message, ResponseCode errorCode) {
    return withMsgErrorCodeAndFields(status, message, errorCode, null);
  }
//Keycloak errors
//Complex validation errors
  /** Message + error code + optional error fields (for Keycloak errors). */
  public static ResponseEntity<ErrorResponseDto> withMsgErrorCodeAndFields(
      HttpStatus status, String message, ResponseCode errorCode, Map<String, Object> errorFieldsMap) {
    ErrorResponseDto dto = new ErrorResponseDto()
        .message(message)
        .errorCode(errorCode != null ? errorCode.getValue() : null)
        .errorFields(convertMapToErrorFields(errorFieldsMap));
    return ResponseEntity.status(status).body(dto);
  }
//Custom HTTP headers
//Custom status
//Field errors
  /** Bad request with field errors (validation). */
  public static ResponseEntity<Object> badRequestWithMsgAndErrorsAsObject(
      String message, Map<String, Object> errors) {
    ErrorResponseDto dto = new ErrorResponseDto()
        .message(message)
        .errorCode(null)
        .errorFields(convertMapToErrorFields(errors));
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((Object) dto);
  }

  /** Bad request with message, error code, and field errors (e.g. validation). */
  public static ResponseEntity<Object> badRequestWithMsgErrorCodeAndErrors(
      String message, ResponseCode errorCode, Map<String, Object> errors) {
    ErrorResponseDto dto = new ErrorResponseDto()
        .message(message)
        .errorCode(errorCode != null ? errorCode.getValue() : null)
        .errorFields(convertMapToErrorFields(errors));
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((Object) dto);
  }
//You want status + message + field errors
  /** For ResponseEntityExceptionHandler: status + message + optional errors + headers. */
  public static ResponseEntity<Object> withMsgErrorsAndHeadersAsObject(
      HttpStatusCode status, String message, Map<String, Object> errors,
      org.springframework.http.HttpHeaders headers) {
    HttpStatus httpStatus = status instanceof HttpStatus
        ? (HttpStatus) status
        : HttpStatus.valueOf(status.value());
    ErrorResponseDto dto = new ErrorResponseDto()
        .message(message)
        .errorCode(null)
        .errorFields(convertMapToErrorFields(errors));
    return ResponseEntity.status(httpStatus).headers(headers).body((Object) dto);
  }
//status,message
  /** Status + message + optional field errors (no headers). */
  public static ResponseEntity<Object> withMsgAndErrorsAsObject(
      HttpStatus status, String message, Map<String, Object> errors) {
    ErrorResponseDto dto = new ErrorResponseDto()
        .message(message)
        .errorCode(null)
        .errorFields(convertMapToErrorFields(errors));
    return ResponseEntity.status(status).body((Object) dto);
  }

  /** Message only (no code, no field errors). */
  public static ResponseEntity<ErrorResponseDto> withMsg(HttpStatus status, String message) {
    ErrorResponseDto dto = new ErrorResponseDto()
        .message(message)
        .errorCode(null)
        .errorFields(null);
    return ResponseEntity.status(status).body(dto);
  }

  /** 500 with message. */
  public static ResponseEntity<ErrorResponseDto> serverErrorWithMsg(String message) {
    ErrorResponseDto dto = new ErrorResponseDto()
        .message(message)
        .errorCode(null)
        .errorFields(null);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(dto);
  }
}
