package co.altir.ems.exception;

/** Exception for unauthorized (401) errors. */
public final class UnauthorizedException extends ServiceException {

  public UnauthorizedException(ResponseCode responseCode) {
    super(responseCode);
  }

  public UnauthorizedException(ResponseCode responseCode, String message) {
    super(responseCode, message);
  }

  public UnauthorizedException(ResponseCode responseCode, String message, Throwable cause) {
    super(responseCode, message, cause);
  }
}

