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
        GL11.glClearDepth(1.0f);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glColorMaterial(GL11.GL_FRONT_AND_BACK,
                GL11.GL_AMBIENT_AND_DIFFUSE);
        player.render();
        //Camera
        camera.render(player);
    }

}
