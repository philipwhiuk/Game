package com.whiuk.philip.mmorpg.server.game.events;


/**
 * A visual event listener sensor is a sensor
 * by which agents are notified of events within their visual range.
 * As an event listener it follows the event listener model, in which
 * eventlisteners register to a source, which notifies the listener
 * by firing events.
 * @author Philip
 *
 */
public interface VisualEventListenerSensor extends EventListenerSensor {
    /**
     * Indicates the given character arrived.
     * @param c Character event
     */
    void characterArrived(GameCharacterEvent c);
    /**
     * Indicates the given character left.
     * @param c Character event
     */
    void characterLeft(GameCharacterEvent c);
    /**
     * Indicates the given character moved.
     * @param c Character event
     */
    void characterMoved(GameCharacterEvent c);
    /**
     * Indicates the given item was spawned.
     * @param i Item event
     */
    void itemSpawned(ItemEvent i);
    /**
     * Indicates the given item was consumed.
     * @param i Item event
     */
    void itemConsumed(ItemEvent i);
    /**
     * Indicates the given resource was spawned.
     * @param r Resource event
     */
    void resourceSpawned(ResourceEvent r);
    /**
     * Indicates the given resource was consumed.
     * @param r Resource event
     */
    void resourceConsumed(ResourceEvent r);
    /**
     * Indicates the given tile was modified.
     * @param t Tile event
     */
    void tileModified(TileEvent t);
    /**
     * Indicates the given building was created.
     * @param b Building Event
     */
    void buildingCreated(BuildingEvent b);
    /**
     * Indicates the given building was modified.
     * @param b Building Event
     */
    void buildingModified(BuildingEvent b);
    /**
     * Indicates the given building was destroyed.
     * @param b Building Event
     */
    void buildingDestroyed(BuildingEvent b);
}
