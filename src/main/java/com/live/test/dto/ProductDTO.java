package com.live.test.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import com.live.test.domain.EntityStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO implements Serializable {

  private static final long serialVersionUID = 6802502544955589813L;
  
  private Long id;
  
  @NotEmpty(message = "{product.name.notEmpty}")
  @Size(min = 3, max = 100, message = "{product.name.size}")
  private String name;
  
  @Size(min = 3, max = 255, message = "{product.description.size}")
  private String description;
  
  @NotNull(message = "{product.name.notNull}")
  @Positive(message = "{product.preco.positive}")
  @Digits(integer = 10, fraction = 2, message = "{product.preco.digits}")
  private BigDecimal price;
  
  private EntityStatus status;

  public static class Fields {
    public static final String ID = "id";
    public static final String NAME = "name";   
  }
}