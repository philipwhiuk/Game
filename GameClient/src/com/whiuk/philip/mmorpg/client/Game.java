package com.whiuk.philip.mmorpg.client;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.whiuk.philip.mmorpg.shared.Messages.ClientMessage;
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
     * Camera.
     */
    private Camera camera;
    /**
     * Whether the player is moving.
     */
    private boolean moving;

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
    public void handleGameMessage(final ServerMessage.GameData data) {
        // TODO Auto-generated method stub
    }
    /**
     * Perform the game update.
     */
    public final void update() {
        boolean serverUpdateRequired = false;
        boolean movementChanged = player.handleMovement();
        if (movementChanged) {
            serverUpdateRequired = true;
        }
        //TODO: Other updates.

        if (serverUpdateRequired) {
            ClientMessage.GameData.Builder data =
                    ClientMessage.GameData.newBuilder();
            if (moving) {
                data.setMovementInformation(
                        ClientMessage.GameData.MovementInformation.newBuilder()
                        .setDirection(player.direction).build());
            }
            GameClientUtils.sendGameData(data.build());
        }
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
        //Camera
        camera.render(player);
    }

}
