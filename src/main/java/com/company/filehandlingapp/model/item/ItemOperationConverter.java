package com.company.filehandlingapp.model.item;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class ItemOperationConverter implements OperationConverter<ItemOperation> {

    @Override
    public List<ItemOperation> convert(List<String> input, String delimiter) {
        List<ItemOperation> result = new LinkedList<>();
        if (input != null && input.size() > 0 && delimiter != null) {
            for (int i = 0; i < input.size(); i++) {
                String[] separatedInput = input.get(i).split(delimiter);
                if (separatedInput.length != 2) {
                    throw new IllegalArgumentException(String.format("Conversion failure: no such delimiter '%s' at input index %s", delimiter, i));
                }
                String operation = separatedInput[0].toLowerCase(Locale.ROOT).trim();
                String amount = separatedInput[1].trim();
                if (validateOperationType(operation) && validateAmount(amount)) {
                    ItemOperation itemOperation = new ItemOperation(ItemOperationType.valueOf(operation.toUpperCase(Locale.ROOT)), Integer.valueOf(amount));
                    result.add(itemOperation);
                }
            }
        }
        return result;
    }

    private boolean validateOperationType(String value) {
        return Arrays.stream(ItemOperationType.values()).anyMatch(iot -> iot.getValue().equals(value));
    }

    private boolean validateAmount(String value) {
        try {
            Integer.valueOf(value);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

}
