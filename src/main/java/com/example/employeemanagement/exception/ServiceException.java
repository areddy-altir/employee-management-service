package com.example.employeemanagement.exception;

import lombok.Getter;

/** Base exception for all API exceptions; carries a ResponseCode. */
@Getter
public class ServiceException extends RuntimeException {

  private final ResponseCode responseCode;

  public ServiceException(ResponseCode responseCode) {
    super(responseCode.getValue());
    this.responseCode = responseCode;
  }

  public ServiceException(ResponseCode responseCode, String message) {
    super(message);
    this.responseCode = responseCode;
  }

  public ServiceException(ResponseCode responseCode, String message, Throwable cause) {
    super(message, cause);
    this.responseCode = responseCode;
  }
}
