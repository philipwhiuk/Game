package com.whiuk.philip.mmorpg.client;

import org.lwjgl.input.Keyboard;

/**
 * @author Philip
 *
 */
public class PlayerCharacter {
    //TODO: Key customisation
    /**
     * 
     */
    private static final int MOVE_FORWARD = Keyboard.KEY_W;
    /**
     * 
     */
    private static final int MOVE_BACKWARD = Keyboard.KEY_S;
    /**
     * 
     */
    private static final int TURN_LEFT = Keyboard.KEY_A;
    /**
     * 
     */
    private static final int TURN_RIGHT = Keyboard.KEY_D;
    /**
     * 
     */
    public float direction;
    /**
     * 
     */
    private boolean moving = false;

    /**
     * Initial data constructor.
     * @param id ID
     * @param name Name
     * @param race Race
     * @param location Location
     */
    public PlayerCharacter(final int id, final String name,
            final String race, final String location) {
        // TODO Auto-generated constructor stub
    }

    public void render() {
        // TODO Auto-generated method stub
        
    }
    public boolean handleMovement() {
        boolean movementChanged = false;
        if (Keyboard.isKeyDown(MOVE_FORWARD)) {
            if (!moving) {
                moving = true;
                movementChanged = true;
            } else {
                //Do Move Forward
            }
        } else if (Keyboard.isKeyDown(MOVE_BACKWARD)) {
            if (!moving) {
                moving = true;
                movementChanged = true;
            } else {
                //Do Move Backward
            }
        }
        if (Keyboard.isKeyDown(TURN_LEFT)) {
            if (!moving) {
                moving = true;
                movementChanged = true;
            } else {
                //Do Turn Left
            }
        } else if (Keyboard.isKeyDown(TURN_RIGHT)) {
            if (!moving) {
                moving = true;
                movementChanged = true;
            }
        }
        return movementChanged;
    }
}
