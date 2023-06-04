package com.github.rodionovsasha.shoppinglist.unit.item.controller;

import com.github.rodionovsasha.shoppinglist.controllers.ItemRestController;
import com.github.rodionovsasha.shoppinglist.services.ItemService;

import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.Test;
import org.modelmapper.ModelMapper;

import static org.mockito.Mockito.*;
public class DeleteItemControllerTest {

    private ItemService itemService;

    private ItemRestController itemController;

    @Before
    public void init(){
        itemService = mock(ItemService.class);
        ModelMapper modelMapper = mock(ModelMapper.class);
        itemController = new ItemRestController(itemService, modelMapper);
    }

    @Test
    @DisplayName("Successful update of an existing item")
    public void itemUpdateTest() {
        // Arrange
        doNothing().when(itemService).deleteItem(1);

        // Action
        itemController.deleteItem(1);

        // Assert
        verify(itemService).deleteItem(1);
    }
}
