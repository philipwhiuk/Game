package com.whiuk.philip.game.server.hibernate;

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
     * 
     * @param entity
     */
    void save(T entity);

    /**
     * Merge.
     * 
     * @param entity
     */
    void merge(T entity);

    /**
     * Delete.
     * 
     * @param entity
     */
    void delete(T entity);

    /**
     * Find many.
     * 
     * @param query
     * @return
     */
    List<T> findMany(Query query);

    /**
     * Find one.
     * 
     * @param query
     * @return
     */
    T findOne(Query query);

    /**
     * Find all.
     * 
     * @param clazz
     * @return
     */
    @SuppressWarnings("rawtypes")
    // Generic DAO
    List findAll(Class clazz);

    /**
     * Find by ID.
     * 
     * @param clazz
     * @param id
     * @return
     */
    @SuppressWarnings("rawtypes")
    // Generic DAO
    T findByID(Class clazz, Long id);
}
