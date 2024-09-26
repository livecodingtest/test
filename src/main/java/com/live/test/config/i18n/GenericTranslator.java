package com.live.test.config.i18n;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import lombok.val;

public abstract class GenericTranslator {

  public String toLocale(String code, Object... args) {
    val locale = LocaleContextHolder.getLocale();
    return toLocaleDefault(code, locale, args);
  }

  public String toLocaleDefault(String code, Locale locale, Object... args) {
    return source().getMessage(code, args, locale);
  }
  
  protected abstract MessageSource source();
}
