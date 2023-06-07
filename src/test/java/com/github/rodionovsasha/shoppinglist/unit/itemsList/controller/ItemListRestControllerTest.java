package com.github.rodionovsasha.shoppinglist.unit.itemsList.controller;

import com.github.rodionovsasha.shoppinglist.controllers.ItemsListRestController;
import com.github.rodionovsasha.shoppinglist.dto.ItemsListDto;
import com.github.rodionovsasha.shoppinglist.entities.ItemsList;
import com.github.rodionovsasha.shoppinglist.services.ItemsListService;
import com.github.rodionovsasha.shoppinglist.services.impl.ItemsListServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;

import static com.github.rodionovsasha.shoppinglist.unit.utils.DTOBuilder.getDefaultItemsList1Dto;
import static com.github.rodionovsasha.shoppinglist.unit.utils.ModelBuilder.*;
import static com.github.rodionovsasha.shoppinglist.unit.utils.ModelBuilder.getItemsForItemsList2;
import static junit.framework.TestCase.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

public class ItemListRestControllerTest {

    private ItemsListService itemsListService;
    private ModelMapper modelMapper;
    private ItemsListRestController itemsListRestController;

    @Before
    public void init(){
        itemsListService = mock(ItemsListServiceImpl.class);
        modelMapper = spy(ModelMapper.class);
        itemsListRestController = new ItemsListRestController(itemsListService, modelMapper);
    }

    @Test
    public void testGetAllList(){
        ArrayList<ItemsList> serviceResponse = new ArrayList<>();
        ItemsList itemsList1 = getDefaultItemsList1();
        itemsList1.setItems(getItemsForItemsList1());
        ItemsList itemsList2 = getDefaultItemsList2();
        itemsList2.setItems(getItemsForItemsList2());
        serviceResponse.add(itemsList1);
        serviceResponse.add(itemsList2);

        when(itemsListService.findAllLists()).thenReturn(serviceResponse);
        List<ItemsListDto.GetAllListsResponse> allLists = itemsListRestController.getAllLists();

        assertEquals(serviceResponse.size(), allLists.size());
        for (int i = 0; i < allLists.size(); i++){
            assertEquals(serviceResponse.get(i).getId(), allLists.get(i).getId());
            assertEquals(serviceResponse.get(i).getName(), allLists.get(i).getName());
        }
        verify(modelMapper, times(1)).map(any(), any(Type.class));
        verify(modelMapper, times(1)).map(argThat((itemsLists) -> itemsLists == serviceResponse), any(Type.class));
        verify(itemsListService, times(1)).findAllLists();
    }

    @Test
    public void testGetItemsListById(){
        ItemsList response = getDefaultItemsList2();
        response.setItems(getItemsForItemsList2());

        when(itemsListService.getItemsListById(response.getId())).thenReturn(response);
        ItemsListDto.GetResponse itemsListFound = itemsListRestController.getItemsList(response.getId());

        assertEquals(response.getId(), itemsListFound.getId());
        assertEquals(response.getName(), itemsListFound.getName());
        assertEquals(1, itemsListFound.getItems().size());
        assertEquals(response.getItems().get(0).getId(), itemsListFound.getItems().get(0).getId());
        assertEquals(response.getItems().get(0).getName(), itemsListFound.getItems().get(0).getName());
        assertEquals(response.getItems().get(0).getComment(), itemsListFound.getItems().get(0).getComment());
        assertEquals(response.getItems().get(0).isBought(), itemsListFound.getItems().get(0).isBought());
        verify(modelMapper, times(1)).map(any(), any());
        verify(modelMapper, times(1)).map(argThat(x -> x == response), argThat(x -> x.equals(ItemsListDto.GetResponse.class)));
        verify(itemsListService, times(1)).getItemsListById(anyLong());
    }

    @Test
    public void testSaveItemsList(){
        ItemsList itemsList = getDefaultItemsList1();
        ItemsListDto itemsListDto = getDefaultItemsList1Dto();

        when(itemsListService.addItemsList(any(ItemsList.class))).thenReturn(itemsList.getId());
        ItemsListDto.CreateResponse response = itemsListRestController.saveItemsList(itemsListDto);
        assertEquals(itemsList.getId(), response.getId());
        verify(modelMapper, times(1)).map(any(), any());
        verify(modelMapper, times(1)).map(argThat(x -> x == itemsListDto), argThat(x -> x.equals(ItemsList.class)));
    }

    @Test
    public void testUpdateItemsList(){
        ItemsList itemsList = getDefaultItemsList1();
        ItemsListDto itemsListDto = getDefaultItemsList1Dto();
        String newName = "New name";
        itemsListDto.setName(newName);

        doNothing().when(itemsListService).updateItemsList(itemsList.getId(), itemsListDto.getName());
        itemsListRestController.updateItemsList(itemsList.getId(), itemsListDto);

        verify(itemsListService, times(1)).updateItemsList(anyLong(), anyString());
        verify(itemsListService, times(1)).updateItemsList(longThat(x -> x == itemsList.getId()), argThat(x -> x.equals(itemsListDto.getName())));
    }

    @Test
    public void deleteItemsList(){
        ItemsList itemsList = getDefaultItemsList1();

        doNothing().when(itemsListService).deleteItemsList(itemsList.getId());
        itemsListRestController.deleteItemsList(itemsList.getId());

        verify(itemsListService, times(1)).deleteItemsList(anyLong());
        verify(itemsListService, times(1)).deleteItemsList(longThat(x -> x == itemsList.getId()));
    }
}
