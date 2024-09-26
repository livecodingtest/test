package com.live.test.config.i18n;

import java.util.Arrays;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import lombok.val;

@Configuration
public class MessageConfig {

  public static final String[] BASENAME =
      {"classpath:message", "classpath:validationMessages"};
  
  @Bean
  public MessageSource messageSource() {
    val source = new ReloadableResourceBundleMessageSource();
    source.setBasenames(Arrays.asList(BASENAME).toArray(new String[] {}));
    source.setUseCodeAsDefaultMessage(true);
    source.setFallbackToSystemLocale(true);
    source.setDefaultEncoding("UTF-8");
    return source;
  }

  @Bean
  @Primary
  public LocalValidatorFactoryBean getValidator() {
    val bean = new LocalValidatorFactoryBean();
    bean.setValidationMessageSource(messageSource());
    return bean;
  }

}
