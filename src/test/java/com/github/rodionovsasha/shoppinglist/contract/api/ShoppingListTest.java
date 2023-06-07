package com.github.rodionovsasha.shoppinglist.contract.api;

import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import au.com.dius.pact.provider.junitsupport.loader.PactBrokerAuth;
import au.com.dius.pact.provider.spring.junit5.MockMvcTestTarget;
import com.github.rodionovsasha.shoppinglist.controllers.ItemRestController;
import com.github.rodionovsasha.shoppinglist.controllers.ItemsListRestController;
import com.github.rodionovsasha.shoppinglist.dto.ItemDto;
import com.github.rodionovsasha.shoppinglist.dto.ItemsListDto;
import com.github.rodionovsasha.shoppinglist.entities.Item;
import com.github.rodionovsasha.shoppinglist.entities.ItemsList;
import com.github.rodionovsasha.shoppinglist.services.ItemService;
import com.github.rodionovsasha.shoppinglist.services.ItemsListService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import java.lang.reflect.Type;
import java.util.Collections;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Provider("ShoppingListBack")
@PactBroker(
        url = "${PACT_BROKER_BASE_URL}",
        authentication = @PactBrokerAuth(token = "${PACT_BROKER_TOKEN}"))
@ExtendWith(MockitoExtension.class)
public class ShoppingListTest {

    @Mock
    private ItemService itemService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ItemRestController itemRestController;

    @Mock
    private ItemsListService itemsListService;

    @InjectMocks
    private ItemsListRestController itemsListRestController;

    @BeforeEach
    public void changeContext(PactVerificationContext context) {
        System.setProperty("pact.verifier.publishResults", "true");
        MockMvcTestTarget testTarget = new MockMvcTestTarget();
        testTarget.setControllers(itemRestController,itemsListRestController);
        context.setTarget(testTarget);
    }

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void pactVerificationTestTemplate(PactVerificationContext context) {
        context.verifyInteraction();
    }

    @State("there are no items")
    public void addItem() {
        ItemsList list = new ItemsList("Shopping list");
        list.setId(1);

        Item item = new Item();
        item.setId(1);
        item.setName("Cheese");
        item.setComment("Fresh parmesan cheese");
        item.setItemsList(list);

        when(modelMapper.map(any(ItemDto.class),any())).thenReturn(item);
        when(itemService.addItem(anyLong(),any(Item.class))).thenReturn(1L);
    }

    @State("has items to delete")
    public void deleteItem() {
        doNothing().when(itemService).deleteItem(anyLong());
    }

    @State("has item to get")
    public void getItem() {
        ItemsList listItem;
        listItem = new ItemsList("List for Breakfast");
        listItem.setId(1);
        Item item;
        item = new Item("Orange 2k");
        item.setId(1);
        item.setComment("For juice");
        item.setBought(true);
        item.setItemsList(listItem);
        ItemDto.GetResponse itemDto = new ItemDto.GetResponse();
        itemDto.setId(1);
        itemDto.setName("Orange 2k");
        itemDto.setComment("For juice");
        itemDto.setBought(true);
        itemDto.setListId(1);
        when(itemService.getItemById(any(long.class))).thenReturn(item);
        when(modelMapper.map(any(Item.class),any())).thenReturn(itemDto);
    }

    @State("has item to update")
    public void updateItem() {
        ItemsList listItem;
        listItem = new ItemsList("List for Brekfast");
        listItem.setId(1);
        Item item;
        item = new Item("Orange 2k");
        item.setId(1);
        item.setComment("For juice");
        item.setBought(true);
        item.setItemsList(listItem);
        when(itemService.updateItem(any(long.class),any(long.class),any())).thenReturn(item);
    }

    @State("has lists to get")
    public void getAllLists() {
        ItemsList itemsList = new ItemsList("Test list");
        itemsList.setId(1L);
        when(itemsListService.findAllLists()).thenReturn(Collections.singletonList(itemsList));
        when(modelMapper.map(anyIterable(), any(Type.class))).thenReturn(Collections.singletonList(itemsList));
    }

    @State("there are no lists")
    public void saveItemsList() {
        when(modelMapper.map(any(ItemsListDto.class), any())).thenReturn(new ItemsList("Test list"));
        when(itemsListService.addItemsList(any(ItemsList.class))).thenReturn(1L);
    }

    @State("has list to get")
    public void getItemsList() {
        Item item = new Item("Orange");
        item.setId(2L);
        item.setComment("For juice");
        item.setBought(true);
        ItemsList itemsList = new ItemsList();
        itemsList.setId(1L);
        itemsList.setName("Test list");
        itemsList.setItems(Collections.singletonList(item));
        ItemDto.GetInListResponse itemDto = new ItemDto.GetInListResponse();
        itemDto.setId(2L);
        itemDto.setName("Orange");
        itemDto.setComment("For juice");
        itemDto.setBought(true);
        ItemsListDto.GetResponse getResponse = new ItemsListDto.GetResponse();
        getResponse.setId(1L);
        getResponse.setName("Test list");
        getResponse.setItems(Collections.singletonList(itemDto));
        when(itemsListService.getItemsListById(anyLong())).thenReturn(itemsList);
        when(modelMapper.map(any(ItemsList.class), any())).thenReturn(getResponse);
    }

    @State("has list to delete")
    public void deleteItemsList() {
        doNothing().when(itemsListService).deleteItemsList(anyLong());
    }

    @State("has list to update")
    public void updateItemsList() {
        doNothing().when(itemsListService).updateItemsList(anyLong(), anyString());
    }
}
