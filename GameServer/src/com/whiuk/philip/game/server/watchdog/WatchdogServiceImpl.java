package com.whiuk.philip.game.server.watchdog;

import java.util.List;

import org.springframework.stereotype.Service;

import com.whiuk.philip.util.watchdog.Watchable;
import com.whiuk.philip.util.watchdog.Watchdog;

@Service
public class WatchdogServiceImpl implements WatchdogService {

	/**
	 * List of watchdogs monitored by the service.
	 */
	private List<Watchdog> watchdogs;

	@Override
	public final void monitor(final Watchable watched) {
		watchdogs.add(new WatchdogServiceWatchdog(watched));
	}

}
