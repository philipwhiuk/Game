package com.whiuk.philip.game.server.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

/**
 * Hibernate utilities
 * @author Philip Whitehouse
 *
 */
public final class HibernateUtils {
	/**
	 * Utility class constructor
	 */
	private HibernateUtils() {
	}

	/**
	 *
	 */
	private static final SessionFactory SESSION_FACTORY;

    static {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
        	Configuration configuration = new Configuration();
        	configuration.configure();

            ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().
            		applySettings(configuration.getProperties())
            		.buildServiceRegistry();
            SESSION_FACTORY = configuration
            		.buildSessionFactory(serviceRegistry);
        } catch (Throwable ex) {
            // Log the exception.
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
    /**
     * @return session factory
     */
    public static SessionFactory getSessionFactory() {
        return SESSION_FACTORY;
    }
    /**
     *
     * @return
     */
    public static Session beginTransaction() {
    	Session hibernateSession = HibernateUtils.getSession();
    	hibernateSession.beginTransaction();
    	return hibernateSession;
	}
	/**
	 *
	 */
	public static void commitTransaction() {
		HibernateUtils.getSession().getTransaction().commit();
	}
	/**
	 *
	 */
	public static void rollbackTransaction() {
		HibernateUtils.getSession().getTransaction().rollback();
	}
	/**
	 *
	 */
	public static void closeSession() {
		HibernateUtils.getSession().close();
	}
	/**
	 *
	 * @return
	 */
	public static Session getSession() {
		Session hibernateSession = SESSION_FACTORY.getCurrentSession();
		return hibernateSession;
	}
}
