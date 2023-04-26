package com.github.rodionovsasha.shoppinglist.repositories;

import com.github.rodionovsasha.shoppinglist.entities.ItemsList;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ItemsListRepository extends CrudRepository<ItemsList, Long> {
}
