package com.live.test.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class DefaultDTO implements Serializable {

  private static final long serialVersionUID = -7324975470637360720L;

  public DefaultDTO(ErrorDTO error) {
    this.error = error;
  }

  private ErrorDTO error; 

}
