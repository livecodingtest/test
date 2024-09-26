package com.live.test.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

import lombok.Data;

@Data
@MappedSuperclass
@EntityListeners(AuditListener.class)
public abstract class DefaultEntity implements Serializable {

  private static final long serialVersionUID = 5746515215018862360L;

  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  private EntityStatus status;

  @Column(name = "created_by_user", updatable = false)
  private Long createdByUser;

  @Column(name = "deleted_by_user")
  private Long deletedByUser;
  
  public static class Fields {
    public static final String STATUS = "status";    
  }
}