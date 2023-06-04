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
import static org.mockito.Mockito.*;

public class UpdateItemControllerTest {

    private ModelMapper mockModelMapper;

    private ModelMapper realModelMapper;

    private ItemService itemService;

    private ItemRestController itemController;

    private ItemsList testList;

    @Before
    public void init() {
        // Arrange
        testList = new ItemsList("My new list");
        testList.setId(1);
        itemService = mock(ItemService.class);
        mockModelMapper = spy(ModelMapper.class);
        realModelMapper = new ModelMapper();
        itemController = new ItemRestController(itemService, mockModelMapper);
        itemController = spy(itemController);
    }

    @Test
    @DisplayName("Successful update of an existing item")
    public void itemUpdateTest() {
        // Arrange
        ItemDto newItemDto = new ItemDto();
        newItemDto.setName("Cheese");
        newItemDto.setComment("Delicious parmesan cheese");
        newItemDto.setBought(true);
        newItemDto.setListId(testList.getId());

        Item newItem = realModelMapper.map(newItemDto, Item.class);

        Mockito.when(mockModelMapper.map(newItemDto,Item.class)).thenReturn(newItem);
        Mockito.when(itemService.updateItem(1, 1, newItem)).thenReturn(newItem);

        // Action
        itemController.updateItem(1, newItemDto);

        // Assert
        verify(itemController).updateItem(1, newItemDto);
        verify(mockModelMapper).map(newItemDto,Item.class);
    }
}
