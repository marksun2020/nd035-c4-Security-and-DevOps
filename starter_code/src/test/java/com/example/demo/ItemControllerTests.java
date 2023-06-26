package com.example.demo;

import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemControllerTests {

    @Mock
    private ItemRepository repository;

    @InjectMocks
    private ItemController controller;

    @Test
    void getItemsTest() {
        List<Item> itemsList = getRandomItemsList();
        when(this.repository.findAll()).thenReturn(itemsList);

        ResponseEntity<List<Item>>  res = controller.getItems();

        Assertions.assertEquals(HttpStatus.OK, res.getStatusCode());
        Assertions.assertEquals(itemsList.size(), res.getBody().size());
    }

    @Test
    void getItemByIdTest() {
        Item item = this.getTestItem();
        when(this.repository.findById(any())).thenReturn(Optional.of(item));

        ResponseEntity<Item> res = controller.getItemById(1L);

        Assertions.assertEquals(HttpStatus.OK, res.getStatusCode());
        Assertions.assertEquals(1, res.getBody().getId());
    }

    @ParameterizedTest
    @MethodSource
    void getItemsByNameTest(Map.Entry<HttpStatus, List<Item>> input) {
        when(this.repository.findByName(any())).thenReturn(input.getValue());

        ResponseEntity<List<Item>> res = this.controller.getItemsByName("any name");

        Assertions.assertEquals(input.getKey(), res.getStatusCode());
    }

    private static Stream<Map.Entry<HttpStatus, List<Item>>> getItemsByNameTest() {
        return Stream.of(
                new AbstractMap.SimpleEntry(HttpStatus.OK, getRandomItemsList()),
                new AbstractMap.SimpleEntry(HttpStatus.NOT_FOUND, Collections.EMPTY_LIST),
                new AbstractMap.SimpleEntry(HttpStatus.NOT_FOUND, null)
        );
    }

    private static List<Item> getRandomItemsList() {
        return LongStream.range(1, 10).mapToObj(i -> {Item item = new Item(); item.setId(i); return item;}).collect(Collectors.toList());
    }

    private Item getTestItem() {
        Item item = new Item();
        item.setId(1L);
        item.setName("test item");
        item.setPrice(BigDecimal.valueOf(11.99));
        return item;
    }
}
