package com.example.demo;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class TestObjectGenerator {
    public static Cart getTestCart(User user) {
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setItems(Collections.singletonList(getTestItem()));
        cart.setTotal(BigDecimal.ONE);
        cart.setUser(user);

        return cart;
    }

    public static User getTestUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        user.setPassword("12345");
        user.setCart(getTestCart(user));

        return user;
    }

    public static Item getTestItem() {
        Item item = new Item();
        item.setId(1L);
        item.setName("test item");
        item.setPrice(BigDecimal.valueOf(11.99));
        return item;
    }

    public static List<Item> getRandomItemsList() {
        return LongStream
                .range(1, 10)
                .mapToObj(i ->
                {
                    Item item = getTestItem();
                    item.setId(i); return item;
                })
                .collect(Collectors.toList());
    }
}
