package com.live.test.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "product")
public class Product extends DefaultEntity implements Serializable {

  private static final long serialVersionUID = 4117432548889327821L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private String description;
  private BigDecimal price;

  public static class Fields {
    public static final String ID = "id";
    public static final String NAME = "name";
    
    public static class WithAlias {
      public static final String ALIAS = "PR";
      public static final String ID = ALIAS + "." + Fields.ID;
      public static final String NAME = ALIAS + "." + Fields.NAME;
      public static final String STATUS = ALIAS + "." + DefaultEntity.Fields.STATUS;
    }
  }
}