package com.whiuk.philip.mmorpg.server.game.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author Philip
 *
 */
@Entity
public class Race {

    /**
     * 
     */
    @Id
    private Long id;
    /**
     * 
     */
    private String name;
    /**
     * 
     */
    private boolean playable;
    /**
     * @return name
     */
    public String getName() {
        return name;
    }
    /**
     * @return playable
     */
    public boolean isPlayable() {
        return playable;
    }

}
