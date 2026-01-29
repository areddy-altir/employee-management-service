package com.example.employeemanagement.exception;

/** Exception when the request cannot be processed. Results in 422. */
public class UnprocessableEntityException extends ServiceException {

  public UnprocessableEntityException(ResponseCode responseCode) {
    super(responseCode);
  }

  public UnprocessableEntityException(ResponseCode responseCode, String message) {
    super(responseCode, message);
  }

  public UnprocessableEntityException(ResponseCode responseCode, String message, Throwable cause) {
    super(responseCode, message, cause);
  }
}
