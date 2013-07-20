package com.whiuk.philip.mmorpg.client;

/**
 * The game camera.
 * <p>Technically there's no such thing as a camera in OpenGL.
 * The purpose of this class is mimic the functionality
 * of a camera, by modifying the rendered view accordingly.</p>
 * <p>The camera is tied to the player, with distance and angle on the player
 * being relative.</p>
 * @author Philip
 *
 */
class Camera {

    /**
     * Rotate <code>d</code> degrees around the Y (vertical) axis.
     * @param d degrees
     */
    void rotateY(final float d) {
        //TODO:
    }
    /**
     * Rotate <code>d</code> degrees around the X axis.
     * @param d degrees
     */
    void rotateX(final float d) {
        //TODO:
    }
    /**
     * Rotate <code>d</code> degrees around the Z axis.
     * @param d degrees
     */
    void rotateZ(final float d) {
        //TODO:
    }
    /**
     * Translate <code>x,y,z</code>.
     * @param x Units in the x-axis
     * @param y Units in the y-axis
     * @param z Units in the z-axis
     */
    void translate(final float x, final float y, final float z) {
        //TODO:
    }
    /**
     * Zoom in by the given factor.
     * @param zoom multiplication factor (relative to current)
     */
    void zoom(final float zoom) {
        //TODO:
    }
    /**
     * Apply the rendering transformations.
     * @param p The player character to transform based on.
     */
    void render(final PlayerCharacter p) {
        // TODO Auto-generated method stub
    }
}
