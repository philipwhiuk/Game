package com.whiuk.philip.mmorpg.client;

/**
 * A queued event waiting for GUI updates.
 * @author Philip
 *
 */
interface QueuedLWJGLEvent extends Runnable {
    /**
     * @return whether the event can be run.
     */
    boolean canRun();

}
