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
}
