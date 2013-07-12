package com.whiuk.philip.mmorpg.server.game.domain;

import java.awt.Point;
import java.util.Map;

import com.whiuk.philip.mmorpg.server.game.ai.Agent;
import com.whiuk.philip.mmorpg.server.game.ai.Messages.DeclineMessage;
import com.whiuk.philip.mmorpg.server.game.ai.Messages.InformMessage;
import com.whiuk.philip.mmorpg.server.game.ai.Messages.OfferMessage;
import com.whiuk.philip.mmorpg.server.game.ai.Messages.PromiseMessage;
import com.whiuk.philip.mmorpg.server.game.ai.Messages.RequestMessage;

/**
 * Represents a non-player character
 * (one controlled by the game server and not a player).
 * @author Philip
 *
 */
public class NPC extends GameAgent implements GameCharacter {
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
