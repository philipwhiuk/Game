package com.whiuk.philip.mmorpg.client;

import org.lwjgl.input.Keyboard;

import com.whiuk.philip.mmorpg.client.GameSettings.Control;
import com.whiuk.philip.mmorpg.shared.Messages
    .ServerMessage.GameData.MovementInformation;

/**
 * @author Philip
 *
 */
class PlayerCharacter {
    /**
     * Current direction.
     */
    private float direction;
    /**
     * Whether or not the player is moving.
     */
    private boolean moving = false;

    /**
     * Initial data constructor.
     * @param id ID
     * @param name Name
     * @param race Race
     * @param location Location
     */
    PlayerCharacter(final int id, final String name,
            final String race, final String location) {
        // TODO Auto-generated constructor stub
    }
    /**
     * @return the direction
     */
    final float getDirection() {
        return direction;
    }
    /**
     * @param d the direction to set
     */
    final void setDirection(final float d) {
        this.direction = d;
    }
    /**
     * Render player character.
     */
    void render() {
        // TODO Auto-generated method stub
    }
    /**
     * Handle movement.
     * @return <code>true</code> if the player's movement has changed.
     */
    final boolean updateMovement() {
        boolean movementChanged = false;
        if (Keyboard.isKeyDown(GameSettings.getSettings()
                .getKeyMapping(Control.MOVE_FOREWARD))) {
            if (!moving) {
                moving = true;
                movementChanged = true;
            }
            //Do Move Forward
        } else if (Keyboard.isKeyDown(GameSettings.getSettings()
                .getKeyMapping(Control.MOVE_BACKWARD))) {
            if (!moving) {
                moving = true;
                movementChanged = true;
            }
            //Do Move Backward
        }
        if (Keyboard.isKeyDown(GameSettings.getSettings()
                .getKeyMapping(Control.TURN_LEFT))) {
            if (!moving) {
                moving = true;
                movementChanged = true;
            }
            //Do Turn Left
        } else if (Keyboard.isKeyDown(GameSettings.getSettings()
                .getKeyMapping(Control.TURN_RIGHT))) {
            if (!moving) {
                moving = true;
                movementChanged = true;
            }
            //Do Turn Right
        }
        return movementChanged;
    }
    /**
     * @param movementInformation Movement information.
     */
    void handleMovement(final MovementInformation movementInformation) {
        // TODO Auto-generated method stub
    }
}
