package com.live.test.config.i18n;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class Translator extends GenericTranslator {

  private MessageSource source;

  public Translator(MessageSource messageSource) {
    source = messageSource;
  }

  @Override
  protected MessageSource source() {
    return source;
  }

}