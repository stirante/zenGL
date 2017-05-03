package com.stirante.opengl.component;

import com.stirante.opengl.util.Texture;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_VERTEX_ARRAY;
import static org.lwjgl.opengl.GL11.glDisableClientState;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;

public class Text implements GLComponent {
    private final int vbo;
    private int size;
    private final Texture texture;
    private final float width;
    private final float height;

    public Text(int vbo, int size, Texture font, float width, float height) {
        this.vbo = vbo;
        this.size = size;
        this.texture = font;
        this.width = width;
        this.height = height;
    }

    @Override
    public void initGL() {

    }

    @Override
    public void renderGL() {
        glEnable(GL_BLEND);
        texture.bind();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glVertexPointer(2, GL_FLOAT, 4 * 4, 0);
        glTexCoordPointer(2, GL_FLOAT, 4 * 4, 4 * 2);

        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        for (int i = 0; i < size; i++) {
            glDrawArrays(GL_QUADS, i * 4, 4);
        }
        glDisableClientState(GL_TEXTURE_COORD_ARRAY);
        glDisableClientState(GL_VERTEX_ARRAY);
        glDisable(GL_BLEND);
    }

    @Override
    public void destroyGL() {
        glDeleteBuffers(vbo);
    }

    @Override
    public void update() {

    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
