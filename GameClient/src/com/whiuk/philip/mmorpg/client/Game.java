package com.whiuk.philip.mmorpg.client;

import com.whiuk.philip.mmorpg.shared.Messages.ServerMessage;

/**
 * Controls the actual game, itself, while in progress.
 * @author Philip
 *
 */
public class Game {
    private PlayerCharacter player;

    /**
     * @param character
     */
    public Game(final PlayerCharacter character) {
        this.player = character;
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     */
    public void handleChatMessage(final ServerMessage message) {
        // TODO Auto-generated method stub
    }

    /**
     * 
     * @param message
     */
    public void handleGameMessage(final ServerMessage message) {
        // TODO Auto-generated method stub
        
    }

}
