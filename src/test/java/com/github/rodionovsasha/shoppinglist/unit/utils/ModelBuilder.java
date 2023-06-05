package com.github.rodionovsasha.shoppinglist.unit.utils;

import com.github.rodionovsasha.shoppinglist.entities.Item;
import com.github.rodionovsasha.shoppinglist.entities.ItemsList;

import java.util.LinkedList;
import java.util.List;

public class ModelBuilder {
    public static Item getDefaultItem1(){
        Item item = new Item("Item 1");
        item.setId(1);
        item.setComment("comment for item 1");
        item.setBought(false);
        item.setItemsList(getDefaultItemsList1());
        return item;
    }

    public static Item getDefaultItem2(){
        Item item = new Item("Item 2");
        item.setId(2);
        item.setComment("comment for item 2");
        item.setBought(false);
        item.setItemsList(getDefaultItemsList2());
        return item;
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
        ItemsList itemsList = new ItemsList("Shopping list 1");
        itemsList.setId(1);
        return itemsList;
    }

    public static ItemsList getDefaultItemsList2(){
        ItemsList itemsList = new ItemsList("Shopping list 2");
        itemsList.setId(2);
        return itemsList;
    }
}
