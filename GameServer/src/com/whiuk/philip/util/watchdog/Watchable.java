package com.whiuk.philip.util.watchdog;

/**
 * @author Philip
 *
 */
public interface Watchable {
	/**
	 * Warn the object it's not notified
	 * the watchdog before the warn limit.
	 */
	void warn();

	/**
	 * Restart the object as it's exceeded the kill limit.
	 */
	void restart();

	/**
	 * Set the watchdog monitoring the thread.
	 * @param watchdog The watchdog
	 */
	void setWatchdog(Watchdog watchdog);

}
