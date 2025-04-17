package com.services.application.usermanagement.model.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Data
@ToString
@Entity
@Table(name = "phone")
public class Phone implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String number;

  @Column(name = "city_code")
  private String cityCode;

  @Column(name = "country_code")
  private String countryCode;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;
}