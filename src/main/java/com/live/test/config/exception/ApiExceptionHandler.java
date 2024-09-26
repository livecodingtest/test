package com.live.test.config.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;

import com.live.test.config.i18n.GenericTranslator;
import com.live.test.config.i18n.Messages;
import com.live.test.config.i18n.Translator;

@ControllerAdvice
public class ApiExceptionHandler extends GenericApiExceptionHandler {

  private final Translator translator;

  public ApiExceptionHandler(Translator translator) {
    this.translator = translator;
  }

  @Override
  protected GenericTranslator translator() {
    return translator;
  }

  @Override
  protected String defaultMessageKey() {
    return Messages.DEFAULT_ERROR_MESSAGE;
  }
}