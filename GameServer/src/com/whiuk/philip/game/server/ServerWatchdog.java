package com.whiuk.philip.game.server;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.log4j.Level;

import com.whiuk.philip.util.watchdog.Watchdog;

/**
 * @author Philip
 *
 */
public class ServerWatchdog implements Runnable {

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
    public static final transient Logger LOGGER =
           Logger.getLogger(ServerWatchdog.class);

    /**
     * List of watchdogs to check.
     */
    private List<Watchdog> watchdogs = new ArrayList<Watchdog>();

    @Override
    public final void run() {
        for (Watchdog watchdog: watchdogs) {
            long timeSincePat = System.currentTimeMillis() - watchdog.lastPat();
            if (timeSincePat > watchdog.getWarningTime()) {
                String message = String.format(THREAD_EXCEEDED_WARNING_TIME,
                        watchdog.getThread(), timeSincePat);
                LOGGER.log(Level.WARN, message);
                watchdog.restartThread();
            }
        }
    }
}
