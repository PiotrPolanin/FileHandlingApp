package com.company.filehandlingapp.model.item;

import java.util.List;
import java.util.Optional;

public class ItemOperationReportCalculator {

    public ItemOperationReportCalculator() {
    }

    public Optional<ItemOperationReport> calculate(List<ItemOperation> operations) {
        if (operations != null && operations.size() > 0) {
            Integer supplySum = sum(operations, ItemOperationType.SUPPLY);
            Integer buySum = sum(operations, ItemOperationType.BUY);
            Integer sumDiff = supplySum - buySum;
            return Optional.ofNullable(new ItemOperationReport(supplySum, buySum, sumDiff));
        }
        return Optional.empty();
    }

    private Integer sum(List<ItemOperation> operations, ItemOperationType operationType) {
        return operations.stream().filter(io -> io.getOperationType().equals(operationType)).map(io -> io.getAmount()).reduce(0, Integer::sum);
    }

}
