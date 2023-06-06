package com.github.rodionovsasha.shoppinglist.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.rodionovsasha.shoppinglist.dto.ItemsListDto;
import com.github.rodionovsasha.shoppinglist.entities.ItemsList;
import com.github.rodionovsasha.shoppinglist.repositories.ItemsListRepository;
import lombok.SneakyThrows;
import lombok.val;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@RunWith(SpringRunner.class)
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

    @Before
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
        List<ItemsListDto.GetAllListsResponse> itemsList = objectMapper.readValue(response.getContentAsString(),new TypeReference<List<ItemsListDto.GetAllListsResponse>>() {});
        assertEquals(itemsList.size(), 1);
        assertEquals(1, itemsList.get(0).getId());
        assertEquals("Shopping List Test 1", itemsList.get(0).getName());
    }

    @Test
    @SneakyThrows
    public void testTheSchemaOfGetAllItemsList() {
        val response = mockMvc.perform(get("/api/v1/")).andReturn().getResponse();

        val jsonSchema = new JSONObject(new JSONTokener(Objects.requireNonNull(ItemListRestControllerTest.class.getResourceAsStream("/get-all-shopping-list.json"))));
        val jsonArray = new JSONArray(response.getContentAsString());

        val schema = SchemaLoader.load(jsonSchema);
        schema.validate(jsonArray);
    }

    @Test
    @SneakyThrows
    public void testGetAnItemsListById() {
        val response = mockMvc.perform(get("/api/v1/itemsList/1")).andReturn().getResponse();

        assertThat(response.getStatus(), equalTo(HttpStatus.OK.value()));
        assertThat(response.getContentType(), equalTo(MediaType.APPLICATION_JSON_UTF8.toString()));
        ItemsListDto.GetResponse itemsList = objectMapper.readValue(response.getContentAsString(),new TypeReference<ItemsListDto.GetResponse>() {});
        assertEquals(1, itemsList.getId());
        assertEquals("Shopping List Test 1", itemsList.getName());
    }

    @Test
    @SneakyThrows
    public void testTheSchemaOfGetAnItemsListById() {
        val response = mockMvc.perform(get("/api/v1/itemsList/1")).andReturn().getResponse();

        val jsonSchema = new JSONObject(new JSONTokener(Objects.requireNonNull(ItemListRestControllerTest.class.getResourceAsStream("/get-by-id-shopping-list.json"))));
        val jsonObject = new JSONObject(response.getContentAsString());

        val schema = SchemaLoader.load(jsonSchema);
        schema.validate(jsonObject);
    }

    @Test
    @SneakyThrows
    public void testGetAnItemsListByIdWhenTheIdIsBlank() {
        val response = mockMvc.perform(get("/api/v1/itemsList/ ")).andReturn().getResponse();

        assertThat(response.getStatus(), equalTo(HttpStatus.BAD_REQUEST.value()));
        assertNull(response.getContentType());
    }
    @Test
    @SneakyThrows
    public void testGetAnItemsListByIdWhenTheIdIsALetter() {
        val response = mockMvc.perform(get("/api/v1/itemsList/x")).andReturn().getResponse();

        assertThat(response.getStatus(), equalTo(HttpStatus.BAD_REQUEST.value()));
        assertEquals("", response.getContentAsString());
        assertNull(response.getContentType());
    }

    @Test
    @SneakyThrows
    public void testGetAnItemsListByIdWhenTheItemsListDoesNotExist() {
        val response = mockMvc.perform(get("/api/v1/itemsList/2")).andReturn().getResponse();

        assertThat(response.getStatus(), equalTo(HttpStatus.NOT_FOUND.value()));
        assertEquals("", response.getContentAsString());
        assertNull(response.getContentType());
    }

    @Test
    @SneakyThrows
    public void testSaveAnItemsList() {
        ItemsListDto itemsList = new ItemsListDto();
        itemsList.setName("Shopping List");
        String body = objectMapper.writeValueAsString(itemsList);
        val response = mockMvc.perform(post("/api/v1/itemsList").contentType(MediaType.APPLICATION_JSON_UTF8).content(body)).andExpect(status().isCreated()).andReturn().getResponse();
        LinkedHashMap createResponse = objectMapper.readValue(response.getContentAsString(), LinkedHashMap.class);
        assertEquals(2, createResponse.get("id"));
    }

    @Test
    @SneakyThrows
    public void testSaveAnItemsListWhenTheRequestIsBad() {
        ItemsListDto itemsList = new ItemsListDto();
        itemsList.setName("");
        String body = objectMapper.writeValueAsString(itemsList);
        val response = mockMvc.perform(post("/api/v1/itemsList").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(body)).andExpect(status().isBadRequest()).andReturn().getResponse();
        LinkedHashMap createResponse = objectMapper.readValue(response.getContentAsString(), LinkedHashMap.class);
        assertEquals("must not be blank", createResponse.get("name"));
        assertThat(response.getContentType(), equalTo(MediaType.APPLICATION_JSON_UTF8.toString()));
    }

    @Test
    @SneakyThrows
    public void testUpdateAnItemsList() {
        ItemsListDto itemsList = new ItemsListDto();
        itemsList.setName("Shopping List Updated");
        String body = objectMapper.writeValueAsString(itemsList);
        mockMvc.perform(put("/api/v1/itemsList/1").contentType(MediaType.APPLICATION_JSON_UTF8).content(body)).andExpect(status().isNoContent());
    }

    @Test
    @SneakyThrows
    public void testUpdateAnItemsListWhenTheRequestIsBad() {
        ItemsListDto itemsList = new ItemsListDto();
        itemsList.setName("");
        String body = objectMapper.writeValueAsString(itemsList);
        val response1 = mockMvc.perform(put("/api/v1/itemsList/1").contentType(MediaType.APPLICATION_JSON_UTF8).content(body)).andExpect(status().isBadRequest()).andReturn().getResponse();
        val response2 = mockMvc.perform(put("/api/v1/itemsList/a").contentType(MediaType.APPLICATION_JSON_UTF8).content(body)).andExpect(status().isBadRequest()).andReturn().getResponse();
        LinkedHashMap createResponse = objectMapper.readValue(response1.getContentAsString(), LinkedHashMap.class);
        assertEquals("must not be blank", createResponse.get("name"));
        assertThat(response1.getContentType(), equalTo(MediaType.APPLICATION_JSON_UTF8.toString()));
        assertEquals("", response2.getContentAsString());
        assertNull(response2.getContentType());
    }

    @Test
    @SneakyThrows
    public void testUpdateAnItemsListWhenTheItemsListDoesNotExist() {
        ItemsListDto itemsList = new ItemsListDto();
        itemsList.setName("Shopping List Updated");
        String body = objectMapper.writeValueAsString(itemsList);
        val response = mockMvc.perform(put("/api/v1/itemsList/999").contentType(MediaType.APPLICATION_JSON_UTF8).content(body)).andExpect(status().isNotFound()).andReturn().getResponse();
        assertEquals("", response.getContentAsString());
        assertNull(response.getContentType());
    }

    @Test
    @SneakyThrows
    public void testDeleteAnItemsList() {
        mockMvc.perform(delete("/api/v1/itemsList/1")).andExpect(status().isNoContent());
    }

    @Test
    @SneakyThrows
    public void testDeleteAnItemsListWhenTheRequestIsBad() {
        val response = mockMvc.perform(delete("/api/v1/itemsList/a")).andExpect(status().isBadRequest()).andReturn().getResponse();
        assertEquals("", response.getContentAsString());
        assertNull(response.getContentType());
    }

    @Test
    @SneakyThrows
    public void testDeleteAnItemsListWhenTheItemsListDoesNotExist() {
        val response = mockMvc.perform(delete("/api/v1/itemsList/999")).andExpect(status().isNotFound()).andReturn().getResponse();
        assertEquals("", response.getContentAsString());
        assertNull(response.getContentType());
    }
}
