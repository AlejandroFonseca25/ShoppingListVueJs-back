package com.github.rodionovsasha.shoppinglist.unit.utils;

import com.github.rodionovsasha.shoppinglist.dto.ItemsListDto;

public class DTOBuilder {
    public static ItemsListDto getDefaultItemsList1Dto(){
        ItemsListDto itemsListDto = new ItemsListDto();
        itemsListDto.setName("Shopping list 1");
        return itemsListDto;
    }
}
