package com.whiuk.philip.game.server.watchdog;

import com.whiuk.philip.game.server.game.GameWorld;
import com.whiuk.philip.util.watchdog.Watchable;

public interface WatchdogService {

	void monitor(Watchable watched);

}
