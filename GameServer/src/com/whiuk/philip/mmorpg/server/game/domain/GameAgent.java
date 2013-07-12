package com.whiuk.philip.mmorpg.server.game.domain;

import java.util.Map;

import com.whiuk.philip.mmorpg.server.game.ai.Agent;
import com.whiuk.philip.mmorpg.server.game.ai.Messages.DeclineMessage;
import com.whiuk.philip.mmorpg.server.game.ai.Messages.InformMessage;
import com.whiuk.philip.mmorpg.server.game.ai.Messages.OfferMessage;
import com.whiuk.philip.mmorpg.server.game.ai.Messages.PromiseMessage;
import com.whiuk.philip.mmorpg.server.game.ai.Messages.RequestMessage;

/**
 * <p>Provides a middle-layer between the theoretical {@link Agent}
 * and implementations such as {@link NPC} and {@link Tribe}.</p>
 * <p>Incorporates relationship modelling between the agent
 * and characters and groups.</p>
 * @author Philip
 *
 */
public abstract class GameAgent extends Agent {
    /**
     * Relationships with other characters.
     */
    private Map<GameCharacter, Relationship> characterRelationships;
    /**
     * Relationships with groups.
     */
    private Map<GameCharacterGroup, Relationship> tribalRelationships;
    @Override
    public final void processInform(
            final Agent srcAgent, final InformMessage msg) {
        if (srcAgent instanceof GameCharacter) {
            if (!characterRelationships.containsKey(srcAgent)) {
                switch (characterRelationships.get(srcAgent).getState()) {
                    case ALLIES:
                        processAllyCharInform((GameCharacter) srcAgent, msg);
                        break;
                    case FRIENDLY:
                        processFriendCharInform((GameCharacter) srcAgent, msg);
                        break;
                    case NEUTRAL:
                        processNeutralCharInform((GameCharacter) srcAgent, msg);
                        break;
                    case HOSTILE:
                        processHostileCharInform((GameCharacter) srcAgent, msg);
                        break;
                    case WAR:
                        processWarCharInform((GameCharacter) srcAgent, msg);
                        break;
                    default:
                        throw new IllegalStateException(
                                "Unsupported relationship type");
                }
            } else {
                processUnknownCharInform((GameCharacter) srcAgent, msg);
            }
        } else if (srcAgent instanceof GameCharacterGroup) {
            if (!tribalRelationships.containsKey(srcAgent)) {
                switch (characterRelationships.get(srcAgent).getState()) {
                    case ALLIES:
                        processAllyGroupInform(
                                (GameCharacterGroup) srcAgent, msg);
                        break;
                    case FRIENDLY:
                        processFriendGroupInform(
                                (GameCharacterGroup) srcAgent, msg);
                        break;
                    case NEUTRAL:
                        processNeutralGroupInform(
                                (GameCharacterGroup) srcAgent, msg);
                        break;
                    case HOSTILE:
                        processHostileGroupInform(
                                (GameCharacterGroup) srcAgent, msg);
                        break;
                    case WAR:
                        processWarGroupInform(
                                (GameCharacterGroup) srcAgent, msg);
                        break;
                    default:
                        throw new IllegalStateException(
                                "Unsupported relationship type");
                }
            } else {
                processUnknownGroupInform(srcAgent, msg);
            }
        } else {
            //TODO: Other Agent types
        }
    }

    /**
     * 
     * @param srcAgent
     * @param msg
     */
    private void processUnknownGroupInform(
            final Agent srcAgent, final InformMessage msg) {
        // TODO Auto-generated method stub
    }

    /**
     * 
     * @param srcAgent
     * @param msg
     */
    private void processWarGroupInform(final GameCharacterGroup srcAgent,
            final InformMessage msg) {
        // TODO Auto-generated method stub
    }

    /**
     * 
     * @param srcAgent
     * @param msg
     */
    private void processHostileGroupInform(final GameCharacterGroup srcAgent,
            final InformMessage msg) {
        // TODO Auto-generated method stub
        
    }

    /**
     * 
     * @param srcAgent
     * @param msg
     */
    private void processNeutralGroupInform(final GameCharacterGroup srcAgent,
            final InformMessage msg) {
        // TODO Auto-generated method stub
        
    }

    /**
     * 
     * @param srcAgent
     * @param msg
     */
    private void processFriendGroupInform(final GameCharacterGroup srcAgent,
            final InformMessage msg) {
        // TODO Auto-generated method stub
        
    }

    /**
     * 
     * @param srcAgent
     * @param msg
     */
    private void processAllyGroupInform(final GameCharacterGroup srcAgent,
            final InformMessage msg) {
        // TODO Auto-generated method stub
        
    }

    /**
     * 
     * @param srcAgent
     * @param msg
     */
    private void processUnknownCharInform(
            final GameCharacter srcAgent, final InformMessage msg) {
        // TODO Auto-generated method stub
        
    }

    /**
     * 
     * @param srcAgent
     * @param msg
     */
    private void processWarCharInform(
            final GameCharacter srcAgent, final InformMessage msg) {
        // TODO Auto-generated method stub
        
    }

    /**
     * 
     * @param srcAgent
     * @param msg
     */
    private void processHostileCharInform(
            final GameCharacter srcAgent, final InformMessage msg) {
        // TODO Auto-generated method stub
        
    }

    /**
     * 
     * @param srcAgent
     * @param msg
     */
    private void processNeutralCharInform(
            final GameCharacter srcAgent, final InformMessage msg) {
        // TODO Auto-generated method stub
        
    }

    /**
     * 
     * @param srcAgent
     * @param msg
     */
    private void processFriendCharInform(
            final GameCharacter srcAgent, final InformMessage msg) {
        // TODO Auto-generated method stub
        
    }

    /**
     * 
     * @param srcAgent
     * @param msg
     */
    private void processAllyCharInform(
            final GameCharacter srcAgent, final InformMessage msg) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void processOffer(final Agent srcAgent, final OfferMessage msg) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void processPromise(final Agent srcAgent, final PromiseMessage msg) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void processDecline(final Agent srcAgent, final DeclineMessage msg) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void processRequest(final Agent srcAgent, final RequestMessage msg) {
        // TODO Auto-generated method stub
        
    }

}
