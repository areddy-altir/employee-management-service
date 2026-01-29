package com.example.employeemanagement.exception;

/**
 * Exception when the requester does not have access. Results in 403.
 */
public class ForbiddenException extends ServiceException {

  public ForbiddenException(ResponseCode responseCode) {
    super(responseCode);
  }

  public ForbiddenException(ResponseCode responseCode, String message) {
    super(responseCode, message);
  }

  public ForbiddenException(ResponseCode responseCode, String message, Throwable cause) {
    super(responseCode, message, cause);
  }
}
