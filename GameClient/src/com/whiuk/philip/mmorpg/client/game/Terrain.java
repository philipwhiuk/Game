package com.whiuk.philip.mmorpg.client.game;

import org.lwjgl.opengl.GL11;

import com.whiuk.philip.gameEngine.Node;

/**
 * @author Philip
 *
 */
public class Terrain extends Node {
    /**
     * 
     */
    private float x;
    /**
     * 
     */
    private float width;
    /**
     * 
     */
    private float y;
    /**
     * 
     */
    private float z;
    /**
     * 
     */
    private float length;

    /**
     * @param x X
     * @param y Y
     * @param z Z
     * @param w W
     * @param l L
     */
    Terrain(final float x, final float y, final float z,
            final float w, final float l) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.width = w;
        this.length = l;
    }
    
    /**
     * Render terrain
     */
    void render() {
        //TODO: Tile-based terrain
        GL11.glPushMatrix();
            GL11.glBegin(GL11.GL_QUADS);
            // Set The Colour To Green
                GL11.glColor3f(0.0f, 0.39f, 0.0f);
                GL11.glVertex3f(x, y, -z);
                GL11.glVertex3f(x + width, y, -z);
                GL11.glVertex3f(x + width, y, -(z + length));
                GL11.glVertex3f(x, y, -(z + length));
            GL11.glEnd();
        GL11.glPopMatrix();

    }
}
