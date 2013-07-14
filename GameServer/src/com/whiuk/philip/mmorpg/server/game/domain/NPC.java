package com.whiuk.philip.mmorpg.server.game.domain;

import java.awt.Point;
import java.util.Map;

import com.whiuk.philip.mmorpg.server.game.ai.Agent;

/**
 * Represents a non-player character
 * (one controlled by the game server and not a player).
 * @author Philip
 *
 */
public class NPC extends Agent implements GameCharacter {
    /**
     * Relationships with other characters.
     */
    private Map<GameCharacter, Relationship> characterRelationships;
    /**
     * Relationships with groups.
     */
    private Map<GameCharacterGroup, Relationship> tribalRelationships;
    /**
     * 
     */
    private Point position;
    /**
     * 
     */
    private Map<Attribute, Integer> attributes;
    /**
     * 
     */
    private Map<Skill, Integer> skills;
    
    @Override
    public Item getItemById(int i1id) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public boolean canPerform(Action a) {
        // TODO Auto-generated method stub
        return false;
    }
    @Override
    public void doAction(Action a) {
        // TODO Auto-generated method stub
        
    }
    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public Race getRace() {
        // TODO Auto-generated method stub
        return null;
    }
}
