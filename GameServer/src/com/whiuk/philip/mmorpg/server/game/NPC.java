package com.whiuk.philip.mmorpg.server.game;

import java.awt.Point;
import java.util.Map;

import com.whiuk.philip.mmorpg.server.game.ai.Agent;

/**
 * 
 * @author Philip
 *
 */
public class NPC extends Agent {
    /**
     * 
     */
    private Map<NPC, Relationship> characterRelationships;
    /**
     * 
     */
    private Map<Tribe, Relationship> tribalRelationships;
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
}
