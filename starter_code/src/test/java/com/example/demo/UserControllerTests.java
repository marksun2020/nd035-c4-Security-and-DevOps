package com.example.demo;

import com.example.demo.controllers.UserController;
import com.example.demo.exeptions.CreateUserException;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;
import java.util.stream.Stream;

import static com.example.demo.TestObjectGenerator.getTestUser;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserController controller;

    @Test
    void findByIdTest() {
        User user = getTestUser();
        when(this.userRepository.findById(any())).thenReturn(Optional.of(user));

        ResponseEntity<User> result = this.controller.findById(1L);

        Assertions.assertEquals(HttpStatus.OK,result.getStatusCode());
        // no need to test if objects are equal - they are equal because of repository mocking,
        // therefore it is enough to check the id as a quick check.
        // We just want to see the ResponseEntity-result
        Assertions.assertEquals(user.getId(), result.getBody().getId());
    }

    @ParameterizedTest
    @MethodSource
    void getItemsByNameTest(Map.Entry<HttpStatus, User> input) {
        User user = input.getValue();
        HttpStatus httpStatus = input.getKey();
        when(this.userRepository.findByUsername(any())).thenReturn(user);

        ResponseEntity<User> res = this.controller.findByUserName("any name");

        Assertions.assertEquals(httpStatus, res.getStatusCode());
        if (httpStatus.is2xxSuccessful()) {
            Assertions.assertEquals(user.getId(), res.getBody().getId());
        }
    }

    @Test
    void createUserHappyPathTest() {
        final HttpStatus status = HttpStatus.OK;
        final String pass = "12345678";
        final String confirmPass = pass;
        CreateUserRequest request = getCreateUserRequest(pass, confirmPass);

        ResponseEntity<User> result = this.controller.createUser(request);

        Assertions.assertEquals(status, result.getStatusCode());
    }

    @Test
    void createUserBadRequestTest() {
        final HttpStatus status = HttpStatus.BAD_REQUEST;
        final String pass = "abcdefjh";
        final String confirmPass = "abcdefjH";
        CreateUserRequest request = getCreateUserRequest(pass, confirmPass);

        CreateUserException exception = assertThrows(CreateUserException.class, () -> this.controller.createUser(request));


        Assertions.assertEquals(exception.getUserName(), request.getUsername());
        Assertions.assertTrue(exception.getMessage().contains("createUser. Error: "));
    }

    private static Stream<Map.Entry<HttpStatus, User>> getItemsByNameTest() {
        return Stream.of(
                new AbstractMap.SimpleEntry(HttpStatus.OK, getTestUser()),
                new AbstractMap.SimpleEntry(HttpStatus.NOT_FOUND, null)
        );
    }

    private static CreateUserRequest getCreateUserRequest(String pass, String confirmPass) {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("user name");
        request.setPassword(pass);
        request.setConfirmPassword(confirmPass);

        return request;
    }
}
