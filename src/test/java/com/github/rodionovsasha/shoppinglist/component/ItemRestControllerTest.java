package com.github.rodionovsasha.shoppinglist.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.rodionovsasha.shoppinglist.dto.ItemDto;
import com.github.rodionovsasha.shoppinglist.dto.ItemsListDto;
import com.github.rodionovsasha.shoppinglist.entities.Item;
import com.github.rodionovsasha.shoppinglist.entities.ItemsList;
import com.github.rodionovsasha.shoppinglist.repositories.ItemRepository;
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
public class ItemRestControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ItemsListRepository itemsListRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    ObjectMapper objectMapper;

    @Before
    public void setUp() {
        val shoppingList = new ItemsList("Shopping List Test 1");
        itemsListRepository.save(shoppingList);
        val item = new Item("Orange 2kg");
        itemRepository.save(item);
    }

    @Test
    @SneakyThrows
    public void testGetAnItemById() {
        val response = mockMvc.perform(get("/api/v1/item/1")).andReturn().getResponse();

        assertThat(response.getStatus(), equalTo(HttpStatus.OK.value()));
        assertThat(response.getContentType(), equalTo(MediaType.APPLICATION_JSON_UTF8.toString()));
        ItemDto.GetResponse items = objectMapper.readValue(response.getContentAsString(), new TypeReference<ItemDto.GetResponse>() {
        });
        assertEquals(1, items.getId());
        assertEquals("Orange 2kg", items.getName());
    }

    @Test
    @SneakyThrows
    public void testGetAnItemsByIdWhenTheIdIsALetter() {
        val response = mockMvc.perform(get("/api/v1/item/a")).andReturn().getResponse();

        assertThat(response.getStatus(), equalTo(HttpStatus.BAD_REQUEST.value()));
        assertEquals("", response.getContentAsString());
        assertNull(response.getContentType());
    }

    @Test
    @SneakyThrows
    public void testGetAnItemsListByIdWhenTheItemsListDoesNotExist() {
        val response = mockMvc.perform(get("/api/v1/item/999")).andReturn().getResponse();

        assertThat(response.getStatus(), equalTo(HttpStatus.NOT_FOUND.value()));
        assertEquals("", response.getContentAsString());
        assertNull(response.getContentType());
    }

    @Test
    @SneakyThrows
    public void testDeleteAnItem() {
        mockMvc.perform(delete("/api/v1/item/1")).andExpect(status().isNoContent());
    }

    @Test
    @SneakyThrows
    public void testDeleteAnItemsWhenTheRequestIsBad() {
        val response = mockMvc.perform(delete("/api/v1/item/a")).andExpect(status().isBadRequest()).andReturn().getResponse();
        assertEquals("", response.getContentAsString());
        assertNull(response.getContentType());
    }

    @Test
    @SneakyThrows
    public void testDeleteAnItemsWhenTheItemsListDoesNotExist() {
        val response = mockMvc.perform(delete("/api/v1/item/999")).andExpect(status().isNotFound()).andReturn().getResponse();
        assertEquals("", response.getContentAsString());
        assertNull(response.getContentType());
    }

    @Test
    @SneakyThrows
    public void testSaveAnItem() {
        ItemDto item = new ItemDto();
        item.setBought(true);
        item.setComment("Magnificent cream cheese");
        item.setListId(1L);
        item.setName("Cheese");
        String body = objectMapper.writeValueAsString(item);
        val response = mockMvc.perform(post("/api/v1/item").contentType(MediaType.APPLICATION_JSON_UTF8).content(body)).andExpect(status().isCreated()).andReturn().getResponse();
        LinkedHashMap createResponse = objectMapper.readValue(response.getContentAsString(), LinkedHashMap.class);
        assertEquals(2, createResponse.get("id"));
        assertEquals(1, createResponse.get("listId"));
    }

    @Test
    @SneakyThrows
    public void testSaveAnItemWithoutName() {
        ItemDto item = new ItemDto();
        item.setBought(true);
        item.setComment("Magnificent cream cheese");
        item.setListId(1L);
        String body = objectMapper.writeValueAsString(item);
        val response = mockMvc.perform(post("/api/v1/item").contentType(MediaType.APPLICATION_JSON_UTF8).content(body)).andExpect(status().isBadRequest()).andReturn().getResponse();
        LinkedHashMap createResponse = objectMapper.readValue(response.getContentAsString(), LinkedHashMap.class);
        assertEquals("must not be blank", createResponse.get("name"));
        assertThat(response.getContentType(), equalTo(MediaType.APPLICATION_JSON_UTF8.toString()));
    }

    @Test
    @SneakyThrows
    public void testSaveAnItemWithoutListId() {
        ItemDto item = new ItemDto();
        item.setBought(true);
        item.setComment("Magnificent cream cheese");
        item.setName("Cheese");
        String body = objectMapper.writeValueAsString(item);
        val response = mockMvc.perform(post("/api/v1/item").contentType(MediaType.APPLICATION_JSON_UTF8).content(body)).andExpect(status().isBadRequest()).andReturn().getResponse();
        LinkedHashMap createResponse = objectMapper.readValue(response.getContentAsString(), LinkedHashMap.class);
        assertEquals("must not be null", createResponse.get("listId"));
        assertThat(response.getContentType(), equalTo(MediaType.APPLICATION_JSON_UTF8.toString()));
    }

    @Test
    @SneakyThrows
    public void testSaveAnItemWithANonExistentList() {
        ItemDto item = new ItemDto();
        item.setBought(true);
        item.setComment("Magnificent cream cheese");
        item.setListId(999L);
        item.setName("Cheese");
        String body = objectMapper.writeValueAsString(item);
        mockMvc.perform(post("/api/v1/item").contentType(MediaType.APPLICATION_JSON_UTF8).content(body)).andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    public void testUpdateAnItem() {
        ItemsListDto itemsList = new ItemsListDto();
        itemsList.setName("Temporal Shopping List");
        String listBody = objectMapper.writeValueAsString(itemsList);
        mockMvc.perform(post("/api/v1/itemsList").contentType(MediaType.APPLICATION_JSON_UTF8).content(listBody));

        ItemDto item = new ItemDto();
        item.setBought(false);
        item.setComment("Special goat milk");
        item.setName("Milk");
        item.setListId(2L);
        String body = objectMapper.writeValueAsString(item);
        mockMvc.perform(put("/api/v1/item/1").contentType(MediaType.APPLICATION_JSON_UTF8).content(body)).andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    public void testUpdateANonExistentItem() {
        ItemsListDto itemsList = new ItemsListDto();
        itemsList.setName("Temporal Shopping List");
        String listBody = objectMapper.writeValueAsString(itemsList);
        mockMvc.perform(post("/api/v1/itemsList").contentType(MediaType.APPLICATION_JSON_UTF8).content(listBody));

        ItemDto item = new ItemDto();
        item.setBought(false);
        item.setComment("Special goat milk");
        item.setName("Milk");
        item.setListId(2L);
        String body = objectMapper.writeValueAsString(item);
        mockMvc.perform(put("/api/v1/item/999").contentType(MediaType.APPLICATION_JSON_UTF8).content(body)).andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    public void testUpdateAnItemWithEmptyName() {
        ItemsListDto itemsList = new ItemsListDto();
        itemsList.setName("Temporal Shopping List");
        String listBody = objectMapper.writeValueAsString(itemsList);
        mockMvc.perform(post("/api/v1/itemsList").contentType(MediaType.APPLICATION_JSON_UTF8).content(listBody));

        ItemDto item = new ItemDto();
        item.setBought(false);
        item.setComment("Special goat milk");
        item.setListId(2L);
        String body = objectMapper.writeValueAsString(item);
        val response = mockMvc.perform(put("/api/v1/item/1").contentType(MediaType.APPLICATION_JSON_UTF8).content(body)).andExpect(status().isBadRequest()).andReturn().getResponse();
        LinkedHashMap createResponse = objectMapper.readValue(response.getContentAsString(), LinkedHashMap.class);
        assertEquals("must not be blank", createResponse.get("name"));
        assertThat(response.getContentType(), equalTo(MediaType.APPLICATION_JSON_UTF8.toString()));
    }

    @Test
    @SneakyThrows
    public void testUpdateAnItemWithANonExistentList() {
        ItemDto item = new ItemDto();
        item.setBought(false);
        item.setComment("Special goat milk");
        item.setListId(999L);
        item.setName("Milk");
        String body = objectMapper.writeValueAsString(item);
        mockMvc.perform(put("/api/v1/item/1").contentType(MediaType.APPLICATION_JSON_UTF8).content(body)).andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    public void testUpdateAnItemWithANullList() {
        ItemDto item = new ItemDto();
        item.setBought(false);
        item.setComment("Special goat milk");
        item.setName("Milk");
        String body = objectMapper.writeValueAsString(item);
        val response = mockMvc.perform(put("/api/v1/item/1").contentType(MediaType.APPLICATION_JSON_UTF8).content(body)).andExpect(status().isBadRequest()).andReturn().getResponse();
        LinkedHashMap createResponse = objectMapper.readValue(response.getContentAsString(), LinkedHashMap.class);
        assertEquals("must not be null", createResponse.get("listId"));
        assertThat(response.getContentType(), equalTo(MediaType.APPLICATION_JSON_UTF8.toString()));
    }
}