package com.github.rodionovsasha.shoppinglist.unit.item.service;

import com.github.rodionovsasha.shoppinglist.entities.Item;
import com.github.rodionovsasha.shoppinglist.entities.ItemsList;
import com.github.rodionovsasha.shoppinglist.exceptions.NotFoundException;
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
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class GetItemServiceTest {

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
        
        testList = new ItemsList("List for Brekfast");
        testList.setId(1);
        testItem = new Item("Oranges 2kg");
        testItem.setId(1);
        testItem.setComment("I need 2kg for my juice");
        testItem.setBought(false);
        testItem.setItemsList(testList);
    }
    @Test
    @DisplayName("Successful get of an existing item")
    public void itemGetTest() {

        Mockito.when(itemRepository.findById((long) 1)).thenReturn(Optional.of(testItem));
        Mockito.when(itemsListService.getItemsListById(1)).thenReturn(testList);

        // Action
        Item getItem = itemService.getItemById(1);

        // Assert
        assertNotNull(getItem);
        assertThat(getItem.getId(), equalTo(testItem.getId()));
        assertThat(getItem.getName(), equalTo(testItem.getName()));
        assertThat(getItem.getComment(), equalTo(testItem.getComment()));
        assertThat(getItem.isBought(), equalTo(testItem.isBought()));
        assertThat(getItem.getItemsList(), equalTo(testItem.getItemsList()));
    }
    @Test
    @DisplayName("Incorrect get of an non existing item")
    public void itemGetWithNoExistingIdTest() {

        when(itemRepository.findById((long)-1)).thenReturn(Optional.empty());
        NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> itemService.getItemById((long)-1));

        assertEquals("The entity with id '"+ "-1" +"' could not be found", notFoundException.getMessage());
        verify(itemRepository, times(1)).findById(any());
    }
    
}
