package com.projectforge.template.api;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectforge.template.ControllerTest;
import com.projectforge.template.application.exception.UserNotFoundException;
import com.projectforge.template.application.port.UserServicePort;
import com.projectforge.template.domain.User;
import com.projectforge.template.infrastructure.api.UserAPI;
import com.projectforge.template.infrastructure.api.dto.UserInputDTO;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@ControllerTest(controllers = UserAPI.class)
class UserAPITest {

    public static final String USERS = "/users";
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserServicePort userService;

    @Test
    void givenValidUserData_whenCallsCreateUser_thenShouldReturnUserId() throws Exception {
        final String expectedName = "John Doe";
        final String expectedEmail = "johndoe@example.com";

        final UserInputDTO userInput = new UserInputDTO(expectedName, expectedEmail);

        Mockito.when(userService.createUser(any(User.class)))
                .thenReturn(new User(123L, expectedName, expectedEmail));

        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(USERS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(userInput));

        final ResultActions response = mvc.perform(request).andDo(print());

        response
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/users/123"))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(123)));

        Mockito.verify(userService, times(1))
                .createUser(argThat(cmd ->
                        expectedName.equals(cmd.getName()) &&
                                expectedEmail.equals(cmd.getEmail())));
    }

    @Test
    void givenInvalidUserData_whenCallsCreateUser_thenShouldReturnValidationError() throws Exception {
        final String expectedEmail = "johndoe@example.com";
        final UserInputDTO userInput = new UserInputDTO("", expectedEmail);

        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(USERS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(userInput));

        final ResultActions response = mvc.perform(request).andDo(print());

        response
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo("Validation Failed")))
                .andExpect(jsonPath("$.errors[0].message", equalTo("'name' cannot be empty")))
                .andExpect(jsonPath("$.errors[1].message", equalTo("Name must be between 3 and 50 characters.")));

        Mockito.verify(userService, times(0))
                .createUser(any(User.class));
    }

    @Test
    void givenValidUserId_whenCallsGetUser_thenShouldReturnUser() throws Exception {
        final long expectedId = 123L;
        final String expectedName = "John Doe";
        final String expectedEmail = "johndoe@example.com";

        final User user = new User(expectedId, expectedName, expectedEmail);

        Mockito.when(userService.getUserById(expectedId)).thenReturn(user);

        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(USERS + "/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final ResultActions response = mvc.perform(request).andDo(print());

        response
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(123)))
                .andExpect(jsonPath("$.name", equalTo(expectedName)))
                .andExpect(jsonPath("$.email", equalTo(expectedEmail)));

        Mockito.verify(userService, times(1)).getUserById(expectedId);
    }

    @Test
    void givenInvalidUserId_whenCallsGetUser_thenShouldReturnNotFound() throws Exception {
        final long expectedId = 123L;
        final String expectedErrorMessage = "User with ID 123 not found.";

        Mockito.when(userService.getUserById(expectedId))
                .thenThrow(new UserNotFoundException(expectedId));

        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(USERS + "/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));

        Mockito.verify(userService, times(1)).getUserById(expectedId);
    }

    @Test
    void givenValidUserData_whenCallsUpdateUser_thenShouldReturnUpdatedUser() throws Exception {
        final long expectedId = 123L;
        final String expectedName = "John Updated";
        final String expectedEmail = "johnupdated@example.com";

        final UserInputDTO updateRequest = new UserInputDTO(expectedName, expectedEmail);

        final User user = new User(expectedId, expectedName, expectedEmail);

        Mockito.when(userService.updateUser(user))
                .thenReturn(user);

        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put(USERS + "/{id}", expectedId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(updateRequest));

        final ResultActions response = mvc.perform(request).andDo(print());

        response
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(123)))
                .andExpect(jsonPath("$.name", equalTo(expectedName)))
                .andExpect(jsonPath("$.email", equalTo(expectedEmail)));

        Mockito.verify(userService, times(1))
                .updateUser(user);
    }

    @Test
    void givenValidUserId_whenCallsDeleteUser_thenShouldReturnNoContent() throws Exception {
        final long expectedId = 123L;

        Mockito.doNothing().when(userService).deleteUser(expectedId);

        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete(USERS + "/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final ResultActions response = mvc.perform(request).andDo(print());

        response
                .andExpect(status().isNoContent());

        Mockito.verify(userService, times(1)).deleteUser(expectedId);
    }


    @Test
    void whenCallsListUsers_thenShouldReturnUsers() throws Exception {
        final User user1 = new User(123L, "John Doe", "johndoe@example.com");
        final User user2 = new User(456L, "Jane Doe", "janedoe@example.com");

        Mockito.when(userService.getAllUsers())
                .thenReturn(List.of(user1, user2));

        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(USERS)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final ResultActions response = mvc.perform(request).andDo(print());

        response
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", equalTo(2)))
                .andExpect(jsonPath("$[0].id", equalTo(123)))
                .andExpect(jsonPath("$[1].id", equalTo(456)));

        Mockito.verify(userService, times(1)).getAllUsers();
    }
}
