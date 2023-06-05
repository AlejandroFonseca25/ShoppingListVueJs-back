package com.github.rodionovsasha.shoppinglist.unit.item.controller;

import com.github.rodionovsasha.shoppinglist.controllers.ItemRestController;
import com.github.rodionovsasha.shoppinglist.dto.ItemDto;
import com.github.rodionovsasha.shoppinglist.entities.Item;
import com.github.rodionovsasha.shoppinglist.entities.ItemsList;
import com.github.rodionovsasha.shoppinglist.services.ItemService;

import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class GetItemControllerTest {

    private ModelMapper mockModelMapper;

    private ModelMapper realModelMapper;

    private ItemService itemService;

    private ItemRestController itemController;

    private ItemsList testList;
    
    @Before
    public void init() {
        // Arrange
        testList = new ItemsList("Breakfast List");
        testList.setId(1);
        itemService = mock(ItemService.class);
        mockModelMapper = spy(ModelMapper.class);
        realModelMapper = new ModelMapper();
        itemController = new ItemRestController(itemService, mockModelMapper);
        itemController = spy(itemController);
    }

    @Test
    @DisplayName("Successful get of an item")
    public void itemGetTest() {
        // Arrange
        Item item = new Item();
        item.setId((long)1);
        item.setName("Oranges 2kg");
        item.setComment("I need 2kg for my juice");
        item.setBought(true);
        item.setItemsList(testList);

        Mockito.when(itemService.getItemById((long)1)).thenReturn(item);

        // Action
        ItemDto.GetResponse response = itemController.getItem((long)1);

        // Assert
        assertEquals(item.getId(), response.getId());
        assertEquals(item.getName(), response.getName());
        assertEquals(item.getComment(), response.getComment());
        verify(itemService, times(1)).getItemById(anyLong());

    }
    
}
