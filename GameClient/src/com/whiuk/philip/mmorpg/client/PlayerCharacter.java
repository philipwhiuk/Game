package com.whiuk.philip.mmorpg.client;


import org.lwjgl.input.Keyboard;

import com.whiuk.philip.mmorpg.client.GameSettings.Control;
import com.whiuk.philip.mmorpg.client.models.Model;
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
     * 
     */
    private Model model;
    /**
     * 
     */
    private float x;
    /**
     * 
     */
    private float y;
    /**
     * 
     */
    private float z;

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
        model = Model.fromFile("player.mdl");
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
        org.lwjgl.opengl.GL11.glPushMatrix();
        org.lwjgl.opengl.GL11.glTranslatef(x, y, z);
//TODO: org.lwjgl.opengl.GL11.glRotatef(direction);
        model.render();
        org.lwjgl.opengl.GL11.glPopMatrix();
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
