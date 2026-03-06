package com.example.employeemanagement.exception;

/** Error codes for employee-management-service API errors. */
public enum ErrorCode implements ResponseCode {

  UNAUTHORIZED("AUTH_001", "Unauthorized"),
  EMP_400("EMP_400", "Bad request"),
  EMP_404("EMP_404", "Resource not found"),
  EMP_422("EMP_422", "Unprocessable entity"),
  FORBIDDEN("EMP_403", "Forbidden"),
  BUSINESS_ERROR("EMP_500", "Internal server error"),
  MISSING_REQUEST_BODY("EMP_400", "Request body is required"),
  MISSING_REQUEST_PARAMETER("EMP_400", "Required parameter is missing"),
  NOT_ACCEPTABLE("EMP_406", "Accept header not acceptable"),
  UNSUPPORTED_MEDIA_TYPE("EMP_415", "Content-Type not supported"),
  METHOD_NOT_ALLOWED("EMP_405", "HTTP method not allowed"),
  TYPE_MISMATCH("EMP_400", "Invalid parameter or path value"),
  MISSING_PATH_VARIABLE("EMP_400", "Required path variable is missing");

  private final String value;
  private final String message;

  ErrorCode(String value, String message) {
    this.value = value;
    this.message = message;
  }

  @Override
  public String getValue() {
    return value;
  }

  public String getMessage() {
    return message;
  }
}
