package com.whiuk.philip.util.watchdog;

/**
 * @author Philip
 *
 */
public interface Watchdog extends Runnable {

    /**
     *
     */
    void restartThread();

    /**
     * @return
     */
    long lastPat();

    /**
     * @return
     */
    Thread getThread();

    /**
     * @return
     */
    long getWarningTime();
}
