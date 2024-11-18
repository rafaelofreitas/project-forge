package com.projectforge.template.application.port;

import com.projectforge.template.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryPort {

  User save(User user);

  List<User> findAll();

  Optional<User> findById(Long id);

  boolean existsById(Long id);

  void deleteById(Long id);
}
