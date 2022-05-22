package com.company.filehandlingapp.model;

import com.company.filehandlingapp.model.item.ItemOperationReport;
import com.company.filehandlingapp.model.item.ItemOperationReportGenerator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ItemOperationReportGeneratorTest {

    private final ItemOperationReportGenerator defaultReportGenerator = new ItemOperationReportGenerator();

    @Test
    public void shouldGenerateReturnEmptyListWhenReportParameterIsNull() {
        //When
        List<String> emptyReportToExport = defaultReportGenerator.generate(null);
        //Then
        assertEquals(0, emptyReportToExport.size());
    }

    @Test
    public void shouldGenerateReturnNotEmptyListWithDefaultLabelsWhenReportParameterIsCorrect() {
        //Given
        ItemOperationReport operationReport = new ItemOperationReport(35, 17, 18);
        //When
        List<String> operationReportToExport = defaultReportGenerator.generate(operationReport);
        //Then
        assertNotNull(operationReportToExport);
        assertEquals(3, operationReportToExport.size());
        assertEquals("supply,35", operationReportToExport.get(0));
        assertEquals("buy,17", operationReportToExport.get(1));
        assertEquals("result,18", operationReportToExport.get(2));
    }
}
