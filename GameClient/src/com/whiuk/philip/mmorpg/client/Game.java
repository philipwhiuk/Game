package com.whiuk.philip.mmorpg.client;

import org.lwjgl.opengl.GL11;

import com.whiuk.philip.mmorpg.shared.Messages.ServerMessage;

/**
 * Controls the actual game, itself, while in progress.
 * @author Philip
 *
 */
public class Game {
    /**
     * Player.
     */
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
     * @param message
     */
    public void handleGameMessage(final ServerMessage message) {
        // TODO Auto-generated method stub
    }
    /**
     * Render the game.
     */
    public final void render() {
        // Clear the screen and depth buffer
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        // set the color of the quad (R,G,B,A)
        GL11.glColor3f(0.5f, 0.5f, 1.0f);

        // draw quad
        GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex2f(100, 100);
        GL11.glVertex2f(100+200, 100);
        GL11.glVertex2f(100+200, 100+200);
        GL11.glVertex2f(100, 100+200);
        GL11.glEnd();
    }

}
