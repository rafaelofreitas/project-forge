package com.projectforge.template.infrastructure.persistence;

import com.projectforge.template.application.port.UserRepositoryPort;
import com.projectforge.template.domain.User;
import com.projectforge.template.infrastructure.mapper.UserMapper;
import com.projectforge.template.infrastructure.persistence.entity.UserEntity;
import com.projectforge.template.infrastructure.persistence.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {

    private static final UserMapper userMapper = UserMapper.INSTANCE;

    private final UserRepository userRepository;

    @Override
    public User save(final User user) {
        final UserEntity entity = userMapper.toEntity(user);

        final UserEntity savedEntity = userRepository.save(entity);

        return userMapper.toDomain(savedEntity);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<User> findById(final Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public void deleteById(final Long id) {
        userRepository.deleteById(id);
    }
}