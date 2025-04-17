package com.services.application.usermanagement.repository;

import com.services.application.usermanagement.model.entity.Phone;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PhoneRepository extends CrudRepository<Phone, String> {

  @Transactional
  @Modifying
  @Query("DELETE FROM Phone p WHERE p.user.id = :userId")
  void deleteByUserId(String userId);
}