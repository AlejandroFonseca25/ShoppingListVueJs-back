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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CreateItemControllerTest {

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
    @DisplayName("Successful created of an item")
    public void itemCreateTest() {
        // Arrange
        ItemDto newItemDto = new ItemDto();
        newItemDto.setName("Oranges 2kg");
        newItemDto.setComment("I need 2kg for my juice");
        newItemDto.setBought(true);
        newItemDto.setListId(testList.getId());

        Item newItem = realModelMapper.map(newItemDto, Item.class);

        Mockito.when(mockModelMapper.map(newItemDto,Item.class)).thenReturn(newItem);
        Mockito.when(itemService.addItem((long)1, newItem)).thenReturn((long)0);

        // Action
        ItemDto.CreateResponse response = itemController.addItem(newItemDto);

        // Assert
        verify(itemController).addItem( newItemDto);
        verify(mockModelMapper).map(newItemDto,Item.class);
        verify(itemService, times(1)).addItem(anyLong(),any());
        assertEquals(newItemDto.getListId(), response.getListId());
    }
    
}
