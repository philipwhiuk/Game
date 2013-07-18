package com.whiuk.philip.mmorpg.server.game.controller;

import org.springframework.stereotype.Service;

import com.whiuk.philip.mmorpg.server.game.domain.Action;
import com.whiuk.philip.mmorpg.server.game.domain.GameCharacter;
import com.whiuk.philip.mmorpg.server.game.domain.Item;
import com.whiuk.philip.mmorpg.server.game.domain.PlayerCharacter;

/**
 * 
 * @author Philip
 *
 */
@Service
public class GameCharacterControllerImpl implements GameCharacterController {

    /**
     * 
     */
    public GameCharacterControllerImpl() {
    }
    
    @Override
    public void use(final GameCharacter character,
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

    @Override
    public void smith(GameCharacter character, int source, int target) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mine(GameCharacter character, int source, int target) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void craft(GameCharacter character, int source, int target) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void take(GameCharacter character, int source) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void drop(GameCharacter character, int source) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void equip(GameCharacter character, int source) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void examine(GameCharacter character, int source) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void unequip(GameCharacter character, int source) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void cast(GameCharacter character, int source, int target) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void handleExit(PlayerCharacter playerCharacter) {
        // TODO Auto-generated method stub
    }
}
