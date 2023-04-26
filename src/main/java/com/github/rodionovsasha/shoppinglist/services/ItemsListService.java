package com.github.rodionovsasha.shoppinglist.services;


import com.github.rodionovsasha.shoppinglist.entities.ItemsList;

public interface ItemsListService {
    long addItemsList(ItemsList itemsList);
    void updateItemsList(long id, String name);
    void deleteItemsList(long id);
    ItemsList getItemsListById(long id);
    Iterable<ItemsList> findAllLists();
}
