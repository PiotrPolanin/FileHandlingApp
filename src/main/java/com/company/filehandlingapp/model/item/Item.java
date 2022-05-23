package com.company.filehandlingapp.model.item;

import javax.persistence.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "item")
@Table(schema = "items_management", name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "item", fetch = FetchType.EAGER)
    @Column(name = "item_operations")
    private final Set<ItemOperation> operations = new HashSet<>();

    public Item(String name) {
        this.name = name;
    }

    public Item() {
    }

    public void addOperation(ItemOperation operation) {
        operations.add(operation);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<ItemOperation> getOperations() {
        return Collections.unmodifiableSet(operations);
    }

    @Override
    public String toString() {
        return "Item [" +
                "id: " + id +
                ", name: " + name +
                ']';
    }

}
