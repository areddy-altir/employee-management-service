package com.example.employeemanagement.exception;

/**
 * Exception when the requested resource was not found. Results in 404.
 */
public final class ResourceNotFoundException extends ServiceException {

  public ResourceNotFoundException(ResponseCode responseCode) {
    super(responseCode);
  }

  public ResourceNotFoundException(ResponseCode responseCode, String message) {
    super(responseCode, message);
  }
}
