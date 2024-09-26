package com.live.test.dto;

import static java.util.Objects.nonNull;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.live.test.config.exception.ApiRuntimeException;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ErrorDTO implements Serializable {

  private static final long serialVersionUID = -2195900720004779903L;

  private List<String> messages;

  private Integer code;

  private HttpStatus status;

  public ErrorDTO(HttpStatus status) {
    this.status = status;
    this.code = status.value();
    this.messages = new LinkedList<>();
  }

  public ErrorDTO(String message, HttpStatus status) {
    this(status);
    addMessage(message);
  }

  public ErrorDTO(ApiRuntimeException ex) {
    this(ex.getMessage(), ex.getStatus());
  }

  public ErrorDTO(List<String> messages, HttpStatus status) {
    this(status);
    addAll(messages);
  }

  private void addAll(List<String> messages) {
    if (nonNull(messages)) {
      messages.forEach(this::addMessage);
    }
  }

  public void addMessage(String message) {
    if (nonNull(message)) {
      messages.add(message);
    }
  }

  @JsonIgnore
  public String getSingleMessage() {
    return String.join(", ", messages);
  }
}
