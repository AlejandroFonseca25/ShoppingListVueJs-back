package com.github.rodionovsasha.shoppinglist.unit.utils;

import com.github.rodionovsasha.shoppinglist.entities.Item;
import com.github.rodionovsasha.shoppinglist.entities.ItemsList;

import java.util.LinkedList;
import java.util.List;

public class ModelBuilder {
    public static Item getDefaultItem1(){
        return Item.builder()
                .id(1)
                .name("Item 1")
                .comment("comment for item 1")
                .isBought(false)
                .itemsList(getDefaultItemsList1())
                .build();
    }

    public static Item getDefaultItem2(){
        return Item.builder()
                .id(1)
                .name("Item 2")
                .comment("comment for item 2")
                .isBought(false)
                .itemsList(getDefaultItemsList2())
                .build();
    }

    public static Item getDefaultItem(){
        return Item.builder()
                .id(1)
                .name("Item 1")
                .comment("comment for item 1")
                .isBought(false)
                .itemsList(getDefaultItemsList1())
                .build();
    }

    public static List<Item> getItemsForItemsList1(){
        List<Item> items = new LinkedList<>();
        items.add(getDefaultItem1());
        Item item = getDefaultItem2();
        item.setItemsList(getDefaultItemsList1());
        items.add(item);
        return items;
    }

    public static List<Item> getItemsForItemsList2(){
        List<Item> items = new LinkedList<>();
        items.add(getDefaultItem2());
        return items;
    }

    public static ItemsList getDefaultItemsList1(){
        return ItemsList.builder()
                .id(1)
                .name("Shopping list 1")
                .items(null)
                .build();
    }

    public static ItemsList getDefaultItemsList2(){
        return ItemsList.builder()
                .id(2)
                .name("Shopping list 2")
                .items(null)
                .build();
    }
}
