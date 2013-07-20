package com.whiuk.philip.mmorpg.server.watchdog;

import com.whiuk.philip.util.watchdog.Watchable;

/**
 * @author Philip Whitehouse
 */
public interface WatchdogService {
    /**
     * @param watched The object to monitor
     */
    void monitor(Watchable watched);

}
