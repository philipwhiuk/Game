package com.whiuk.philip.mmorpg.client.game;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

/**
 * Light source.
 * @author Philip
 *
 */
public class Light {
    /**
     * Position of light source.
     */
    private FloatBuffer position;
    /**
     * Model of light source.
     */
    private FloatBuffer model;
    /**
     * Colour.
     */
    private FloatBuffer colour;

    /**
     */
    Light() {
        position = BufferUtils.createFloatBuffer(4);
        position.put(1.0f).put(1.0f).put(10.0f).put(0.0f).flip();
        colour = BufferUtils.createFloatBuffer(4);
        colour.put(1.0f).put(1.0f).put(1.0f).put(1.0f).flip();
        model = BufferUtils.createFloatBuffer(4);
        model.put(0.5f).put(0.5f).put(0.5f).put(1.0f).flip();
    }

    /**
     *
     */
    final void render() {
        GL11.glEnable(GL11.GL_LIGHT0);
        // sets light position
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, position);
        // sets specular light to white
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_SPECULAR, colour);
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, colour);
    }

}
