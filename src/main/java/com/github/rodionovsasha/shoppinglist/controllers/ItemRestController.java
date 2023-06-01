package com.github.rodionovsasha.shoppinglist.controllers;

import com.github.rodionovsasha.shoppinglist.dto.ItemDto;
import com.github.rodionovsasha.shoppinglist.entities.Item;
import com.github.rodionovsasha.shoppinglist.services.ItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@Api(tags = "Item")
@RestController
@RequestMapping(value = "/api/v1", produces = APPLICATION_JSON_VALUE)
public class ItemRestController {
    private static final String ITEM_BASE_PATH = "/item";
    @Autowired
    private ItemService itemService;
    @Autowired
    private ModelMapper modelMapper;

    @ApiOperation(value = "Get item", response = ItemDto.GetResponse.class)
    @ApiResponses({
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Item not found")
    })
    @GetMapping(ITEM_BASE_PATH + "/{id}")
    public ItemDto.GetResponse getItem(@PathVariable long id) {
        return modelMapper.map(itemService.getItemById(id), ItemDto.GetResponse.class);
    }

    @ApiOperation(value = "Add item", response = ItemDto.CreateResponse.class)
    @ApiResponses(@ApiResponse(code = 400, message = "Bad request"))
    @PostMapping(ITEM_BASE_PATH)
    @ResponseStatus(CREATED)
    public ItemDto.CreateResponse addItem(@Valid @RequestBody ItemDto request) {
        Item item = modelMapper.map(request, Item.class);
        System.out.println(item);
        long listId = request.getListId();
        long id = itemService.addItem(listId, item);
        return new ItemDto.CreateResponse(id, listId);
    }

    @ApiOperation("Update item")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Item not found")
    })
    @PutMapping(ITEM_BASE_PATH + "/{id}")
    @ResponseStatus(OK)
    public void updateItem(@PathVariable long id, @Valid @RequestBody ItemDto request) {
        Item item = modelMapper.map(request, Item.class);
        itemService.updateItem(id, request.getListId(), item);
    }

    @ApiOperation(value = "Mark item as bought / not bought")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Item not found")
    })
    @PatchMapping(ITEM_BASE_PATH + "/{id}/buy")
    public void toggleItemBoughtStatus(@PathVariable("id") long id) {
        itemService.toggleBoughtStatus(id);
    }

    @ApiOperation("Delete item")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Item not found")
    })
    @DeleteMapping(ITEM_BASE_PATH + "/{id}") @ResponseStatus(NO_CONTENT)
    public void deleteItem(@PathVariable long id) {
        itemService.deleteItem(id);
    }
}
