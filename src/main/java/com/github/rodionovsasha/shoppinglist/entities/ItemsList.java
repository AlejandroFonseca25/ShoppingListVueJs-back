package com.github.rodionovsasha.shoppinglist.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;


@Entity
@Getter @Setter
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class ItemsList {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;

    public ItemsList(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "itemsList", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("IS_BOUGHT, NAME")
    private List<Item> items = new LinkedList<>();
}
