package com.projectforge.template.infrastructure.persistence.repository;

import com.projectforge.template.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
  // Custom query methods can be added
  // For example:
  UserEntity findByEmail(String email);
}
