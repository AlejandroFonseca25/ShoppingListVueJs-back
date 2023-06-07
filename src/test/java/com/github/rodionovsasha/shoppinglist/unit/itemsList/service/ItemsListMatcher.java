package com.github.rodionovsasha.shoppinglist.unit.itemsList.service;

import com.github.rodionovsasha.shoppinglist.entities.ItemsList;
import lombok.AllArgsConstructor;
import org.mockito.ArgumentMatcher;

import java.util.Objects;

@AllArgsConstructor
public class ItemsListMatcher implements ArgumentMatcher<ItemsList> {

    ItemsList itemsListRight;
    int action;

    @Override
    public boolean matches(ItemsList itemsListLeft) {
        boolean correctId;
        switch (action){
            case 1: //create
                correctId = itemsListLeft.getId() == 0;
                break;
            case 2: //update or delete
                correctId = itemsListLeft.getId() == itemsListRight.getId();
                break;
            default:
                correctId = false;
        }
        return correctId &&
                Objects.equals(itemsListLeft.getName(), itemsListRight.getName()) &&
                Objects.equals(itemsListLeft.getItems().get(0).getId(), itemsListRight.getItems().get(0).getId()) &&
                Objects.equals(itemsListLeft.getItems().get(0).getName(), itemsListRight.getItems().get(0).getName()) &&
                Objects.equals(itemsListLeft.getItems().get(0).getComment(), itemsListRight.getItems().get(0).getComment()) &&
                Objects.equals(itemsListLeft.getItems().get(0).isBought(), itemsListRight.getItems().get(0).isBought()) &&
                Objects.equals(itemsListLeft.getItems().get(0).getItemsList().getId(), itemsListRight.getItems().get(0).getItemsList().getId()) &&
                Objects.equals(itemsListLeft.getItems().get(0).getItemsList().getName(), itemsListRight.getItems().get(0).getItemsList().getName()) &&
                Objects.equals(itemsListLeft.getItems().get(0).getItemsList().getItems(), itemsListRight.getItems().get(0).getItemsList().getItems());
    }
}
