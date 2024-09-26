package com.live.test.config.exception;

import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.live.test.config.i18n.GenericTranslator;
import com.live.test.dto.DefaultDTO;
import com.live.test.dto.ErrorDTO;

import lombok.val;

public abstract class GenericApiExceptionHandler extends ResponseEntityExceptionHandler {
  
  @ExceptionHandler({ApiRuntimeException.class})
  public ResponseEntity<Object> handleApiRuntimeException(ApiRuntimeException ex, WebRequest request) {
    logger.error(ExceptionUtils.getRootCauseMessage(ex));
    return handleExceptionInternal(ex, new DefaultDTO(new ErrorDTO(ex)), new HttpHeaders(), ex.getStatus(), request);
  }

  @ExceptionHandler({TransactionSystemException.class})
  public ResponseEntity<Object> handlePersistenceException(final Exception ex, final WebRequest request) {
    val cause = ((TransactionSystemException) ex).getRootCause();

    if (cause instanceof ConstraintViolationException) {
      return handleConstraintViolationException(request, cause);
    }

    val error = new ErrorDTO(translator().toLocale(defaultMessageKey()), HttpStatus.INTERNAL_SERVER_ERROR);

    logger.error(ExceptionUtils.getRootCauseMessage(ex), ex);
    return handleExceptionInternal(ex, new DefaultDTO(error), new HttpHeaders(), error.getStatus(), request);
  }

  @Override
  protected ResponseEntity<Object> handleExceptionInternal(
      Exception ex,
      Object body,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    logger.error(ex);
    return super.handleExceptionInternal(ex, body, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {

    logger.error(ExceptionUtils.getRootCauseMessage(ex), ex);
    val errors =
        ex.getBindingResult().getAllErrors()
            .stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .collect(Collectors.toList());

    val error = new ErrorDTO(errors, status);

    return super.handleExceptionInternal(ex, new DefaultDTO(error), headers, status, request);
  }
 
  @Override
  protected ResponseEntity<Object> handleBindException(
          BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

    logger.error(ExceptionUtils.getRootCauseMessage(ex), ex);
    val errors =
            ex.getBindingResult().getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

    val error = new ErrorDTO(errors, status);

    return super.handleExceptionInternal(ex, new DefaultDTO(error), headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                HttpHeaders headers, HttpStatus status,
                                                                WebRequest request) {
    logger.error(ex.getMessage());
    val errors = new ErrorDTO(ex.getMessage(), status);
    return handleExceptionInternal(ex, new DefaultDTO(errors), new HttpHeaders(), errors.getStatus(), request);
  }

  @ExceptionHandler({ConstraintViolationException.class})
  public ResponseEntity<Object> handleConstraintViolationException(final Exception ex, final WebRequest request) {
    return handleConstraintViolationException(request, ex);
  }

  protected ResponseEntity<Object> handleConstraintViolationException(final WebRequest request, Throwable cause) {
    val cve = (ConstraintViolationException) cause;
    logger.error(ExceptionUtils.getRootCauseMessage(cve));

    val error = new ErrorDTO(HttpStatus.BAD_REQUEST);

    for (final ConstraintViolation<?> violation : cve.getConstraintViolations()) {
      error.addMessage(translator().toLocale(violation.getMessage()));
    }

    return handleExceptionInternal(cve, new DefaultDTO(error), new HttpHeaders(), error.getStatus(), request);
  }

  protected abstract GenericTranslator translator();

  protected abstract String defaultMessageKey();
}
