package com.whiuk.philip.mmorpg.client.game;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.lwjgl.opengl.GL11;

import com.whiuk.philip.mmorpg.client.GameClientUtils;
import com.whiuk.philip.mmorpg.client.GameInterface;
import com.whiuk.philip.mmorpg.shared.Messages.ClientMessage;
import com.whiuk.philip.mmorpg.shared.Messages.ServerMessage;
/**
 * Controls the actual game, itself, while in progress.
 * @author Philip
 *
 */
public class Game implements GameInterface {
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
     * Class logger.
     */
    private static final Logger LOGGER = Logger.getLogger(Game.class);
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
     * Ambient light source.
     */
    private Light ambientLight;

    /**
     * Other player characters.
     */
    private Map<Integer, OtherPlayerCharacter> otherPCs;
    /**
     * Non player characters.
     */
    private Map<Integer, NPC> npcs;
    /**
     * Structures.
     */
    private Map<Integer, Structure> structures;
    /**
     * Terrain.
     */
    private Terrain terrain;
    /**
     * @param character Player character
     */
    public Game(final PlayerCharacter character) {
        this.player = character;
        this.camera = new Camera();
        this.ambientLight = new Light();
        this.terrain = new Terrain();
        this.otherPCs = new HashMap<Integer, OtherPlayerCharacter>();
        this.npcs = new HashMap<Integer, NPC>();
        this.structures = new HashMap<Integer, Structure>();
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
    public final void render() {
        // Clear the screen and depth buffer
        LOGGER.trace("Rendering game");
        GL11.glClearDepth(1.0f);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glColorMaterial(GL11.GL_FRONT_AND_BACK,
                GL11.GL_AMBIENT_AND_DIFFUSE);
        GL11.glLoadIdentity();
        ambientLight.render();
        terrain.render();
        for (Map.Entry<Integer, Structure> e : structures.entrySet()) {
            e.getValue().render();
        }
        for (Map.Entry<Integer, NPC> e : npcs.entrySet()) {
            e.getValue().render();
        }
        for (Map.Entry<Integer, OtherPlayerCharacter> e : otherPCs.entrySet()) {
            e.getValue().render();
        }
        player.render();
        //Camera
        camera.render(player);
        GL11.glDisable(GL11.GL_BLEND);
    }
}
