package com.whiuk.philip.mmorpg.server.game.events;


/**
 * 
 * @author Philip
 *
 */
public interface VisualEventListenerSensor extends EventListenerSensor {
    /**
     * 
     * @param c Character event
     */
    void characterArrived(GameCharacterEvent c);
    /**
     * 
     * @param c Character event
     */
    void characterLeft(GameCharacterEvent c);
    /**
     * 
     * @param c Character event
     */
    void characterMoved(GameCharacterEvent c);
    /**
     * 
     * @param i Event
     */
    void itemSpawned(ItemEvent i);
    /**
     * 
     * @param i Event
     */
    void itemConsumed(ItemEvent i);
    /**
     * 
     * @param r Event
     */
    void resourceSpawned(ResourceEvent r);
    /**
     * 
     * @param r Event
     */
    void resourceConsumed(ResourceEvent r);
    /**
     * 
     * @param t Event
     */
    void tileModified(TileEvent t);
    /**
     * 
     * @param t Event
     */
    void buildingCreated(BuildingEvent t);
    /**
     * 
     * @param t Event
     */
    void buildingModified(BuildingEvent t);
    /**
     * 
     * @param t Event
     */
    void buildingDestroyed(BuildingEvent t);
}
