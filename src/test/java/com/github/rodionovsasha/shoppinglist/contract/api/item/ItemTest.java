package com.github.rodionovsasha.shoppinglist.contract.api.item;

import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import au.com.dius.pact.provider.junitsupport.loader.PactBrokerAuth;
import au.com.dius.pact.provider.spring.junit5.MockMvcTestTarget;
import com.github.rodionovsasha.shoppinglist.controllers.ItemRestController;
import com.github.rodionovsasha.shoppinglist.dto.ItemDto;
import com.github.rodionovsasha.shoppinglist.entities.Item;
import com.github.rodionovsasha.shoppinglist.entities.ItemsList;
import com.github.rodionovsasha.shoppinglist.services.ItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Provider("ShoppingListBack")
@PactBroker(
        url = "${PACT_BROKER_BASE_URL}",
        authentication = @PactBrokerAuth(token = "${PACT_BROKER_TOKEN}"))
@ExtendWith(MockitoExtension.class)
public class ItemTest {

    @Mock
    private ItemService itemService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ItemRestController itemController;

    @BeforeEach
    public void changeContext(PactVerificationContext context) {
        System.setProperty("pact.verifier.publishResults", "true");
        MockMvcTestTarget testTarget = new MockMvcTestTarget();
        testTarget.setControllers(itemController);
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
}
