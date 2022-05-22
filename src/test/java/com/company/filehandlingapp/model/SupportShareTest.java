package com.company.filehandlingapp.model;

import com.company.filehandlingapp.model.item.ItemOperation;
import com.company.filehandlingapp.model.item.ItemOperationType;
import org.junit.jupiter.api.BeforeAll;

import java.util.LinkedList;
import java.util.List;

public abstract class SupportShareTest {

    public static final List<String> refItemOperationForItemXFile = new LinkedList<>();
    public static final List<String> refItemOperationForItemYFile = new LinkedList<>();
    public static final List<ItemOperation> refItemOperationForItemX = new LinkedList<>();
    public static final List<ItemOperation> refItemOperationForItemY = new LinkedList<>();

    @BeforeAll
    public static void addReferenceElements() {
        refItemOperationForItemXFile.add("supply,123");
        refItemOperationForItemXFile.add("buy,17");
        refItemOperationForItemXFile.add("buy,5");
        refItemOperationForItemXFile.add("buy,28");
        refItemOperationForItemXFile.add("supply,30");
        refItemOperationForItemXFile.add("buy,38");
        refItemOperationForItemXFile.add("supply,19");
        refItemOperationForItemXFile.add("buy,24");

        refItemOperationForItemYFile.add("supply,256");
        refItemOperationForItemYFile.add("buy,24");
        refItemOperationForItemYFile.add("buy,134");
        refItemOperationForItemYFile.add("buy,56");
        refItemOperationForItemYFile.add("supply,139");
        refItemOperationForItemYFile.add("buy,44");
        refItemOperationForItemYFile.add("supply,40");
        refItemOperationForItemYFile.add("buy,21");

        refItemOperationForItemX.add(new ItemOperation(ItemOperationType.SUPPLY, 123));
        refItemOperationForItemX.add(new ItemOperation(ItemOperationType.BUY, 17));
        refItemOperationForItemX.add(new ItemOperation(ItemOperationType.BUY, 5));
        refItemOperationForItemX.add(new ItemOperation(ItemOperationType.BUY, 28));
        refItemOperationForItemX.add(new ItemOperation(ItemOperationType.SUPPLY, 30));
        refItemOperationForItemX.add(new ItemOperation(ItemOperationType.BUY, 38));
        refItemOperationForItemX.add(new ItemOperation(ItemOperationType.SUPPLY, 19));
        refItemOperationForItemX.add(new ItemOperation(ItemOperationType.BUY, 24));

        refItemOperationForItemY.add(new ItemOperation(ItemOperationType.SUPPLY, 256));
        refItemOperationForItemY.add(new ItemOperation(ItemOperationType.BUY, 24));
        refItemOperationForItemY.add(new ItemOperation(ItemOperationType.BUY, 134));
        refItemOperationForItemY.add(new ItemOperation(ItemOperationType.BUY, 56));
        refItemOperationForItemY.add(new ItemOperation(ItemOperationType.SUPPLY, 139));
        refItemOperationForItemY.add(new ItemOperation(ItemOperationType.BUY, 44));
        refItemOperationForItemY.add(new ItemOperation(ItemOperationType.SUPPLY, 40));
        refItemOperationForItemY.add(new ItemOperation(ItemOperationType.BUY, 21));
    }

}
