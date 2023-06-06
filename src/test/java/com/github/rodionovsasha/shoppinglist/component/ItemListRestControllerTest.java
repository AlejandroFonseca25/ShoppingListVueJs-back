package com.github.rodionovsasha.shoppinglist.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.rodionovsasha.shoppinglist.entities.ItemsList;
import com.github.rodionovsasha.shoppinglist.repositories.ItemsListRepository;
import lombok.SneakyThrows;
import lombok.val;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = { "spring.config.additional-location=classpath:component-test.yml"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "test")
public class ItemListRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ItemsListRepository itemsListRepository;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        val ShoppingList1 = new ItemsList("Shopping List Test 1");
        itemsListRepository.save(ShoppingList1);
    }

    @Test
    @SneakyThrows
    public void testGetAllItemsList() {
        val response = mockMvc.perform(get("/api/v1/")).andReturn().getResponse();

        assertThat(response.getStatus(), equalTo(HttpStatus.OK.value()));
        assertThat(response.getContentType(), equalTo(MediaType.APPLICATION_JSON_UTF8.toString()));


    }

    @Test
    @SneakyThrows
    public void testTheSchemaOfGetAllItemsList() {
        val response = mockMvc.perform(get("/api/v1/")).andReturn().getResponse();

        val jsonSchema = new JSONObject(new JSONTokener(Objects.requireNonNull(ItemListRestControllerTest.class.getResourceAsStream("/shopping-list.json"))));
        val jsonArray = new JSONArray(response.getContentAsString());

        val schema = SchemaLoader.load(jsonSchema);
        schema.validate(jsonArray);
    }

//    @Test
//    @SneakyThrows
//    public void testGetAnItemsListById() {
//
//    }
//
//    @Test
//    @SneakyThrows
//    public void testTheSchemaOfGetAnItemsListById() {
//
//    }
//
//    @Test
//    @SneakyThrows
//    public void testGetAnItemsListByIdWhenTheRequestIsBad() {
//
//    }
//
//    @Test
//    @SneakyThrows
//    public void testGetAnItemsListByIdWhenTheItemsListDoesNotExist() {
//
//    }
//
//    @Test
//    @SneakyThrows
//    public void testSaveAnItemsList() {
//
//    }
//
//    @Test
//    @SneakyThrows
//    public void testSaveAnItemsListWhenTheRequestIsBad() {
//
//    }
//
//    @Test
//    @SneakyThrows
//    public void testUpdateAnItemsList() {
//
//    }
//
//    @Test
//    @SneakyThrows
//    public void testUpdateAnItemsListWhenTheRequestIsBad() {
//
//    }
//
//    @Test
//    @SneakyThrows
//    public void testUpdateAnItemsListWhenTheItemsListDoesNotExist() {
//
//    }
//
//    @Test
//    @SneakyThrows
//    public void testDeleteAnItemsList() {
//
//    }
//
//    @Test
//    @SneakyThrows
//    public void testDeleteAnItemsListWhenTheRequestIsBad() {
//
//    }
//
//    @Test
//    @SneakyThrows
//    public void testDeleteAnItemsListWhenTheItemsListDoesNotExist() {
//
//    }


}
