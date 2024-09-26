package com.live.test.config.exception;

import org.springframework.http.HttpStatus;

import com.live.test.config.i18n.GenericTranslator;

public class ApiRuntimeException extends RuntimeException {

  private static final long serialVersionUID = -3882807453175214074L;

  final HttpStatus status;

  public ApiRuntimeException(String code, GenericTranslator translator, HttpStatus status) {
    this(translator.toLocale(code), status);
  }

  protected ApiRuntimeException(String message, HttpStatus status, Exception e) {
    super(message, e);
    this.status = status;
  }

  public ApiRuntimeException(String code, GenericTranslator translator, HttpStatus status, Object... args) {
    this(translator.toLocale(code, args), status);
  }

  public ApiRuntimeException(String message, HttpStatus status) {
    super(message);
    this.status = status;
  }

  public ApiRuntimeException(String message, Exception e) {
    super(message, e);
    this.status = HttpStatus.INTERNAL_SERVER_ERROR;
  }

  public HttpStatus getStatus() {
    return status;
  }
  
}
