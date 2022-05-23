package com.company.filehandlingapp.dao;

import com.company.filehandlingapp.model.item.Item;
import org.hibernate.SessionFactory;

public class ItemDaoImpl extends GenericDaoImpl<Item, Long> implements ItemDao {

    public ItemDaoImpl(SessionFactory sessionFactory) {
        super(Item.class, sessionFactory);
    }

}
