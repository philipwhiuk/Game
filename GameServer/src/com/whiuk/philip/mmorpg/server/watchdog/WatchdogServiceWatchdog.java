package com.whiuk.philip.mmorpg.server.watchdog;

import com.whiuk.philip.util.watchdog.Watchable;
import com.whiuk.philip.util.watchdog.Watchdog;

/**
 * Implements a watchdog for the watchdog service.
 * 
 * @author Philip Whitehouse
 */
public class WatchdogServiceWatchdog implements Watchdog {

    /**
     * Amount of time thread is given to start up before patting the watchdog.
     */
    private static final long THREAD_START_UP_TIME = 0;
    /**
     * How often to check for pats.
     */
    private static final long CHECK_TIME = 0;
    /**
     * Amount of time before a thread is warned.
     */
    private long warningTime;
    /**
     * Amount of time before a thread is killed.
     */
    private long killTime;

    /**
     * Whether the watchdog is running.
     */
    private boolean running;
    /**
     * Whether the watched thread has started.
     */
    private boolean started;

    /**
     * The time the last pat occurred.
     */
    private long lastPat;
    /**
     * The object being watched.
     */
    private Watchable watchedObject;

    /**
     * @param watched
     */
    public WatchdogServiceWatchdog(final Watchable watched) {
        this.watchedObject = watched;
        watchedObject.setWatchdog(this);
    }

    @Override
    public final void run() {
        running = true;
        while (running) {
            try {
                Thread.sleep(THREAD_START_UP_TIME);
            } catch (InterruptedException e1) {
                //TODO: Handle interrupt
            }
            started = true;
            while (started) {
                if (lastPat < System.nanoTime() - warningTime) {
                    watchedObject.warn();
                }
                if (lastPat < System.nanoTime() - killTime) {
                    watchedObject.restart();
                    started = false;
                }
                try {
                    Thread.sleep(CHECK_TIME);
                } catch (InterruptedException e) {
                    //TODO: Handle interrupt
                }
            }
        }
    }

    @Override
    public final void pat() {
        lastPat = System.nanoTime();
    }

    @Override
    public final void restartThread() {
        watchedObject.restart();
    }

    @Override
    public final long lastPat() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public final Thread getThread() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public final long getWarningTime() {
        // TODO Auto-generated method stub
        return 0;
    }

}
