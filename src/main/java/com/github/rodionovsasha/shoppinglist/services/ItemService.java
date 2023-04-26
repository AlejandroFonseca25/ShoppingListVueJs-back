package com.github.rodionovsasha.shoppinglist.services;


import com.github.rodionovsasha.shoppinglist.entities.Item;

public interface ItemService {
    long addItem(long listId, Item item);
    void updateItem(long id, long listId, Item item);
    void deleteItem(long id);
    Item getItemById(long id);
    void toggleBoughtStatus(long id);
}
