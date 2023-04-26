package com.github.rodionovsasha.shoppinglist.repositories;

import com.github.rodionovsasha.shoppinglist.entities.Item;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ItemRepository extends CrudRepository<Item, Long> {
}
