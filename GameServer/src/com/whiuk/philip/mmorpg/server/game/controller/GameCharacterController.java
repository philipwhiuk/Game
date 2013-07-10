package com.whiuk.philip.mmorpg.server.game.controller;

import com.whiuk.philip.mmorpg.server.game.domain.Action;
import com.whiuk.philip.mmorpg.server.game.domain.GameCharacter;
import com.whiuk.philip.mmorpg.server.game.domain.Item;
import com.whiuk.philip.mmorpg.server.game.domain.PlayerCharacter;

/**
 * 
 * @author Philip
 *
 */
public class GameCharacterController {
    /**
     * 
     * @param character
     * @param source
     * @param target
     */
    public void smith(final GameCharacter character,
            final int source, final int target) {
        // TODO Auto-generated method stub

    }

    /**
     * 
     * @param character
     * @param source
     * @param target
     */
    public void mine(final GameCharacter character,
            final int source, final int target) {
        // TODO Auto-generated method stub
    }

    /**
     * 
     * @param character
     * @param source
     * @param target
     */
    public void craft(final GameCharacter character,
            final int source, final int target) {
        // TODO Auto-generated method stub

    }

    /**
     * 
     * @param character
     * @param source
     */
    public void take(GameCharacter character, int source) {
        // TODO Auto-generated method stub
        
    }

    /**
     * 
     * @param character
     * @param source
     */
    public void drop(GameCharacter character, int source) {
        // TODO Auto-generated method stub
        
    }

    /**
     * 
     * @param character
     * @param source
     */
    public void equip(GameCharacter character, int source) {
        // TODO Auto-generated method stub
        
    }

    /**
     * 
     * @param character
     * @param source
     */
    public void examine(GameCharacter character, int source) {
        // TODO Auto-generated method stub
        
    }

    /**
     * 
     * @param character
     * @param source
     */
    public void unequip(GameCharacter character, int source) {
        // TODO Auto-generated method stub
        
    }

    /**
     * 
     * @param character
     * @param source
     * @param target
     */
    public void cast(GameCharacter character, int source, int target) {
        // TODO Auto-generated method stub
        
    }

    /**
     * 
     * @param character
     * @param i1ID
     * @param i2ID
     */
    public void use(final PlayerCharacter character,
            final int i1ID, final int i2ID) {
        Item i1 = character.getItemById(i1ID);
        Item i2 = character.getItemById(i2ID);
        if (i1.canUseOn(i2)) {
            //TODO: Multiple action possibilities
            Action a = i1.getAction(i2);
            if (character.canPerform(a)) {
                character.doAction(a);
            } else {
                //TODO: Send message, need reqs.
            }
        } else {
            //TODO: Send message, not valid action.
        }
    }
}
