package com.github.rodionovsasha.shoppinglist.unit.itemsList.service;

import com.github.rodionovsasha.shoppinglist.entities.ItemsList;
import com.github.rodionovsasha.shoppinglist.exceptions.NotFoundException;
import com.github.rodionovsasha.shoppinglist.repositories.ItemsListRepository;
import com.github.rodionovsasha.shoppinglist.services.impl.ItemsListServiceImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Optional;

import static com.github.rodionovsasha.shoppinglist.unit.utils.ModelBuilder.*;
import static junit.framework.TestCase.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ItemListServiceTest {
    private ItemsListRepository itemsListRepository;
    private ItemsListServiceImpl itemsListService;

    @Before
    public void init(){
        itemsListRepository = spy(ItemsListRepository.class);
        itemsListService = new ItemsListServiceImpl(itemsListRepository);
    }

    @Test
    public void testCreateItemList(){
        ItemsList response = getDefaultItemsList2();
        response.setItems(getItemsForItemsList2());
        ItemsList itemsList = getDefaultItemsList2();
        itemsList.setItems(getItemsForItemsList2());

        when(itemsListRepository.save(itemsList)).thenReturn(response);
        long id = itemsListService.addItemsList(itemsList);

        assertEquals(response.getId(), id);
        verify(itemsListRepository, times(0)).findById(any());
        verify(itemsListRepository, times(1)).save(argThat(new ItemsListMatcher(itemsList, 1)));
        verify(itemsListRepository, times(1)).save(any());
        verify(itemsListRepository, times(0)).findAll();
    }

    @Test
    public void testUpdateItemsList(){
        ItemsList response = getDefaultItemsList2();
        response.setItems(getItemsForItemsList2());
        String newName = "New name";
        ItemsList itemsListToCompare = getDefaultItemsList2();
        itemsListToCompare.setItems(getItemsForItemsList2());
        itemsListToCompare.setName(newName);

        when(itemsListRepository.findById(response.getId())).thenReturn(Optional.of(response));
        itemsListService.updateItemsList(response.getId(), newName);

        verify(itemsListRepository, times(1)).findById(any());
        verify(itemsListRepository, times(1)).findById(argThat(x -> x.equals(response.getId())));
        verify(itemsListRepository, times(1)).save(argThat(new ItemsListMatcher(itemsListToCompare, 2)));
        verify(itemsListRepository, times(1)).save(any());
        verify(itemsListRepository, times(0)).findAll();
    }

    @Test
    public void testUpdateItemsListWhenTheItemsListDoesNotExist(){
        ItemsList response = getDefaultItemsList2();
        response.setItems(getItemsForItemsList2());
        String newName = "New name";
        ItemsList itemsListToCompare = getDefaultItemsList2();
        itemsListToCompare.setItems(getItemsForItemsList2());
        itemsListToCompare.setName(newName);

        when(itemsListRepository.findById(response.getId())).thenReturn(Optional.empty());
        NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> itemsListService.updateItemsList(response.getId(), newName));

        assertEquals("The entity with id '"+response.getId()+"' could not be found", notFoundException.getMessage());
        verify(itemsListRepository, times(1)).findById(any());
        verify(itemsListRepository, times(1)).findById(argThat(x -> x.equals(response.getId())));
        verify(itemsListRepository, times(0)).save(any());
        verify(itemsListRepository, times(0)).findAll();
    }

    @Test
    public void testDeleteItemsList(){
        ItemsList response = getDefaultItemsList2();
        response.setItems(getItemsForItemsList2());
        ItemsList itemsListToCompare = getDefaultItemsList2();
        itemsListToCompare.setItems(getItemsForItemsList2());

        when(itemsListRepository.findById(response.getId())).thenReturn(Optional.of(response));
        itemsListService.deleteItemsList(response.getId());

        verify(itemsListRepository, times(1)).findById(any());
        verify(itemsListRepository, times(1)).findById(argThat(x -> x.equals(response.getId())));
        verify(itemsListRepository, times(1)).delete(argThat(new ItemsListMatcher(itemsListToCompare, 2)));
        verify(itemsListRepository, times(1)).delete(any());
        verify(itemsListRepository, times(0)).findAll();
    }

    @Test
    public void testDeleteItemsListWhenTheItemsListDoesNotExist(){
        ItemsList response = getDefaultItemsList2();
        response.setItems(getItemsForItemsList2());

        when(itemsListRepository.findById(response.getId())).thenReturn(Optional.empty());
        NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> itemsListService.deleteItemsList(response.getId()));

        assertEquals("The entity with id '"+response.getId()+"' could not be found", notFoundException.getMessage());
        verify(itemsListRepository, times(1)).findById(any());
        verify(itemsListRepository, times(1)).findById(argThat(x -> x.equals(response.getId())));
        verify(itemsListRepository, times(0)).delete(any());
        verify(itemsListRepository, times(0)).findAll();

    }

    @Test
    public void testGetItemsListById(){
        ItemsList response = getDefaultItemsList2();
        response.setItems(getItemsForItemsList2());
        ItemsList itemsListToCompare = getDefaultItemsList2();
        itemsListToCompare.setItems(getItemsForItemsList2());

        when(itemsListRepository.findById(response.getId())).thenReturn(Optional.of(response));
        ItemsList itemsListFound = itemsListService.getItemsListById(response.getId());

        assertEquals(itemsListToCompare.getId(), itemsListFound.getId());
        assertEquals(itemsListToCompare.getName(), itemsListFound.getName());
        assertEquals(1, itemsListFound.getItems().size());
        assertEquals(itemsListToCompare.getItems().get(0).getName(), itemsListFound.getItems().get(0).getName());
        assertEquals(itemsListToCompare.getItems().get(0).getComment(), itemsListFound.getItems().get(0).getComment());
        assertEquals(itemsListToCompare.getItems().get(0).isBought(), itemsListFound.getItems().get(0).isBought());
        assertEquals(itemsListToCompare.getItems().get(0).getItemsList().getId(), itemsListFound.getItems().get(0).getItemsList().getId());
        assertEquals(itemsListToCompare.getItems().get(0).getItemsList().getName(), itemsListFound.getItems().get(0).getItemsList().getName());
        assertEquals(itemsListToCompare.getItems().get(0).getItemsList().getItems(), itemsListFound.getItems().get(0).getItemsList().getItems());
        verify(itemsListRepository, times(1)).findById(any());
        verify(itemsListRepository, times(1)).findById(argThat(x -> x.equals(response.getId())));
        verify(itemsListRepository, times(0)).delete(any());
        verify(itemsListRepository, times(0)).save(any());
        verify(itemsListRepository, times(0)).findAll();
    }

    @Test
    public void testGetItemsListByIdWhenTheItemsListDoesNotExist(){
        ItemsList response = getDefaultItemsList2();
        response.setItems(getItemsForItemsList2());

        when(itemsListRepository.findById(response.getId())).thenReturn(Optional.empty());
        NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> itemsListService.getItemsListById(response.getId()));

        assertEquals("The entity with id '"+response.getId()+"' could not be found", notFoundException.getMessage());
        verify(itemsListRepository, times(1)).findById(any());
        verify(itemsListRepository, times(1)).findById(argThat(x -> x.equals(response.getId())));
        verify(itemsListRepository, times(0)).delete(any());
        verify(itemsListRepository, times(0)).save(any());
        verify(itemsListRepository, times(0)).findAll();
    }

    @Test
    public void testFindAllList(){
        ArrayList<ItemsList> response = new ArrayList<>();
        ItemsList itemsList1 = getDefaultItemsList1();
        itemsList1.setItems(getItemsForItemsList1());
        ItemsList itemsList2 = getDefaultItemsList2();
        itemsList2.setItems(getItemsForItemsList2());
        response.add(itemsList1);
        response.add(itemsList2);
        ArrayList<ItemsList> itemsListToCompare = new ArrayList<>();
        ItemsList itemsList1ToCompare = getDefaultItemsList1();
        itemsList1ToCompare.setItems(getItemsForItemsList1());
        ItemsList itemsList2ToCompare = getDefaultItemsList2();
        itemsList2ToCompare.setItems(getItemsForItemsList2());
        itemsListToCompare.add(itemsList1ToCompare);
        itemsListToCompare.add(itemsList2ToCompare);

        when(itemsListRepository.findAll()).thenReturn(response);
        ArrayList<ItemsList> itemsLists = (ArrayList<ItemsList>) itemsListService.findAllLists();

        for(int j = 0; j < itemsLists.size(); j++){
            assertEquals(itemsListToCompare.get(j).getItems().size(), itemsLists.get(j).getItems().size());
            for (int i = 0; i < itemsLists.get(j).getItems().size(); i++){
                assertEquals(itemsListToCompare.get(j).getItems().get(i).getId(), itemsLists.get(j).getItems().get(i).getId());
                assertEquals(itemsListToCompare.get(j).getItems().get(i).getName(), itemsLists.get(j).getItems().get(i).getName());
                assertEquals(itemsListToCompare.get(j).getItems().get(i).getComment(), itemsLists.get(j).getItems().get(i).getComment());
                assertEquals(itemsListToCompare.get(j).getItems().get(i).getItemsList().getId(), itemsLists.get(j).getItems().get(i).getItemsList().getId());
            }
        }
        verify(itemsListRepository, times(1)).findAll();
        verify(itemsListRepository, times(0)).findById(any());
        verify(itemsListRepository, times(0)).delete(any());
        verify(itemsListRepository, times(0)).save(any());
    }
}
