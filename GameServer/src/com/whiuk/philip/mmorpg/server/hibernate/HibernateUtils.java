package com.whiuk.philip.mmorpg.server.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

/**
 * Hibernate utilities.
 * 
 * @author Philip Whitehouse
 */
public final class HibernateUtils {
    /**
     * Utility class constructor.
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

            StandardServiceRegistry standardRegistry = 
            	       new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
            Metadata metaData = 
                    new MetadataSources(standardRegistry).getMetadataBuilder().build();
            SESSION_FACTORY = metaData.getSessionFactoryBuilder().build();
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
     * @return Session
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
     * @return session
     */
    public static Session getSession() {
        Session hibernateSession = SESSION_FACTORY.getCurrentSession();
        return hibernateSession;
    }
}
