package com.whiuk.philip.mmorpg.server.game.domain;

import java.util.List;

import javax.persistence.OneToMany;

import com.whiuk.philip.mmorpg.server.game.ai.Environment;

/**
 * Zone.
 * @author Philip
 *
 */
public class Zone implements Environment, Location {
    /**
     * 
     */
    @OneToMany(mappedBy = "zone")
    private List<GameCharacter> characters;
    /**
     * 
     */
    private String name;
    /**
     * Tile data
     */
    private Tile[][] tilemap;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Zone getZone() {
        return this;
    }

    /**
     * @param gc
     */
    public final void addCharacter(final GameCharacter gc) {
        characters.add(gc);
    }

    /**
     * @return tile map
     */
    public final Tile[][] getData() {
        return (Tile[][]) tilemap.clone();
    }

    /**
     * @return
     */
    public final GameCharacter[] getCharacters() {
        return characters.toArray(new GameCharacter[characters.size()]);
    }
}
