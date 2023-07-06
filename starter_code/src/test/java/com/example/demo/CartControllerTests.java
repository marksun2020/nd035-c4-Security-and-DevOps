package com.example.demo;

import com.example.demo.controllers.CartController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static com.example.demo.TestObjectGenerator.getTestItem;
import static com.example.demo.TestObjectGenerator.getTestUser;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartControllerTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private CartController controller;

    @Test
    void addTocartUnknownUserTest() {
        when(this.userRepository.findByUsername(any())).thenReturn(null);

        ResponseEntity<Cart> result = this.controller.addToCart(new ModifyCartRequest());

        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        Assertions.assertNull(result.getBody());
    }

    @Test
    void addToCartUnknownItemTest() {
        when(this.userRepository.findByUsername(any())).thenReturn(getTestUser());
        when(this.itemRepository.findById(any())).thenReturn(Optional.empty());

        ResponseEntity<Cart> result = this.controller.addToCart(new ModifyCartRequest());

        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        Assertions.assertNull(result.getBody());
    }

    @Test
    void addToCartHappyPathTest() {
        when(this.userRepository.findByUsername(any())).thenReturn(getTestUser());
        when(this.itemRepository.findById(any())).thenReturn(Optional.of(getTestItem()));

        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(2L);
        ResponseEntity<Cart> result = this.controller.addToCart(request);

        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void removeFromCartUnknownUserTest() {
        when(this.userRepository.findByUsername(any())).thenReturn(null);

        ResponseEntity<Cart> result = this.controller.removeFromCart(new ModifyCartRequest());

        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        Assertions.assertNull(result.getBody());
    }

    @Test
    void removeFromCartUnknownItemTest() {
        when(this.userRepository.findByUsername(any())).thenReturn(getTestUser());
        when(this.itemRepository.findById(any())).thenReturn(Optional.empty());

        ResponseEntity<Cart> result = this.controller.removeFromCart(new ModifyCartRequest());

        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        Assertions.assertNull(result.getBody());
    }

    @Test
    void removeFromCartHappyPathTest() {
        when(this.userRepository.findByUsername(any())).thenReturn(getTestUser());
        when(this.itemRepository.findById(any())).thenReturn(Optional.of(getTestItem()));

        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(2L);
        ResponseEntity<Cart> result = this.controller.removeFromCart(request);

        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    }
}
