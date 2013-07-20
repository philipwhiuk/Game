package com.whiuk.philip.mmorpg.client;

import org.lwjgl.opengl.GL11;

import com.whiuk.philip.mmorpg.shared.Messages.ClientMessage;
import com.whiuk.philip.mmorpg.shared.Messages.ServerMessage;
/**
 * Controls the actual game, itself, while in progress.
 * @author Philip
 *
 */
class Game implements GameInterface {
    /**
     * Position.
     */
    private static final float QUAD_POS = 100;
    /**
     * Size.
     */
    private static final float QUAD_SIZE = 200;
    /**
     * Colour (R).
     */
    private static final float QUAD_R = 0.5f;
    /**
     * Colour (G).
     */
    private static final float QUAD_G = 0.5f;
    /**
     * Colour (B).
     */
    private static final float QUAD_B = 1.0f;
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
     * @param character Player character
     */
    Game(final PlayerCharacter character) {
        this.player = character;
        this.camera = new Camera();
        // TODO Auto-generated constructor stub
    }

    @Override
    public final void handleGameData(final ServerMessage.GameData data) {
        if (data.hasMovementInformation()) {
            player.handleMovement(data.getMovementInformation());
        }
    }
    /**
     * Perform the game update.
     */
    final void update() {
        boolean serverUpdateRequired = false;
        boolean movementChanged = player.updateMovement();
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
                        .setDirection(player.getDirection()).build());
            }
            GameClientUtils.sendGameData(data.build());
        }
    }
    /**
     * Render the game.
     */
    final void render() {
        // Clear the screen and depth buffer
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        // set the color of the quad (R,G,B,A)
        GL11.glColor3f(QUAD_R, QUAD_G, QUAD_B);
        // draw quad
        GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex2f(QUAD_POS, QUAD_POS);
        GL11.glVertex2f(QUAD_POS + QUAD_SIZE, QUAD_POS);
        GL11.glVertex2f(QUAD_POS + QUAD_SIZE, QUAD_POS + QUAD_SIZE);
        GL11.glVertex2f(QUAD_POS, QUAD_POS + QUAD_SIZE);
        GL11.glEnd();
        //Camera
        camera.render(player);
    }

}
