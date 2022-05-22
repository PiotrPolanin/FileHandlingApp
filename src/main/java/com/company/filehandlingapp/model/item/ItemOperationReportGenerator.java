package com.company.filehandlingapp.model.item;

import java.util.ArrayList;
import java.util.List;

public class ItemOperationReportGenerator implements OperationGenerator<ItemOperationReport> {

    private String supplyLabel = "supply";
    private String buyLabel = "buy";
    private String resultLabel = "result";
    private String defaultDelimiter = ",";

    public ItemOperationReportGenerator(String supplyLabel, String buyLabel, String resultLabel, String delimiter) {
        this.supplyLabel = supplyLabel;
        this.buyLabel = buyLabel;
        this.resultLabel = resultLabel;
        this.defaultDelimiter = delimiter;
    }

    public ItemOperationReportGenerator() {
    }

    @Override
    public List<String> generate(ItemOperationReport report) {
        List<String> result = new ArrayList<>();
        if (report != null) {
            result.add(String.join(defaultDelimiter, supplyLabel, report.getSupplySum().toString()));
            result.add(String.join(defaultDelimiter, buyLabel, report.getBuySum().toString()));
            result.add(String.join(defaultDelimiter, resultLabel, report.getSumDiff().toString()));
        }
        return result;
    }

}
