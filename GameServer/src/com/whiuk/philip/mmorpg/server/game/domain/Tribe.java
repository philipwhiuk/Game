package com.whiuk.philip.mmorpg.server.game.domain;
import java.util.Map;
import java.util.Set;

import com.whiuk.philip.mmorpg.server.game.ai.Agent;
import com.whiuk.philip.mmorpg.server.game.ai.Messages.DeclineMessage;
import com.whiuk.philip.mmorpg.server.game.ai.Messages.InformMessage;
import com.whiuk.philip.mmorpg.server.game.ai.Messages.OfferMessage;
import com.whiuk.philip.mmorpg.server.game.ai.Messages.PromiseMessage;
import com.whiuk.philip.mmorpg.server.game.ai.Messages.RequestMessage;
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

    @Override
    public final void processInform(Agent srcAgent, InformMessage msg) {
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
                processUnknownCharInform((GameCharacter) srcAgent,msg);
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
    private void processUnknownGroupInform(Agent srcAgent, InformMessage msg) {
        // TODO Auto-generated method stub
        
    }

    /**
     * 
     * @param srcAgent
     * @param msg
     */
    private void processWarGroupInform(GameCharacterGroup srcAgent,
            InformMessage msg) {
        // TODO Auto-generated method stub
        
    }

    /**
     * 
     * @param srcAgent
     * @param msg
     */
    private void processHostileGroupInform(GameCharacterGroup srcAgent,
            InformMessage msg) {
        // TODO Auto-generated method stub
        
    }

    /**
     * 
     * @param srcAgent
     * @param msg
     */
    private void processNeutralGroupInform(GameCharacterGroup srcAgent,
            InformMessage msg) {
        // TODO Auto-generated method stub
        
    }

    /**
     * 
     * @param srcAgent
     * @param msg
     */
    private void processFriendGroupInform(GameCharacterGroup srcAgent,
            InformMessage msg) {
        // TODO Auto-generated method stub
        
    }

    /**
     * 
     * @param srcAgent
     * @param msg
     */
    private void processAllyGroupInform(GameCharacterGroup srcAgent,
            InformMessage msg) {
        // TODO Auto-generated method stub
        
    }

    /**
     * 
     * @param srcAgent
     * @param msg
     */
    private void processUnknownCharInform(GameCharacter srcAgent, InformMessage msg) {
        // TODO Auto-generated method stub
        
    }

    /**
     * 
     * @param srcAgent
     * @param msg
     */
    private void processWarCharInform(GameCharacter srcAgent, InformMessage msg) {
        // TODO Auto-generated method stub
        
    }

    /**
     * 
     * @param srcAgent
     * @param msg
     */
    private void processHostileCharInform(GameCharacter srcAgent, InformMessage msg) {
        // TODO Auto-generated method stub
        
    }

    /**
     * 
     * @param srcAgent
     * @param msg
     */
    private void processNeutralCharInform(GameCharacter srcAgent, InformMessage msg) {
        // TODO Auto-generated method stub
        
    }

    /**
     * 
     * @param srcAgent
     * @param msg
     */
    private void processFriendCharInform(GameCharacter srcAgent, InformMessage msg) {
        // TODO Auto-generated method stub
        
    }

    /**
     * 
     * @param srcAgent
     * @param msg
     */
    private void processAllyCharInform(GameCharacter srcAgent, InformMessage msg) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void processRequest(GameCharacter srcAgent, RequestMessage msg) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void processOffer(Agent srcAgent, OfferMessage msg) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void processPromise(Agent srcAgent, PromiseMessage msg) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void processDecline(Agent srcAgent, DeclineMessage msg) {
        // TODO Auto-generated method stub
        
    }
}
