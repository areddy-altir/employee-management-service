package com.example.employeemanagement.exception;

import com.example.employeemanagement.models.dto.ErrorResponseDto;
import com.example.employeemanagement.response.ApiResponses;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.beans.TypeMismatchException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.WebUtils;

/** Global exception handler; returns fn-db–generated ErrorResponseDto. */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @SuppressWarnings("unchecked")
  private static void addToMap(Map<String, Object> map, String fieldName, String message) {
    int dot = fieldName.indexOf('.');
    if (dot > 0) {
      String outer = fieldName.substring(0, dot);
      addToMap(
          (Map<String, Object>) map.computeIfAbsent(outer, k -> new HashMap<>()),
          fieldName.substring(dot + 1),
          message);
    } else {
      String msg = message != null ? message : "no error message available";
      map.compute(fieldName, (k, v) -> v == null ? msg : ((String) v).concat(", " + msg));
    }
  }

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<ErrorResponseDto> handleAuthenticationException(AuthenticationException ex) {
    log.error("User is unauthorized. Reason: {}", ex.getMessage());
    return ApiResponses.withMsgAndErrorCode(
        HttpStatus.UNAUTHORIZED, ErrorCode.UNAUTHORIZED.getValue(), ErrorCode.UNAUTHORIZED);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    log.error("MethodArgumentNotValidException occurred.", ex);
    return ApiResponses.badRequestWithMsgAndErrorsAsObject(null, parseErrors(ex));
  }

  @Override
  protected ResponseEntity<Object> handleExceptionInternal(
      Exception ex, Object body, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
      request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
    }
    return ApiResponses.withMsgErrorsAndHeadersAsObject(status, ex.getMessage(), null, headers);
  }

  @ExceptionHandler(BadRequestException.class)
  public Object handleBadRequest(BadRequestException ex) {
    log.error("Bad Request exception. msg:{}", ex.getMessage());
    return ApiResponses.withMsgAndErrorCode(
        HttpStatus.BAD_REQUEST, ex.getMessage(), ex.getResponseCode());
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public Object handleNotFound(ResourceNotFoundException ex) {
    log.error("Resource not found. msg:{}", ex.getMessage());
    return ApiResponses.withMsgAndErrorCode(
        HttpStatus.NOT_FOUND, ex.getMessage(), ex.getResponseCode());
  }

  @ExceptionHandler(UnprocessableEntityException.class)
  public Object handleUnprocessableEntity(UnprocessableEntityException ex) {
    log.error("Unprocessable entity. msg:{}", ex.getMessage());
    return ApiResponses.withMsgAndErrorCode(
        HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage(), ex.getResponseCode());
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
    Map<String, Object> map = new HashMap<>();
    for (ConstraintViolation<?> v : ex.getConstraintViolations()) {
      addToMap(map, v.getPropertyPath().toString(), v.getMessage());
    }
    return ApiResponses.withMsgAndErrorsAsObject(HttpStatus.BAD_REQUEST, null, map);
  }

  private Map<String, Object> parseErrors(BindException ex) {
    BindingResult result = ex.getBindingResult();
    if (result.getAllErrors().isEmpty()) {
      return Map.of();
    }
    Map<String, Object> errorMap = new HashMap<>(result.getFieldErrors().size());
    for (FieldError fe : result.getFieldErrors()) {
      addToMap(errorMap, fe.getField(), fe.getDefaultMessage());
    }
    if (result.hasGlobalErrors()) {
      String global = result.getGlobalErrors().stream()
          .map(DefaultMessageSourceResolvable::getDefaultMessage)
          .map(m -> m != null ? m : "no error message available")
          .collect(Collectors.joining(", "));
      errorMap.put("globalError", global);
    }
    return errorMap;
  }

  @ExceptionHandler(ForbiddenException.class)
  public Object handleForbidden(ForbiddenException ex) {
    log.error("Forbidden exception. msg:{}", ex.getMessage());
    return ApiResponses.withMsgAndErrorCode(
        HttpStatus.FORBIDDEN, ex.getMessage(), ex.getResponseCode());
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorResponseDto> handleAccessDenied(AccessDeniedException ex) {
    log.error("AccessDeniedException occurred", ex);
    return ApiResponses.withMsg(HttpStatus.FORBIDDEN, ex.getMessage());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponseDto> handleUnknown(Exception ex) {
    log.error("Exception occurred.", ex);
    return ApiResponses.serverErrorWithMsg(ex.getMessage());
  }

  @ExceptionHandler(BusinessException.class)
  public Object handleBusiness(BusinessException ex) {
    log.error("Business exception. msg:{}", ex.getMessage());
    return ApiResponses.withMsgAndErrorCode(
        HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex.getResponseCode());
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(
      org.springframework.http.converter.HttpMessageNotReadableException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    log.error("HttpMessageNotReadableException. Missing or invalid request body.", ex);
    String message = ex.getMessage();
    if (message != null && message.contains("Required request body is missing")) {
      message = "Request body is required";
    } else if (message != null && message.contains("JSON parse error")) {
      message = "Invalid JSON format in request body";
    } else {
      message = "Request body is missing or invalid";
    }
    ErrorResponseDto body = ApiResponses
        .withMsgAndErrorCode(HttpStatus.BAD_REQUEST, message, ErrorCode.MISSING_REQUEST_BODY)
        .getBody();
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
  }

  @Override
  protected ResponseEntity<Object> handleMissingServletRequestParameter(
      org.springframework.web.bind.MissingServletRequestParameterException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    log.error("MissingServletRequestParameterException. Parameter: {}", ex.getParameterName(), ex);
    String message = String.format("Required parameter '%s' is missing", ex.getParameterName());
    Map<String, Object> errors = new HashMap<>();
    errors.put(ex.getParameterName(), message);
    return ApiResponses.badRequestWithMsgAndErrorsAsObject(message, errors);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(
      HttpMediaTypeNotAcceptableException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    log.error("HttpMediaTypeNotAcceptableException. Accept header not acceptable.", ex);
    String message = ex.getMessage() != null ? ex.getMessage() : "Accept header specifies a representation the server cannot produce";
    ErrorResponseDto body = ApiResponses
        .withMsgAndErrorCode(HttpStatus.NOT_ACCEPTABLE, message, ErrorCode.NOT_ACCEPTABLE)
        .getBody();
    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).headers(headers).body(body);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
      HttpMediaTypeNotSupportedException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    log.error("HttpMediaTypeNotSupportedException. Content-Type not supported.", ex);
    String message = ex.getMessage() != null ? ex.getMessage() : "Request Content-Type is not supported (e.g. use application/json)";
    ErrorResponseDto body = ApiResponses
        .withMsgAndErrorCode(HttpStatus.UNSUPPORTED_MEDIA_TYPE, message, ErrorCode.UNSUPPORTED_MEDIA_TYPE)
        .getBody();
    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).headers(headers).body(body);
  }

  @Override
  protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
      HttpRequestMethodNotSupportedException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    log.error("HttpRequestMethodNotSupportedException. Method: {}", ex.getMethod(), ex);
    String message = ex.getMessage() != null ? ex.getMessage() : "HTTP method not allowed for this endpoint";
    ErrorResponseDto body = ApiResponses
        .withMsgAndErrorCode(HttpStatus.METHOD_NOT_ALLOWED, message, ErrorCode.METHOD_NOT_ALLOWED)
        .getBody();
    return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).headers(headers).body(body);
  }

  @Override
  protected ResponseEntity<Object> handleTypeMismatch(
      TypeMismatchException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    log.error("TypeMismatchException. Property: {}, required type: {}", ex.getPropertyName(), ex.getRequiredType(), ex);
    String message = ex.getMessage() != null ? ex.getMessage() : "Invalid parameter or path value (e.g. invalid UUID)";
    ErrorResponseDto body = ApiResponses
        .withMsgAndErrorCode(HttpStatus.BAD_REQUEST, message, ErrorCode.TYPE_MISMATCH)
        .getBody();
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).body(body);
  }

  @Override
  protected ResponseEntity<Object> handleMissingPathVariable(
      MissingPathVariableException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    log.error("MissingPathVariableException. Variable: {}", ex.getVariableName(), ex);
    String message = String.format("Required path variable '%s' is missing", ex.getVariableName());
    ErrorResponseDto body = ApiResponses
        .withMsgAndErrorCode(HttpStatus.BAD_REQUEST, message, ErrorCode.MISSING_PATH_VARIABLE)
        .getBody();
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).body(body);
  }
}
