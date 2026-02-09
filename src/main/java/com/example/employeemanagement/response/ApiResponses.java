package com.example.employeemanagement.response;

import com.example.employeemanagement.exception.ResponseCode;
import com.example.employeemanagement.models.dto.ErrorFieldDto;
import com.example.employeemanagement.models.dto.ErrorResponseDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

/** Builds standardized error responses using fn-db–generated ErrorResponseDto / ErrorFieldDto. */
public final class ApiResponses {

  private ApiResponses() {}

  /** Single message + error code (no field errors). */
  public static ResponseEntity<ErrorResponseDto> withMsgAndErrorCode(
      HttpStatus status, String message, ResponseCode errorCode) {
    ErrorResponseDto dto = new ErrorResponseDto()
        .message(message)
        .errorCode(errorCode != null ? errorCode.getValue() : null)
        .errorFields(null);
    return ResponseEntity.status(status).body(dto);
  }

  /** Bad request with field errors (e.g. validation). */
  public static ResponseEntity<Object> badRequestWithMsgAndErrorsAsObject(
      String message, Map<String, Object> errors) {
    ErrorResponseDto dto = new ErrorResponseDto()
        .message(message)
        .errorCode(null)
        .errorFields(convertMapToErrorFields(errors));
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(dto);
  }

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
    return ResponseEntity.status(httpStatus).headers(headers).body(dto);
  }

  /** Status + message + optional field errors (no headers). */
  public static ResponseEntity<Object> withMsgAndErrorsAsObject(
      HttpStatus status, String message, Map<String, Object> errors) {
    ErrorResponseDto dto = new ErrorResponseDto()
        .message(message)
        .errorCode(null)
        .errorFields(convertMapToErrorFields(errors));
    return ResponseEntity.status(status).body(dto);
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
            list.add(new ErrorFieldDto().objectName(null).field(nestedPath).message(msg));
          }
        }
      }
    }
    return list.isEmpty() ? null : list;
  }
}
