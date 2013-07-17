package com.whiuk.philip.mmorpg.server.game.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whiuk.philip.mmorpg.server.game.domain.Action;
import com.whiuk.philip.mmorpg.server.game.domain.GameCharacter;
import com.whiuk.philip.mmorpg.server.game.domain.Item;
import com.whiuk.philip.mmorpg.server.game.domain.PlayerCharacter;
import com.whiuk.philip.mmorpg.server.game.domain.Tile;
import com.whiuk.philip.mmorpg.server.game.service.GameService;
import com.whiuk.philip.mmorpg.shared.Messages.ServerMessage;
import com.whiuk.philip.mmorpg.shared.Messages.ServerMessage.GameData;
import com.whiuk.philip.mmorpg.shared.Messages.ServerMessage.GameData.ZoneInformation.TileData;

/**
 * 
 * @author Philip
 *
 */
@Service
public class GameCharacterControllerImpl implements GameCharacterController {

    @Autowired
    private GameService gameService;

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
    public void sendZoneData(final GameCharacter character,
            final Tile[][] data) {
        TileData.Builder td = TileData.newBuilder();
        for (Tile[] trow : data) {
            TileData.TileRow.Builder tr = TileData.TileRow.newBuilder();
            for (Tile t : trow) {
                tr.addTile(TileData.TileRow.Tile.newBuilder().build());
            }
            td.addTileRow(tr.build());
        }
        if (character instanceof PlayerCharacter) {
            GameData message = ServerMessage.GameData.newBuilder()
                    .setZoneInformation(
                            ServerMessage.GameData
                            .ZoneInformation.newBuilder()
                            .setTileData(td.build())
                            .build())
                    .build();
            gameService.sendGameData(character, message);
        }
    }
}
