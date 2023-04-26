package com.github.rodionovsasha.shoppinglist.services.impl;

import com.github.rodionovsasha.shoppinglist.entities.Item;
import com.github.rodionovsasha.shoppinglist.exceptions.NotFoundException;
import com.github.rodionovsasha.shoppinglist.repositories.ItemRepository;
import com.github.rodionovsasha.shoppinglist.services.ItemService;
import com.github.rodionovsasha.shoppinglist.services.ItemsListService;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
@Transactional
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final ItemsListService itemsListService;

    @Override
    public long addItem(long listId, Item item) {
        item.setId(0); // for some reason model mapper returns ItemsList with id == 1
        item.setItemsList(itemsListService.getItemsListById(listId));
        return itemRepository.save(item).getId();
    }

    @Override
    public void updateItem(long id, long listId, Item item) {
        Item updatedItem = getItemById(id);
        updatedItem.setName(item.getName());
        updatedItem.setComment(item.getComment());
        updatedItem.setBought(item.isBought());
        updatedItem.setItemsList(itemsListService.getItemsListById(listId));
        itemRepository.save(updatedItem);
    }

    @Override
    public void deleteItem(long id) {
        itemRepository.delete(getItemById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Item getItemById(long id) {
        return itemRepository.findById(id).orElseThrow(() -> NotFoundException.forId(id));
    }

    @Override
    public void toggleBoughtStatus(long id) {
        val item = getItemById(id);
        item.setBought(!item.isBought());
        itemRepository.save(item);
    }
}
