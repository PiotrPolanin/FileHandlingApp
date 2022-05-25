package com.company.filehandlingapp.model;

import com.company.filehandlingapp.model.item.ItemOperation;
import com.company.filehandlingapp.model.item.ItemOperationConverter;
import com.company.filehandlingapp.model.item.ItemOperationType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ItemOperationConverterTest extends SupportShareTest {

    private final ItemOperationConverter ioc = new ItemOperationConverter();

    @Test
    public void shouldConvertReturnEmptyListWhenListParameterIsNull() {
        //When
        List<ItemOperation> result = ioc.convert(null, ",");
        //Then
        assertEquals(0, result.size());
    }

    @Test
    public void shouldConvertReturnEmptyListWhenListParameterIsEmptyToo() {
        //Given
        ArrayList<String> empty = new ArrayList<>();
        //When
        List<ItemOperation> result = ioc.convert(empty, ",");
        //Then
        assertEquals(0, result.size());
    }

    @Test
    public void shouldConvertReturnEmptyListWhenDelimiterParameterIsNull() {
        //When
        List<ItemOperation> result = ioc.convert(refItemOperationForItemXFile, null);
        //Then
        assertEquals(0, result.size());
    }

    @Test
    public void shouldConvertThrowIllegalArgumentExceptionWhenDelimiterParameterIsDifferentThanThatOneIncludedInListParameter() {
        //Then
        IllegalArgumentException illegalArgumentExceptionForSemicolon = assertThrows(IllegalArgumentException.class, () -> ioc.convert(refItemOperationForItemXFile, ";"));
        assertEquals("Conversion failure: no such delimiter ';' at input index 0", illegalArgumentExceptionForSemicolon.getMessage());
        IllegalArgumentException illegalArgumentExceptionForSpace = assertThrows(IllegalArgumentException.class, () -> ioc.convert(refItemOperationForItemXFile, " "));
        assertEquals("Conversion failure: no such delimiter ' ' at input index 0", illegalArgumentExceptionForSpace.getMessage());
        IllegalArgumentException illegalArgumentExceptionForEmptyString = assertThrows(IllegalArgumentException.class, () -> ioc.convert(refItemOperationForItemXFile, ""));
        assertEquals("Conversion failure: no such delimiter '' at input index 0", illegalArgumentExceptionForEmptyString.getMessage());
    }

    @Test
    public void shouldConvertReturnNotEmptyListWhenParametersAreCorrect() {
        //When
        List<ItemOperation> resultForItemXFile = ioc.convert(refItemOperationForItemXFile, ",");
        List<ItemOperation> resultForItemYFile = ioc.convert(refItemOperationForItemYFile, ",");
        //Then
        assertEquals(8, resultForItemXFile.size());
        assertEquals(resultForItemXFile.get(0).getOperationType(), ItemOperationType.SUPPLY);
        assertEquals(resultForItemXFile.get(0).getAmount(), Integer.valueOf(123));
        assertEquals(resultForItemXFile.get(1).getOperationType(), ItemOperationType.BUY);
        assertEquals(resultForItemXFile.get(1).getAmount(), Integer.valueOf(17));
        assertEquals(resultForItemXFile.get(2).getOperationType(), ItemOperationType.BUY);
        assertEquals(resultForItemXFile.get(2).getAmount(), Integer.valueOf(5));
        assertEquals(resultForItemXFile.get(3).getOperationType(), ItemOperationType.BUY);
        assertEquals(resultForItemXFile.get(3).getAmount(), Integer.valueOf(28));
        assertEquals(resultForItemXFile.get(4).getOperationType(), ItemOperationType.SUPPLY);
        assertEquals(resultForItemXFile.get(4).getAmount(), Integer.valueOf(30));
        assertEquals(resultForItemXFile.get(5).getOperationType(), ItemOperationType.BUY);
        assertEquals(resultForItemXFile.get(5).getAmount(), Integer.valueOf(38));
        assertEquals(resultForItemXFile.get(6).getOperationType(), ItemOperationType.SUPPLY);
        assertEquals(resultForItemXFile.get(6).getAmount(), Integer.valueOf(19));
        assertEquals(resultForItemXFile.get(7).getOperationType(), ItemOperationType.BUY);
        assertEquals(resultForItemXFile.get(7).getAmount(), Integer.valueOf(24));

        assertEquals(8, resultForItemYFile.size());
        assertEquals(resultForItemYFile.get(0).getOperationType(), ItemOperationType.SUPPLY);
        assertEquals(resultForItemYFile.get(0).getAmount(), Integer.valueOf(256));
        assertEquals(resultForItemYFile.get(1).getOperationType(), ItemOperationType.BUY);
        assertEquals(resultForItemYFile.get(1).getAmount(), Integer.valueOf(24));
        assertEquals(resultForItemYFile.get(2).getOperationType(), ItemOperationType.BUY);
        assertEquals(resultForItemYFile.get(2).getAmount(), Integer.valueOf(134));
        assertEquals(resultForItemYFile.get(3).getOperationType(), ItemOperationType.BUY);
        assertEquals(resultForItemYFile.get(3).getAmount(), Integer.valueOf(56));
        assertEquals(resultForItemYFile.get(4).getOperationType(), ItemOperationType.SUPPLY);
        assertEquals(resultForItemYFile.get(4).getAmount(), Integer.valueOf(139));
        assertEquals(resultForItemYFile.get(5).getOperationType(), ItemOperationType.BUY);
        assertEquals(resultForItemYFile.get(5).getAmount(), Integer.valueOf(44));
        assertEquals(resultForItemYFile.get(6).getOperationType(), ItemOperationType.SUPPLY);
        assertEquals(resultForItemYFile.get(6).getAmount(), Integer.valueOf(40));
        assertEquals(resultForItemYFile.get(7).getOperationType(), ItemOperationType.BUY);
        assertEquals(resultForItemYFile.get(7).getAmount(), Integer.valueOf(21));
    }

    @Test
    public void shouldConvertReturnReducedListWhenSomeElementsOfParameterListAreNotValid() {
        //Given
        List<String> someInvalidData = new LinkedList<>();
        someInvalidData.add("supply,58");
        someInvalidData.add("b,a");
        someInvalidData.add(" , ");
        someInvalidData.add("buy,16");
        someInvalidData.add("spply,5");
        someInvalidData.add("by,10");
        someInvalidData.add("buy,@");
        //When
        List<ItemOperation> convertedResults = ioc.convert(someInvalidData, ",");
        //Then
        assertEquals(2, convertedResults.size());
        assertEquals("supply", convertedResults.get(0).getOperationType().getValue());
        assertEquals(58, convertedResults.get(0).getAmount().intValue());
        assertEquals("buy", convertedResults.get(1).getOperationType().getValue());
        assertEquals(16, convertedResults.get(1).getAmount().intValue());
    }

    @Test
    public void shouldConvertReturnCorrectResultWhenSomeElementsOfParameterListAreNegativeOrUpperLowerCase() {
        //Given
        List<String> values = new LinkedList<>();
        values.add("suPPly , 78");
        values.add("buY , -23");
        values.add("SUPPLY , -3");
        values.add(" bUy , 44");
        values.add("SUPPLY , -5");
        values.add(" supply   , 8 ");
        //When
        List<ItemOperation> convertedResults = ioc.convert(values, ",");
        //Then
        assertEquals(6, convertedResults.size());
        assertEquals("supply", convertedResults.get(0).getOperationType().getValue());
        assertEquals(78, convertedResults.get(0).getAmount().intValue());
        assertEquals("buy", convertedResults.get(1).getOperationType().getValue());
        assertEquals(23, convertedResults.get(1).getAmount().intValue());
        assertEquals("supply", convertedResults.get(2).getOperationType().getValue());
        assertEquals(3, convertedResults.get(2).getAmount().intValue());
        assertEquals("buy", convertedResults.get(3).getOperationType().getValue());
        assertEquals(44, convertedResults.get(3).getAmount().intValue());
        assertEquals("supply", convertedResults.get(4).getOperationType().getValue());
        assertEquals(5, convertedResults.get(4).getAmount().intValue());
        assertEquals("supply", convertedResults.get(5).getOperationType().getValue());
        assertEquals(8, convertedResults.get(5).getAmount().intValue());
    }

}
