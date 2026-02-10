package com.example.employeemanagement.exception;

/**
 * Exception for business/DB/IO or other internal failure. Results in 500.
 */
public class BusinessException extends ServiceException {

  public BusinessException(ResponseCode responseCode) {
    super(responseCode);
  }

  public BusinessException(ResponseCode responseCode, String message) {
    super(responseCode, message);
  }

  public BusinessException(ResponseCode responseCode, String message, Throwable cause) {
    super(responseCode, message, cause);
  }

  public static void throwEx(ResponseCode responseCode, Object... formatArgs) {
    throw new BusinessException(responseCode, String.format(responseCode.getValue(), formatArgs));
  }
}
