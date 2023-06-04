package com.github.rodionovsasha.shoppinglist.unit.item.service;

import com.github.rodionovsasha.shoppinglist.entities.Item;
import com.github.rodionovsasha.shoppinglist.entities.ItemsList;
import com.github.rodionovsasha.shoppinglist.repositories.ItemRepository;
import com.github.rodionovsasha.shoppinglist.services.ItemService;

import com.github.rodionovsasha.shoppinglist.services.ItemsListService;
import com.github.rodionovsasha.shoppinglist.services.impl.ItemServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mockito;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class DeleteItemServiceTest {

    private ItemRepository itemRepository;

    private ItemsListService itemsListService;

    private ItemServiceImpl itemService;

    // Arrange
    private Item testItem;

    @Before
    public void init() {
        itemRepository = spy(ItemRepository.class);
        itemService = new ItemServiceImpl(itemRepository, itemsListService);
        itemService = spy(itemService);
        testItem = new Item("Cheese");
        ItemsList testList = new ItemsList("My new list");

        testItem.setId(1);
        testItem.setComment("Tasty cheddar cheese");
        testItem.setBought(false);
        testItem.setItemsList(testList);
        testList.setId(1);
    }

    @Test
    @DisplayName("Successful deletion of an existing item")
    public void itemDeleteTest() {
        // Arrange
        doNothing().when(itemRepository).delete(testItem);
        Mockito.when(itemRepository.findById((long) 1)).thenReturn(Optional.of(testItem));

        // Action
        itemService.deleteItem(1);

        // Assert
        verify(itemRepository).delete(testItem);
        verify(itemService).getItemById(1);

    }
}
