package com.github.rodionovsasha.shoppinglist.contract.api.itemsList;

import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import au.com.dius.pact.provider.junitsupport.loader.PactBrokerAuth;
import au.com.dius.pact.provider.spring.junit5.MockMvcTestTarget;
import com.github.rodionovsasha.shoppinglist.controllers.ItemsListRestController;
import com.github.rodionovsasha.shoppinglist.dto.ItemDto;
import com.github.rodionovsasha.shoppinglist.dto.ItemsListDto;
import com.github.rodionovsasha.shoppinglist.entities.Item;
import com.github.rodionovsasha.shoppinglist.entities.ItemsList;
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
public class ItemsListTest {

    @Mock
    private ItemsListService itemsListService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ItemsListRestController itemsListRestController;

    @BeforeEach
    public void changeContext(PactVerificationContext context) {
        System.setProperty("pact.verifier.publishResults", "true");
        MockMvcTestTarget testTarget = new MockMvcTestTarget();
        testTarget.setControllers(itemsListRestController);
        context.setTarget(testTarget);
    }

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void pactVerificationTestTemplate(PactVerificationContext context) {
        context.verifyInteraction();
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
