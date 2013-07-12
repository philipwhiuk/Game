package com.whiuk.philip.mmorpg.server.game.events;

import com.whiuk.philip.mmorpg.server.game.domain.Building;

/**
 * Provides details on an event that occurred to a Building.
 * @author Philip
 *
 */
public class BuildingEvent {
    /**
     * Building on which the event occurred.
     */
    private final Building building;

    /**
     * @param building Building on which the event occurred
     */
    BuildingEvent(final Building building) {
        this.building = building;
    }

    /**
     * @return Building on which the event occurred
     */
    public final Building getBuilding() {
        return building;
    }
}
