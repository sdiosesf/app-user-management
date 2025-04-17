package com.services.application.usermanagement.model.entity;

import com.services.application.usermanagement.model.auditor.BaseEntity;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
@Entity
@Table(name = "user")
public class User extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  private String id;

  @NotBlank
  private String name;

  @Email
  @NotBlank
  private String email;

  @NotBlank
  private String password;

  @OneToMany(mappedBy = "user",fetch = FetchType.EAGER)
  private List<Phone> phones;
}