package com.live.test.domain;

import static java.util.Objects.isNull;

import java.time.LocalDateTime;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
public class AuditListener {

  @PrePersist
  public void beforePersist(DefaultEntity entity) {
    if (isNull(entity.getStatus())) {
      entity.setStatus(EntityStatus.ACTIVE);
    }
    entity.setCreatedAt(LocalDateTime.now());
    entity.setCreatedByUser(NumberUtils.LONG_ONE);
  }

  @PreUpdate
  public void beforeUpdate(DefaultEntity entity) {
    if (!EntityStatus.ACTIVE.equals(entity.getStatus())) {
      entity.setDeletedByUser(NumberUtils.LONG_ONE);
    }
    entity.setUpdatedAt(LocalDateTime.now());
  }

}