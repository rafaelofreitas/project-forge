package com.projectforge.template.infrastructure.api.controller;

import com.projectforge.template.application.port.UserServicePort;
import com.projectforge.template.domain.User;
import com.projectforge.template.infrastructure.api.UserAPI;
import com.projectforge.template.infrastructure.api.dto.UserInputDTO;
import com.projectforge.template.infrastructure.api.dto.UserOutputDTO;
import com.projectforge.template.infrastructure.mapper.UserMapper;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController implements UserAPI {

    private static final UserMapper userMapper = UserMapper.INSTANCE;

    private final UserServicePort userService;

    @Override
    public ResponseEntity<UserOutputDTO> createUser(@Valid @RequestBody final UserInputDTO userDTO) {
        final User createdUser = userService.createUser(userMapper.toDomain(userDTO));

        return ResponseEntity.created(URI.create("/users/" + createdUser.getId()))
                .body(userMapper.toOutput(createdUser));
    }

    @Override
    public List<UserOutputDTO> getAllUsers() {
        return userService.getAllUsers()
                .stream()
                .map(userMapper::toOutput)
                .toList();
    }

    @Override
    public ResponseEntity<UserOutputDTO> getById(final Long id) {
        return ResponseEntity.ok(userMapper.toOutput(userService.getUserById(id)));
    }

    @Override
    public ResponseEntity<UserOutputDTO> updateById(final Long id, final UserInputDTO userDTO) {
        final User user = userMapper.toDomain(userDTO);
        user.setId(id);

        final User updatedUser = userService.updateUser(user);

        return ResponseEntity.ok(userMapper.toOutput(updatedUser));
    }

    @Override
    public ResponseEntity<Void> deleteById(final Long id) {
        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }
}
