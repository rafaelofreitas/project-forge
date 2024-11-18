package com.projectforge.template.application.service;


import com.projectforge.template.application.exception.UserNotFoundException;
import com.projectforge.template.application.port.UserRepositoryPort;
import com.projectforge.template.application.port.UserServicePort;
import com.projectforge.template.domain.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceAdapter implements UserServicePort {

    private final UserRepositoryPort userRepository;

    @Override
    public User createUser(final User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(final Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public User updateUser(final User user) {
        if (this.userRepository.existsById(user.getId())) {
            return userRepository.save(user);
        }

        throw new UserNotFoundException(user.getId());
    }

    @Override
    public void deleteUser(final Long id) {
        if (this.userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new UserNotFoundException(id);
        }
    }
}
