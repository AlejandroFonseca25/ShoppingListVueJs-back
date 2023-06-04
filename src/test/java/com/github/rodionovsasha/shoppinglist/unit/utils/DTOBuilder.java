package com.github.rodionovsasha.shoppinglist.unit.utils;

import com.github.rodionovsasha.shoppinglist.dto.ItemsListDto;

public class DTOBuilder {
    public static ItemsListDto getDefaultItemsList1Dto(){
        return ItemsListDto.builder()
                .name("Shopping list 1")
                .build();
    }
}
