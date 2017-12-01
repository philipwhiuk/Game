package com.whiuk.philip.mmorpg.server;


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
	 * The watchdog for the game server.
	 */
    private ServerWatchdog watchdog;
    /**
     * Network connection service.
     */
    @Autowired
    private NetworkService networkService;
    /**
    * Message handling service.
    */
    @Autowired
    private MessageHandlerService messageHandlerService;
    /**
     * Alarms service.
     */
    @Autowired
    private AlarmsService alarmsService;

    /**
     *
     */
    @Autowired
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
     * @return properties
     */
    public final GameServerProperties getProperties() {
        return this.properties;
    }
}
