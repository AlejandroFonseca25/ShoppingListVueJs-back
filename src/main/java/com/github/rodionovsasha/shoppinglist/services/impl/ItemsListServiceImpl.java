package com.github.rodionovsasha.shoppinglist.services.impl;

import com.github.rodionovsasha.shoppinglist.entities.ItemsList;
import com.github.rodionovsasha.shoppinglist.exceptions.NotFoundException;
import com.github.rodionovsasha.shoppinglist.repositories.ItemsListRepository;
import com.github.rodionovsasha.shoppinglist.services.ItemsListService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@AllArgsConstructor
@Service
@Transactional
public class ItemsListServiceImpl implements ItemsListService {
    private final ItemsListRepository itemsListRepository;

    @Override
    public long addItemsList(ItemsList itemsList) {
        itemsList.setId(0); // for some reason model mapper returns ItemsList with id == 1
        return itemsListRepository.save(itemsList).getId();
    }

    @Override
    public void updateItemsList(long id, String name) {
        ItemsList itemsList = getItemsListById(id);
        itemsList.setName(name);
        itemsListRepository.save(itemsList);
    }

    @Override
    public void deleteItemsList(long id) {
        itemsListRepository.delete(getItemsListById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public ItemsList getItemsListById(long id) {
        return itemsListRepository.findById(id).orElseThrow(() -> NotFoundException.forId(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<ItemsList> findAllLists() {
        return itemsListRepository.findAll();
    }
}
