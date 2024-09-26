package com.live.test.queryparam;

import java.io.Serializable;

import javax.validation.constraints.Size;

import com.live.test.domain.EntityStatus;

import lombok.Data;

@Data
public class ProductQueryParam implements Serializable {

  private static final long serialVersionUID = -3032531947161428541L;
  
  @Size(min = 3, max = 100, message = "{product.name.size}")
  private String name;
  private EntityStatus status;

}
