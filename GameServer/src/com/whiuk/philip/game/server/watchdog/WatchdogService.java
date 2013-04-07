package com.whiuk.philip.game.server.watchdog;

import com.whiuk.philip.util.watchdog.Watchable;

/**
 * @author Philip Whitehouse
 */
public interface WatchdogService {
    /**
     * @param watched
     */
    void monitor(Watchable watched);

}
