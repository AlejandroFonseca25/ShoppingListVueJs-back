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
public class CreateItemServiceTest {
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
        testItem.setId(0);
        testItem.setComment("I need 2kg for my juice");
        testItem.setBought(false);
        testItem.setItemsList(testList);
    }

    
    @Test
    @DisplayName("Successful created of an item")
    public void itemUpdateTest() {

        Mockito.when(itemRepository.save(any(Item.class))).thenReturn(testItem);
        Mockito.when(itemRepository.findById((long) 0)).thenReturn(Optional.of(testItem));


        // Action
        long savedItem = itemService.addItem((long)0,testItem);
        Item getItemSaved = itemService.getItemById(savedItem);

        // Assert
        assertNotNull(getItemSaved);
        assertThat(getItemSaved.getId(), equalTo(testItem.getId()));
        assertThat(getItemSaved.getName(), equalTo(testItem.getName()));
        assertThat(getItemSaved.getComment(), equalTo(testItem.getComment()));
        assertThat(getItemSaved.isBought(), equalTo(testItem.isBought()));
        assertThat(getItemSaved.getItemsList(), equalTo(testItem.getItemsList()));
    }
}
