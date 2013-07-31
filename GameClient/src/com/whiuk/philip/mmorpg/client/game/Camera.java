package com.whiuk.philip.mmorpg.client.game;

import org.lwjgl.util.glu.GLU;

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
     * The way in which camera handling works and
     * the perspective that is provided.
     * @author Philip
     *
     */
    public enum Mode {
        /**
         * Third-person, static offset camera mode.
         */
        THIRD_PERSON_FIXED_STATIC("Third-person, static offset",
                "A third person camera that maintains a set angle relative"
                + " to the map and set distance from the player."),
        /**
         * Third-person, static angle, follow camera mode.
         */
        THIRD_PERSON_FIXED_FOLLOW("Third-person, static follow",
                "A third person camera that maintains a set angle relative"
                + " to the player and set distance from the player."),
        /**
         * Third-person, dynamic follow camera mode.
         */
        THIRD_PERSON_DYNAMIC_FOLLOW("Third-person, dynamic follow",
                "A third person camera that provides a dynamic follow,"
                + " adjusting for terrain."),
        /**
         * First-person camera mode.
         */
        FIRST_PERSON("First person",
                "A first person camera");
        /**
         * Human readable name.
         */
        private final String title;
        /**
         * Description.
         */
        private final String description;
        /**
         * Constructor.
         * @param t title
         * @param d description
         */
        Mode(final String t, final String d) {
            this.title = t;
            this.description = d;
        }
        @Override
        public String toString() {
            return title;
        }
        /**
         * @return title
         */
        public String title() {
            return title;
        }
        /**
         * @return description
         */
        public String description() {
            return description;
        }
    }
    /**
     * Camera mode.
     */
    private Mode mode;
    /**
     * Player-relative offset (x co-ordinate).
     */
    private float xOffset = 0f;
    /**
     * Player-relative offset (y co-ordinate).
     */
    private float yOffset = 5f;
    /**
     * Player-relative offset (z co-ordinate).
     */
    private float zOffset = 5f;
    /**
     * Construct camera in default mode.
     */
    Camera() {
        this(Mode.THIRD_PERSON_FIXED_STATIC);
    }
    /**
     * Construct camera in given mode.
     * @param m mode
     */
    Camera(final Mode m) {
        this.mode = m;
    }

    /**
     * Rotate <code>d</code> degrees around the Y (vertical) axis.
     * @param d degrees
     */
    void rotateY(final float d) {
        //TODO Rotate around y-axis.
    }
    /**
     * Rotate <code>d</code> degrees around the X axis.
     * @param d degrees
     */
    void rotateX(final float d) {
        //TODO Rotate around x-axis.
    }
    /**
     * Rotate <code>d</code> degrees around the Z axis.
     * @param d degrees
     */
    void rotateZ(final float d) {
        //TODO Rotate around z-axis.
    }
    /**
     * Translate <code>x,y,z</code>.
     * @param x Units in the x-axis
     * @param y Units in the y-axis
     * @param z Units in the z-axis
     */
    void translate(final float x, final float y, final float z) {
        //TODO Translate.
    }
    /**
     * Zoom in by the given factor.
     * @param zoom multiplication factor (relative to current)
     */
    void zoom(final float zoom) {
        //TODO Zoom.
    }
    /**
     * Apply the rendering transformations.
     * @param p The player character to transform based on.
     */
    void render(final PlayerCharacter p) {
        // TODO Auto-generated method stub
        GLU.gluLookAt(
                p.getX() + xOffset, p.getY() + yOffset, p.getZ() + zOffset,
                p.getX(), p.getY(), p.getZ(),
                0f, 1.0f, 0.0f);
    }
    /**
     * @return the mode
     */
    public Mode getMode() {
        return mode;
    }
    /**
     * @param m the mode to set
     */
    public void setMode(final Mode m) {
        this.mode = m;
    }
    /**
     * Switch to the next mode.
     */
    public void nextMode() {
        Mode[] modes = Mode.values();
        for (int i = 0; i < modes.length; i++) {
            if (mode == modes[i]) {
                mode = modes[(i + 1) % modes.length];
                return;
            }
        }
        return;
    }
    /**
     * Switch to the previous mode.
     */
    public void previousMode() {
        Mode[] modes = Mode.values();
        for (int i = 0; i < modes.length; i++) {
            if (mode == modes[i]) {
                mode = modes[(i - 1) % modes.length];
                return;
            }
        }
        return;
    }
}
