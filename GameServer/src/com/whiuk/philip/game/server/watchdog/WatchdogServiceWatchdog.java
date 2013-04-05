package com.whiuk.philip.game.server.watchdog;

import com.whiuk.philip.util.watchdog.Watchable;
import com.whiuk.philip.util.watchdog.Watchdog;

public class WatchdogServiceWatchdog implements Watchdog {

	private static final long THREAD_START_UP_TIME = 0;
	private static final long CHECK_TIME = 0;
	private long warningTime;
	private long killTime;

	private boolean running;
	private boolean started;

	private long lastPat;
	private Watchable watchedObject;

	/**
	 *
	 * @param watched
	 */
	public WatchdogServiceWatchdog(Watchable watched) {
		this.watchedObject = watched;
		watchedObject.setWatchdog(this);
	}

	@Override
	public void run() {
		running = true;
		while (running) {
			try {
				Thread.sleep(THREAD_START_UP_TIME);
			} catch (InterruptedException e1) {
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
				}
			}
		}
	}

	@Override
	public void pat() {
		lastPat = System.nanoTime();
	}

	@Override
	public void restartThread() {
		watchedObject.restart();
	}

	@Override
	public long lastPat() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Thread getThread() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getWarningTime() {
		// TODO Auto-generated method stub
		return 0;
	}

}
