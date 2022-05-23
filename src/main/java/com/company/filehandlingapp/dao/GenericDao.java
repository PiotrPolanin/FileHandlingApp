package com.company.filehandlingapp.dao;

import java.io.Serializable;
import java.util.List;

public interface GenericDao<T, ID extends Serializable> {

    T findById(ID identification);

    List<T> findAll();

    T save(T entity);

    void deleteById(ID identification);

    void delete(T entity);

}
