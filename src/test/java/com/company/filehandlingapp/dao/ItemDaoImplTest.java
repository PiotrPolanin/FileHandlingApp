package com.company.filehandlingapp.dao;

import com.company.filehandlingapp.model.item.Item;
import com.company.filehandlingapp.model.item.ItemOperation;
import com.company.filehandlingapp.model.item.ItemOperationType;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class ItemDaoImplTest extends PersistenceSetupTest {

    private final ItemDao itemDao = new ItemDaoImpl(sf);

    @Test
    public void shouldFindByIdReturnItemsFromDB() {
        assertTrue(itemDao.findById(1L).isPresent());
        assertEquals(1L, itemDao.findById(1L).get().getId());
        assertEquals("ItemX", itemDao.findById(1L).get().getName());
        assertTrue(itemDao.findById(2L).isPresent());
        assertEquals(2L, itemDao.findById(2L).get().getId());
        assertEquals("ItemY", itemDao.findById(2L).get().getName());
        assertTrue(itemDao.findById(3L).isPresent());
        assertEquals(3L, itemDao.findById(3L).get().getId());
        assertEquals("ItemZ", itemDao.findById(3L).get().getName());
    }

    @Test
    public void shouldFindByIdReturnEmptyOptionalWhenItemWithIdNotExists() {
        assertTrue(itemDao.findById(4L).isEmpty());
    }

    @Test
    public void shouldFindByIdReturnEmptyOptionalWhenIdIsNull() {
        assertTrue(itemDao.findById(null).isEmpty());
    }

    @Test
    public void shouldFindAllReturnItemsFromDB() {
        //When
        List<Item> dbItems = itemDao.findAll();
        //Then
        assertFalse(dbItems.isEmpty());
        assertEquals(3, dbItems.size());
        assertEquals(1L, dbItems.get(0).getId());
        assertEquals(2L, dbItems.get(1).getId());
        assertEquals(3L, dbItems.get(2).getId());
    }

    @Test
    public void shouldSavePersistNotNullItem() {
        //Given
        Item item = new Item("itemA");
        //When
        Item savedItem = itemDao.save(item);
        //Then
        assertEquals(4, savedItem.getId());
        assertEquals(item.getName(), savedItem.getName());
        assertEquals(item.getOperations(), savedItem.getOperations());
    }

    @Test
    public void shouldSavePersistItemWithNotNullOperations() {
        //Given
        Item item = new Item("item");
        item.addOperation(new ItemOperation(ItemOperationType.SUPPLY, 10));
        item.addOperation(new ItemOperation(ItemOperationType.BUY, 10));
        item.addOperation(new ItemOperation(ItemOperationType.SUPPLY, 20));
        item.addOperation(new ItemOperation(ItemOperationType.SUPPLY, 10));
        //When
        Item savedItem = itemDao.save(item);
        //Then
        assertEquals(4, savedItem.getId());
        assertEquals(item.getName(), savedItem.getName());
        assertEquals(item.getOperations().size(), savedItem.getOperations().size());
    }

    @Test
    public void shouldDeleteByIdRemoveItemWithSpecificId() {
        //When
        itemDao.deleteById(2L);
        List<Item> dbItems = itemDao.findAll();
        //Then
        assertEquals(2, dbItems.size());
        assertFalse(dbItems.stream().map(i -> i.getId()).collect(Collectors.toList()).contains(2L));
    }

    @Test
    public void shouldDeleteThrowRuntimeExceptionWhenItemWithIdNotExists() {
        RuntimeException re = assertThrows(RuntimeException.class, () -> itemDao.deleteById(10L));
        assertEquals("Entity com.company.filehandlingapp.model.item.Item with id 10 not found", re.getMessage());
    }

    @Test
    public void shouldDeleteRemoveItemFromDB() {
        //Given
        Item itemX = itemDao.findById(1L).get();
        //When
        itemDao.delete(itemX);
        List<Item> dbItems = itemDao.findAll();
        //Then
        assertEquals(2, dbItems.size());
        assertFalse(dbItems.contains(itemX));
    }

}
