package com.whiuk.philip.mmorpg.server.game.domain;


/**
 *
 * @author Philip
 *
 */
public class Building extends Structure implements Location {

    /**
     * Zone
     */
    private Zone zone;
    /**
     * Name
     */
    private String name;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Zone getZone() {
        return zone;
    }

}
