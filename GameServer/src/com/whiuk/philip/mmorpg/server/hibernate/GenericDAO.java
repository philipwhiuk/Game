package com.whiuk.philip.mmorpg.server.hibernate;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;

/**
 * Generic data access object.
 * 
 * @author Philip Whitehouse
 * @param <T>
 * @param <ID>
 */
public interface GenericDAO<T, ID extends Serializable> {

    /**
     * Save.
     * @param entity Entity
     */
    void save(T entity);

    /**
     * Merge.
     * @param entity Entity
     */
    void merge(T entity);

    /**
     * Delete.
     * @param entity Entity
     */
    void delete(T entity);

    /**
     * Find many.
     * @param query Query
     * @return List of objects
     */
    List<T> findMany(Query query);

    /**
     * Find one.
     * @param query Query
     * @return Object or <code>null</code>
     */
    T findOne(Query query);

    /**
     * Find all.
     * @param clazz Class
     * @return List of items
     */
    @SuppressWarnings("rawtypes")
    // Generic DAO
    List<T> findAll(Class clazz);

    /**
     * Find by ID.
     * @param clazz Class
     * @param id ID
     * @return Object
     */
    @SuppressWarnings("rawtypes")
    // Generic DAO
    T findByID(Class clazz, Long id);
}
