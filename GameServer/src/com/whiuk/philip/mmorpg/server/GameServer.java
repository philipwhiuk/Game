package com.whiuk.philip.mmorpg.server;

import java.util.Properties;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whiuk.philip.mmorpg.server.alarms.AlarmsService;
import com.whiuk.philip.mmorpg.server.hibernate.HibernateUtils;
import com.whiuk.philip.mmorpg.server.network.NetworkService;

/**
 * @author Philip
 */
@Service
public class GameServer {
    /**
	 *
	 */
    private ServerWatchdog watchdog;
    /**
     *
     */
    @Autowired
    private NetworkService networkService;
    /**
    *
    */
    @Autowired
    private MessageHandlerService messageHandlerService;
    /**
     *
     */
    @Autowired
    private AlarmsService alarmsService;

    /**
     * @author Philip
     */
    public static class GameServerProperties {
        /**
         *
         */
        private static final int PORT = 8443;
        /**
         *
         */
        private int port;

        /**
         *
         */
        public GameServerProperties() {

        }

        /**
         * @param prop
         *            Key-value properties
         */
        public GameServerProperties(final Properties prop) {
            if (prop.containsKey("port")) {
                this.port = Integer.parseInt(prop.getProperty("port"));
            }
        }
    }

    /**
     *
     */
    private GameServerProperties properties;

    /**
	 *
     */
    public GameServer() {
    }

    /**
     *
     */
    @PostConstruct
    public final void init() {
        new Thread(messageHandlerService).start();
        // Just to ensure Hibernate is running
        HibernateUtils.getSessionFactory();
    }

    /**
     * @param gsProp
     */
    public final void setProperties(final GameServerProperties gsProp) {
        this.properties = gsProp;
    }
}