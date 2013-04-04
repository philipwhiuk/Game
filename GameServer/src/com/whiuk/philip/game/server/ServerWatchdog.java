package com.whiuk.philip.game.server;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.whiuk.philip.util.watchdog.Watchdog;
import com.whiuk.philip.util.watchdog.WatchedThread;

/**
 * @author Philip
 *
 */
public class ServerWatchdog {

    /**
     *
     */
    private static final long THREAD_WARN_TIME = 1000;

    /**
     *
     */
    private static final String THREAD_EXCEEDED_WARNING_TIME =
        "Watched thread '%s' exceeded warning time, last notified %sms ago.";

    /**
     *
     */
    public transient Logger logger;
    
    private List<Watchdog> watchdogs = new ArrayList<Watchdog>();
    
    public void run() {
        for (Watchdog watchdog: watchdogs) {
            long timeSincePat = System.currentTimeMillis() - watchdog.lastPat();
            if (timeSincePat > watchdog.getWarningTime()) {
                String message = String.format(THREAD_EXCEEDED_WARNING_TIME,
                        watchdog.getThread(), timeSincePat);
                logger.log(Level.WARNING, message);
                watchdog.restartThread();
            }
        }
    }
}
