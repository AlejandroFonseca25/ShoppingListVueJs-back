package com.github.rodionovsasha.shoppinglist.unit.item.service;

import com.github.rodionovsasha.shoppinglist.entities.Item;
import com.github.rodionovsasha.shoppinglist.entities.ItemsList;
import com.github.rodionovsasha.shoppinglist.repositories.ItemRepository;

import com.github.rodionovsasha.shoppinglist.services.ItemsListService;
import com.github.rodionovsasha.shoppinglist.services.impl.ItemServiceImpl;
import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mockito;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import org.junit.Test;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.Mockito.*;

public class UpdateItemServiceTest {

    private ItemRepository itemRepository;

    private ItemsListService itemsListService;

    private ItemServiceImpl itemService;

    // Arrange
    private Item testItem;
    private ItemsList testList;

    @Before
    public void init() {
        itemRepository = mock(ItemRepository.class);
        itemsListService = mock(ItemsListService.class);
        itemService = new ItemServiceImpl(itemRepository, itemsListService);

        testItem = new Item("Cheese");
        testList = new ItemsList("My new list");
        testItem.setId(1);
        testItem.setComment("Tasty cheddar cheese");
        testItem.setBought(false);
        testItem.setItemsList(testList);
        testList.setId(1);
    }

    @Test
    @DisplayName("Successful update of an existing item")
    public void itemUpdateTest() {
        // Arrange
        Item newItem = new Item("Cheese");
        newItem.setId(2);
        newItem.setComment("Delicious parmesan cheese");
        newItem.setBought(true);
        newItem.setItemsList(testList);

/*
        Mockito.when(itemRepository.save(newItem)).thenReturn(newItem);
        Mockito.when(itemRepository.findById((long) 1)).thenReturn(Optional.of(testItem));
        Mockito.when(itemsListService.getItemsListById( 1)).thenReturn(testList);
*/
        Mockito.when(itemRepository.save(any(Item.class))).thenReturn(newItem);
        Mockito.when(itemRepository.findById((long) 1)).thenReturn(Optional.of(testItem));
        Mockito.when(itemsListService.getItemsListById(1)).thenReturn(testList);

        // Action
        Item updatedItem = itemService.updateItem(1, 1, newItem);

        // Assert
        assertNotNull(updatedItem);
        assertThat(updatedItem.getId(), equalTo(newItem.getId()));
        assertThat(updatedItem.getName(), equalTo(newItem.getName()));
        assertThat(updatedItem.getComment(), equalTo(newItem.getComment()));
        assertThat(updatedItem.isBought(), equalTo(newItem.isBought()));
        assertThat(updatedItem.getItemsList(), equalTo(newItem.getItemsList()));
    }
}
