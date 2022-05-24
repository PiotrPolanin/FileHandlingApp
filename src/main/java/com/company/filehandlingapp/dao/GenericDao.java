package com.company.filehandlingapp.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface GenericDao<T, ID extends Serializable> {

    Optional<T> findById(ID identification);

    List<T> findAll();

    T save(T entity);

    void deleteById(ID identification);

    void delete(T entity);

}
