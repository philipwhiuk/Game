package com.whiuk.philip.mmorpg.server.game;
import java.util.Map;

import com.whiuk.philip.mmorpg.server.game.ai.Agent;


public class Tribe extends Agent {
    Map<Character, Relationship> characterRelationships;
    Map<Tribe, Relationship> tribalRelationships;
    
    private GameService gameService;
    
    /**
     * 
     * @param c
     */
    void assignPosition(Character c) {
        gameService.getRandom().nextInt();
    }
    
    
}
