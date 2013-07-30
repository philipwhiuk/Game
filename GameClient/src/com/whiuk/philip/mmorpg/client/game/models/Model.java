package com.whiuk.philip.mmorpg.client.game.models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

/**
 * Model.
 * @author Philip
 */
public final class Model {
    /**
     * Number of vertex in a quad.
     */
    private static final int QUAD_VERTEX_COUNT = 4;
    /**
     * Pre-loaded models.
     */
    private static Map<String, Model> models = new HashMap<String, Model>();
    /**
     * Colour.
     * @author Philip
     */
    private static final class RGB {
        /**
         * Red (0.0f -> 1.0f).
         */
        private float red;
        /**
         * Green (0.0f -> 1.0f).
         */
        private float green;
        /**
         * Blue (0.0f -> 1.0f).
         */
        private float blue;
        /**
         * Constructor.
         * @param r red
         * @param g green
         * @param b blue
         */
        RGB(final float r, final float g, final float b) {
            this.red = r;
            this.green = g;
            this.blue = b;
        }
        /**
         * Render the colour.
         */
        private void render() {
            org.lwjgl.opengl.GL11.glColor3f(red, green, blue);
        }
    }
    /**
     * Vertex.
     * @author Philip
     *
     */
    private static final class Vertex {
        /**
         * X co-ordinate.
         */
        private float x;
        /**
         * Y co-ordinate.
         */
        private float y;
        /**
         * Z co-ordinate.
         */
        private float z;
        /**
         * Constructor.
         * @param x x
         * @param y y
         * @param z z
         */
        private Vertex(final float x, final float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
        /**
         * 
         */
        private void render() {
            GL11.glVertex3f(x, y, z);
        }
    }

    /**
     * Quad.
     * @author Philip
     *
     */
    private static final class Quad {
        /**
         * Colour of quad.
         */
        private RGB color;
        /**
         * Vertices of quad.
         */
        private List<Vertex> vertex;
        /**
         * Constructor.
         */
        private Quad() {
            vertex = new ArrayList<Vertex>(QUAD_VERTEX_COUNT);
        }
        /**
         * Render the quad.
         */
        private void render() {
            color.render();
            for (Vertex v: vertex) {
                System.out.println("Rendering vertex x:"+v.x+", y:"+v.y+", z:"+v.z);
                v.render();
            }
        }
    }
    /**
     * List of quads for the model.
     */
    private List<Quad> quads;
    /**
     * Name of model.
     */
    @SuppressWarnings("unused")
    private String name;
    /**
     * Constructor for loading model.
     */
    private Model() {
        this.quads = new ArrayList<Quad>();
    }
    /**
     * Render the model.
     */
    public void render() {
        GL11.glBegin(GL11.GL_QUADS);
        for (Quad q: quads) {
            q.render();
        }
        GL11.glEnd();
    }
    /**
     * Reads a model file.
     * @param filename Filename
     * @return model
     */
    public static Model fromFile(final String filename) {
        if (models.containsKey(filename)) {
            return models.get(filename);
        }
        BufferedReader br;
        try {
            InputStream mis = Model.class.getResourceAsStream(filename);
            InputStreamReader isr = new InputStreamReader(mis, "UTF-8");
            br = new BufferedReader(isr);
            Model m = new Model();
            m.name = br.readLine();
            String[] l;
            int quadCount = Integer.parseInt(br.readLine());
            for (int i = 0; i < quadCount; i++) {
                Quad q = new Quad();
                l = br.readLine().split(",");
                RGB c = new RGB(Float.parseFloat(l[0]),
                        Float.parseFloat(l[1]), Float.parseFloat(l[2]));
                q.color = c;
                for (int j = 0; j < QUAD_VERTEX_COUNT; j++) {
                    l = br.readLine().split(",");
                    Vertex v = new Vertex(Float.parseFloat(l[0]),
                            Float.parseFloat(l[1]), Float.parseFloat(l[2]));
                    q.vertex.add(v);
                }
                m.quads.add(q);
            }
            models.put(filename, m);
        return m;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Error loading model from: "
                    + filename, e);
        } catch (IOException e) {
            throw new RuntimeException("Error loading model from: "
                    + filename, e);
        }
    }
}
