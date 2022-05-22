package com.company.filehandlingapp.model.item;

import javax.persistence.*;

@Entity(name = "item_operation")
@Table(schema = "items_management", name = "operations")
public class ItemOperation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "type", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ItemOperationType operationType;
    @Column(name = "amount", nullable = false)
    private Integer amount;

    public ItemOperation(ItemOperationType operationType, Integer amount) {
        this.operationType = operationType;
        this.amount = amount;
    }

    public ItemOperation() {
    }

    public Long getId() {
        return id;
    }

    public ItemOperationType getOperationType() {
        return operationType;
    }

    public Integer getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Item Operation [" +
                "id: " + id +
                ", operation type: " + operationType +
                ", amount: " + amount +
                ']';
    }
}
