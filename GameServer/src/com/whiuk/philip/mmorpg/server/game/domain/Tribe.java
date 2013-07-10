package com.whiuk.philip.mmorpg.server.game.domain;
import java.util.Map;
import java.util.Set;

import com.whiuk.philip.mmorpg.server.game.ai.Agent;
import com.whiuk.philip.mmorpg.server.game.service.GameService;

/**
 * 
 * @author Philip
 *
 */
public class Tribe extends Agent implements GameCharacterGroup {
    /**
     * 
     */
    private Map<GameCharacter, Relationship> characterRelationships;
    /**
     * 
     */
    private Map<GameCharacterGroup, Relationship> tribalRelationships;
    /**
     * 
     */
    private GameService gameService;
    /**
     * 
     */
    private Set<GameCharacter> members;

    /**
     * 
     * @param c
     */
    final void assignPosition(final Character c) {
        gameService.getRandom().nextInt();
    }

    @Override
    public void add(GameCharacter newMember) {
        members.add(newMember);
    }

    @Override
    public void remove(GameCharacter oldMember) {
        members.remove(oldMember);
        
    }
}
