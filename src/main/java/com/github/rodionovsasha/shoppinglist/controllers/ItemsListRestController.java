package com.github.rodionovsasha.shoppinglist.controllers;

import com.github.rodionovsasha.shoppinglist.dto.ItemsListDto;
import com.github.rodionovsasha.shoppinglist.entities.ItemsList;
import com.github.rodionovsasha.shoppinglist.services.ItemsListService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.Type;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@Api(tags = "Items List")
@RestController
@RequestMapping(value = "/api/v1", produces = APPLICATION_JSON_VALUE)
public class ItemsListRestController {
    private static final String ITEMS_LIST_BASE_PATH = "/itemsList";

    @Autowired
    private ItemsListService itemsListService;
    @Autowired
    private ModelMapper modelMapper;

    @ApiOperation(value = "Get all lists",
            response = ItemsListDto.GetAllListsResponse.class,
            responseContainer = "List")
    @GetMapping("/")
    public List<ItemsListDto.GetAllListsResponse> getAllLists() {
        val itemsLists = itemsListService.findAllLists();
        Type listType = getListTokenForDto().getType();
        return modelMapper.map(itemsLists, listType);
    }

    @ApiOperation(value = "Get list", response = ItemsListDto.GetResponse.class)
    @ApiResponses({
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Items list not found")
    })
    @GetMapping(ITEMS_LIST_BASE_PATH + "/{id}")
    public ItemsListDto.GetResponse getItemsList(@PathVariable long id) {
        return modelMapper.map(itemsListService.getItemsListById(id), ItemsListDto.GetResponse.class);
    }

    @ApiOperation(value = "Add list", response = ItemsListDto.CreateResponse.class)
    @ApiResponses(@ApiResponse(code = 400, message = "Bad request"))
    @PostMapping(ITEMS_LIST_BASE_PATH)
    @ResponseStatus(CREATED)
    public ItemsListDto.CreateResponse saveItemsList(@Valid @RequestBody ItemsListDto request) {
        ItemsList itemsList = modelMapper.map(request, ItemsList.class);
        long id = itemsListService.addItemsList(itemsList);
        return new ItemsListDto.CreateResponse(id);
    }

    @ApiOperation("Update list")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Items list not found")
    })
    @PutMapping(ITEMS_LIST_BASE_PATH + "/{id}")
    @ResponseStatus(NO_CONTENT)
    public void updateItemsList(@PathVariable long id, @Valid @RequestBody ItemsListDto request) {
        itemsListService.updateItemsList(id, request.getName());
    }

    @ApiOperation("Delete list")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Items list not found")
    })
    @DeleteMapping(ITEMS_LIST_BASE_PATH + "/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteItemsList(@PathVariable long id) {
        itemsListService.deleteItemsList(id);
    }

    private static TypeToken<List<ItemsListDto.GetAllListsResponse>> getListTokenForDto() {
        return new TypeToken<List<ItemsListDto.GetAllListsResponse>>() { };
    }
}
