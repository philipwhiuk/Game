package com.whiuk.philip.mmorpg.server.watchdog;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.whiuk.philip.util.watchdog.Watchable;
import com.whiuk.philip.util.watchdog.Watchdog;

/**
 * @author Philip Whitehouse
 */
@Service
public class WatchdogServiceImpl implements WatchdogService {

    /**
     * List of watchdogs monitored by the service.
     */
    private List<Watchdog> watchdogs;

    /**
	 *
	 */
    public WatchdogServiceImpl() {
        watchdogs = new ArrayList<Watchdog>();
    }

    @Override
    public final void monitor(final Watchable watched) {
        watchdogs.add(new WatchdogServiceWatchdog(watched));
    }

}
