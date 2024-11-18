package com.projectforge.template.application.port;

import com.projectforge.template.domain.User;
import java.util.List;

public interface UserServicePort {
    User createUser(User user);

    List<User> getAllUsers();

    User getUserById(Long id);

    User updateUser(User user);

    void deleteUser(Long id);
}
