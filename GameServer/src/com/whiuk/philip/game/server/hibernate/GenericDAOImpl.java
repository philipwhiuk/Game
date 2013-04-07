package com.whiuk.philip.game.server.hibernate;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

/**
 * @author Philip Whitehouse
 * @param <T>
 * @param <ID>
 */
public abstract class GenericDAOImpl<T, ID extends Serializable> implements
        GenericDAO<T, ID> {

    /**
     * @return
     */
    protected Session getSession() {
        return HibernateUtils.getSession();
    }

    @Override
    public final void save(final T entity) {
        Session hibernateSession = this.getSession();
        hibernateSession.saveOrUpdate(entity);
    }

    @Override
    public final void merge(final T entity) {
        Session hibernateSession = this.getSession();
        hibernateSession.merge(entity);
    }

    @Override
    public final void delete(final T entity) {
        Session hibernateSession = this.getSession();
        hibernateSession.delete(entity);
    }

    @SuppressWarnings("unchecked")
    // Hibernate
    @Override
    public final List<T> findMany(final Query query) {
        List<T> t;
        t = (List<T>) query.list();
        return t;
    }

    @SuppressWarnings("unchecked")
    // Hibernate
    @Override
    public final T findOne(final Query query) {
        T t;
        t = (T) query.uniqueResult();
        return t;
    }

    @SuppressWarnings("unchecked")
    // Hibernate
    @Override
    public final T findByID(Class clazz, Long id) {
        Session hibernateSession = this.getSession();
        T t = null;
        t = (T) hibernateSession.get(clazz, id);
        return t;
    }

    @Override
    public final List findAll(Class clazz) {
        Session hibernateSession = this.getSession();
        List t = null;
        Query query = hibernateSession.createQuery("from " + clazz.getName());
        t = query.list();
        return t;
    }
}
