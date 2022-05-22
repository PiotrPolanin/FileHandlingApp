package com.company.filehandlingapp.model.item;

public class ItemOperationReport {

    private final Integer supplySum;
    private final Integer buySum;
    private final Integer sumDiff;

    public ItemOperationReport(Integer supplySum, Integer buySum, Integer sumDiff) {
        this.supplySum = supplySum;
        this.buySum = buySum;
        this.sumDiff = sumDiff;
    }

    public Integer getSupplySum() {
        return supplySum;
    }

    public Integer getBuySum() {
        return buySum;
    }

    public Integer getSumDiff() {
        return sumDiff;
    }

    @Override
    public String toString() {
        return "Item Operation Report [" +
                "supplySum: " + supplySum +
                ", buySum: " + buySum +
                ", sumDiff: " + sumDiff +
                ']';
    }
}
