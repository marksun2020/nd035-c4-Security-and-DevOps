package com.example.demo;

import com.example.demo.controllers.ItemController;
import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static com.example.demo.TestObjectGenerator.getTestUser;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderControllerTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderController controller;

    @Test
    void getOrdersForUserTest() {
        when(this.userRepository.findByUsername(any())).thenReturn(getTestUser());

        ResponseEntity<List<UserOrder>>  res = controller.getOrdersForUser("user name");

        Assertions.assertEquals(HttpStatus.OK, res.getStatusCode());
    }

    @Test
    void getOrdersForNullUserTest() {
        when(this.userRepository.findByUsername(any())).thenReturn(null);

        ResponseEntity<List<UserOrder>>  res = controller.getOrdersForUser("user name");

        Assertions.assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
    }

    @Test
    void submitTest() {
        when(this.userRepository.findByUsername(any())).thenReturn(getTestUser());

        ResponseEntity<UserOrder>  res = controller.submit("user name");

        Assertions.assertEquals(HttpStatus.OK, res.getStatusCode());
    }

    @Test
    void submitNullUserTest() {
        when(this.userRepository.findByUsername(any())).thenReturn(null);

        ResponseEntity<UserOrder>  res = controller.submit("user name");

        Assertions.assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
    }


}
