package com.company.filehandlingapp.model;

import com.company.filehandlingapp.model.file.StringFileOperation;
import com.company.filehandlingapp.shared.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StringFileOperationTest extends SupportShareTest {

    private final StringFileOperation sfo = new StringFileOperation();
    private static final String PATH_TO_TEMP_DIRECTORY = "src/test/resources/temp";

    @AfterAll
    public static void tearDown() {
        FileUtils.removeFilesFromDirectory(PATH_TO_TEMP_DIRECTORY);
    }

    @Test
    public void readShouldReturnEmptyListWhenPathIsNull() {
        //When
        List<String> result = sfo.read(null);
        //Then
        assertEquals(0, result.size());
    }

    @Test
    public void readShouldReturnEmptyListWhenPathIsEmpty() {
        //When
        List<String> result = sfo.read("");
        //Then
        assertEquals(0, result.size());
    }

    @Test
    public void readShouldReturnEmptyListWhenFileIsEmpty() {
        //When
        List<String> result = sfo.read("src/test/resources/test_files/empty.csv");
        //Then
        assertEquals(0, result.size());
    }

    @Test
    public void readShouldReturnEmptyListWhenPathIsIncorrect() {
        //When
        List<String> result = sfo.read("src/test/resources/test_files/notExists.csv");
        //Then
        assertEquals(0, result.size());
    }

    @Test
    public void readShouldReturnNotEmptyListWhenPathIsCorrect() {
        //When
        List<String> resultForItemXFile = sfo.read("src/test/resources/test_files/ItemX.csv");
        List<String> resultForItemYFile = sfo.read("src/test/resources/test_files/ItemY.csv");
        //Then
        assertEquals(8, resultForItemXFile.size());
        assertIterableEquals(refItemOperationForItemXFile, resultForItemXFile);
        assertEquals(8, resultForItemYFile.size());
        assertIterableEquals(refItemOperationForItemYFile, resultForItemYFile);
    }

    @Test
    public void writeShouldReturnFalseWhenPathIsNull() {
        //Then
        assertFalse(sfo.write(null, refItemOperationForItemXFile));
    }

    @Test
    public void writeShouldReturnFalseWhenPathIsEmpty() {
        //Then
        assertFalse(sfo.write("", refItemOperationForItemYFile));
    }

    @Test
    public void writeShouldReturnFalseWhenContentIsNull() {
        //Then
        assertFalse(sfo.write("src/test/resources/test_files/ItemX.csv", null));
    }

    @Test
    public void writeShouldReturnFalseWhenContentIsEmpty() {
        //Given
        List<String> emptyList = new ArrayList<>();
        //Then
        assertFalse(sfo.write("src/test/resources/test_files/ItemY.csv", emptyList));
    }

    @Test
    public void writeShouldReturnTrueWhenPathIsCorrectAndContentIsNotNull() {
        //Then
        assertTrue(sfo.write("src/test/resources/temp/savedItemX.csv", refItemOperationForItemXFile));
        assertTrue(sfo.write("src/test/resources/temp/savedItemY.csv", refItemOperationForItemYFile));
    }

}
