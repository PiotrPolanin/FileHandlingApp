package com.company.filehandlingapp.model;

import com.company.filehandlingapp.model.item.ItemOperation;
import com.company.filehandlingapp.model.item.ItemOperationReport;
import com.company.filehandlingapp.model.item.ItemOperationReportCalculator;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ItemOperationReportCalculatorTest extends SupportShareTest {

    private final ItemOperationReportCalculator reportCalculator = new ItemOperationReportCalculator();

    @Test
    public void shouldCalculateReturnEmptyOptionalWhenListParameterIsNull() {
        //When
        Optional<ItemOperationReport> report = reportCalculator.calculate(null);
        //Then
        assertFalse(report.isPresent());
    }

    @Test
    public void shouldCalculateReturnEmptyOptionalWhenListParameterIsEmpty() {
        //Given
        List<ItemOperation> empty = new ArrayList<>();
        //When
        Optional<ItemOperationReport> report = reportCalculator.calculate(empty);
        //Then
        assertFalse(report.isPresent());
    }

    @Test
    public void shouldCalculateReturnCorrectResultsWhenListParameterHasValidElements() {
        //When
        Optional<ItemOperationReport> reportForItemX = reportCalculator.calculate(refItemOperationForItemX);
        Optional<ItemOperationReport> reportForItemY = reportCalculator.calculate(refItemOperationForItemY);
        //Then
        assertTrue(reportForItemX.isPresent());
        assertEquals(172, reportForItemX.get().getSupplySum());
        assertEquals(112, reportForItemX.get().getBuySum());
        assertEquals(60, reportForItemX.get().getSumDiff());
        assertTrue(reportForItemY.isPresent());
        assertEquals(435, reportForItemY.get().getSupplySum());
        assertEquals(279, reportForItemY.get().getBuySum());
        assertEquals(156, reportForItemY.get().getSumDiff());
    }
}
