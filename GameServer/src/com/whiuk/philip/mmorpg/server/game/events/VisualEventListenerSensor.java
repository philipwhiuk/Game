package com.whiuk.philip.mmorpg.server.game.events;


/**
 * 
 * @author Philip
 *
 */
public interface VisualEventListenerSensor extends EventListenerSensor {
    /**
     * 
     * @param c
     */
    void characterArrived(GameCharacterEvent c);
    /**
     * 
     * @param c
     */
    void characterLeft(GameCharacterEvent c);
    /**
     * 
     * @param c
     */
    void characterMoved(GameCharacterEvent c);
    /**
     * 
     * @param c
     */
    void itemSpawned(ItemEvent i);
    /**
     * 
     * @param c
     */
    void itemConsumed(ItemEvent i);
    /**
     * 
     * @param c
     */
    void resourceSpawned(ResourceEvent r);
    /**
     * 
     * @param c
     */
    void resourceConsumed(ResourceEvent r);
    /**
     * 
     * @param t
     */
    void tileModified(TileEvent t);
    /**
     * 
     * @param t
     */
    void buildingCreated(BuildingEvent t);
    /**
     * 
     * @param t
     */
    void buildingModified(BuildingEvent t);
    /**
     * 
     * @param t
     */
    void buildingDestroyed(BuildingEvent t);
}
