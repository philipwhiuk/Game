package com.whiuk.philip.mmorpg.server.game;
import java.util.Map;

import com.whiuk.philip.mmorpg.server.game.ai.Agent;

/**
 * 
 * @author Philip
 *
 */
public class Tribe extends Agent {
    /**
     * 
     */
    private Map<Character, Relationship> characterRelationships;
    /**
     * 
     */
    private Map<Tribe, Relationship> tribalRelationships;
    /**
     * 
     */
    private GameService gameService;

    /**
     * 
     * @param c
     */
    final void assignPosition(final Character c) {
        gameService.getRandom().nextInt();
    }
}
