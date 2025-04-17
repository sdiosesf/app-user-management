package com.services.application.usermanagement.model.auditor;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import com.services.application.usermanagement.util.Util;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import lombok.Data;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Data
public abstract class BaseEntity {

  @Column(name = "is_active")
  private boolean isActive;

  private String created;

  private String modified;

  @Column(name = "last_login")
  private String lastLogin;
	
  @PrePersist
  public void prePersist() {
	this.created = Util.getCurrentDateTime();
	this.lastLogin = Util.getCurrentDateTime();
	this.isActive = true;
  }
}
