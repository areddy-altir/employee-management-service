package com.example.employeemanagement.exception;

/**
 * Exception for invalid request. Results in 400. Message is sent in the response.
 */
public class BadRequestException extends ServiceException {

  public BadRequestException(ResponseCode responseCode) {
    super(responseCode);
  }

  public BadRequestException(ResponseCode responseCode, String message) {
    super(responseCode, message);
  }

  public BadRequestException(ResponseCode responseCode, String message, Throwable cause) {
    super(responseCode, message, cause);
  }
}
