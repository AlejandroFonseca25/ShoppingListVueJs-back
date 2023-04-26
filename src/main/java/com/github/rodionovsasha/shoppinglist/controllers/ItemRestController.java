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

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@Api(tags = "Item")
@RestController
@RequestMapping(value = "/api/v1/item", produces = APPLICATION_JSON_VALUE)
public class ItemRestController {
    @Autowired
    private ItemService itemService;
    @Autowired
    private ModelMapper modelMapper;

    @ApiOperation(value = "Get item", response = ItemDto.GetResponse.class)
    @ApiResponses({
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Item not found")
    })
    @GetMapping("/{id}")
    public ItemDto.GetResponse getItem(@PathVariable long id) {
        return modelMapper.map(itemService.getItemById(id), ItemDto.GetResponse.class);
    }

    @ApiOperation(value = "Add item", response = ItemDto.CreateResponse.class)
    @ApiResponses(@ApiResponse(code = 400, message = "Bad request"))
    @PostMapping @ResponseStatus(CREATED)
    public ItemDto.CreateResponse addItem(@Valid @RequestBody ItemDto request) {
        Item item = modelMapper.map(request, Item.class);
        long listId = request.getListId();
        long id = itemService.addItem(listId, item);
        return new ItemDto.CreateResponse(id, listId);
    }

    @ApiOperation("Update item")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Item not found")
    })
    @PutMapping("/{id}") @ResponseStatus(NO_CONTENT)
    public void updateItem(@PathVariable long id, @Valid @RequestBody ItemDto request) {
        Item item = modelMapper.map(request, Item.class);
        itemService.updateItem(id, request.getListId(), item);
    }

    @ApiOperation(value = "Mark item as bought / not bought")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Item not found")
    })
    @PatchMapping("/{id}/buy")
    public void toggleItemBoughtStatus(@PathVariable("id") long id) {
        itemService.toggleBoughtStatus(id);
    }

    @ApiOperation("Delete item")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 404, message = "Item not found")
    })
    @DeleteMapping("/{id}") @ResponseStatus(NO_CONTENT)
    public void deleteItem(@PathVariable long id) {
        itemService.deleteItem(id);
    }
}
