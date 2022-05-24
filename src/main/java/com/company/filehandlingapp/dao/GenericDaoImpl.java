package com.company.filehandlingapp.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class GenericDaoImpl<T, ID extends Serializable> implements GenericDao<T, ID> {

    protected final SessionFactory sessionFactory;
    protected final Class<T> entityClass;
    protected final Logger LOGGER = LoggerFactory.getLogger(GenericDaoImpl.class);

    public GenericDaoImpl(Class<T> entityClass, SessionFactory sessionFactory) {
        this.entityClass = entityClass;
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<T> findById(ID identification) {
        T entity = getInTransaction(session -> session.find(entityClass, identification));
        return Optional.ofNullable(entity);
    }

    @Override
    public List<T> findAll() {
        Session session = sessionFactory.openSession();
        CriteriaQuery<T> cq = session.getCriteriaBuilder().createQuery(entityClass);
        cq.select(cq.from(entityClass));
        List<T> entities = session.createQuery(cq).getResultList();
        session.close();
        return entities;
    }

    @Override
    public T save(T entity) {
        return getInTransaction(session -> (T) session.merge(entity));
    }

    @Override
    public void deleteById(ID identification) {
        T entity = findById(identification).orElseThrow(() -> new RuntimeException(String.format("Entity %s with id %s not found", entityClass.getName(), identification)));
        runInTransaction(session -> session.remove(entity));
    }

    @Override
    public void delete(T entity) {
        runInTransaction(session -> session.remove(entity));
    }

    private void runInTransaction(Consumer<Session> action) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            action.accept(session);
            transaction.commit();
        } catch (RuntimeException rex) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            LOGGER.error(rex.getMessage(), rex);
        } finally {
            session.close();
        }
    }

    private T getInTransaction(Function<Session, T> action) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        T result = null;
        try {
            result = action.apply(session);
            transaction.commit();
        } catch (RuntimeException rex) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            LOGGER.error(rex.getMessage(), rex);
        } finally {
            session.close();
        }
        return result;
    }
}
