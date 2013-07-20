package com.whiuk.philip.util.watchdog;

/**
 * @author Philip
 */
public interface Watchdog extends Runnable {

    /**
     *
     */
    void restartThread();

    /**
     * @return number of milliseconds since last pat.
     */
    long lastPat();

    /**
     * @return thread
     */
    Thread getThread();

    /**
     * @return warning time
     */
    long getWarningTime();

    /**
     * Pat the watchdog.
     */
    void pat();
}
