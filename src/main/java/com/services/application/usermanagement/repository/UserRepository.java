package com.services.application.usermanagement.repository;

import com.services.application.usermanagement.model.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

  List<User> findAll();

  User findByEmailAndIsActiveTrue(String name);

  User findByEmailAndIdNotAndIsActiveTrue(String email, String id);
}