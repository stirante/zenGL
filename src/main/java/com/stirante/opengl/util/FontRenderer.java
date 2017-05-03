package com.stirante.opengl.util;

import com.stirante.opengl.component.Text;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;

public class FontRenderer {
    private static final float WIDTH = 16;
    private static final float HEIGHT = 32;
    private static final char START = 32;

    private final Texture texture;

    public FontRenderer() {
        texture = new Texture("res/font.png");
    }

    public Text getText(String text, float x, float y, float height) {
        float cx = x;
        float cy = y;
        int size = 0;
        ArrayList<Float> data = new ArrayList<>();
        float width = (WIDTH / HEIGHT) * height;
        for (int i = 0; i < text.length(); i++) {
            int c = text.charAt(i) - START;
            float row = c / 32;
            float column = c % 32;

            data.add(cx);
            data.add(cy);

            data.add((column * WIDTH)/512);
            data.add((row * HEIGHT)/256);

            data.add(cx + width);
            data.add(cy);

            data.add(((column + 1) * WIDTH)/512);
            data.add((row * HEIGHT)/256);

            data.add(cx + width);
            data.add(cy + height);

            data.add(((column + 1) * WIDTH)/512);
            data.add(((row + 1) * HEIGHT)/256);

            data.add(cx);
            data.add(cy + height);

            data.add((column * WIDTH)/512);
            data.add(((row + 1) * HEIGHT)/256);

            size++;
            cx += width;
        }
        FloatBuffer buff = org.lwjgl.BufferUtils.createFloatBuffer(data.size());
        for (Float f : data) {
            buff.put(f);
        }
        buff.flip();
        int vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, buff, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        return new Text(vbo, size, texture, cx-x, height);
    }

    public void drawText(String text, float x, float y, float height) {
        glEnable(GL_BLEND);
        texture.bind();
        float cx = x;
        float cy = y;
        float width = (WIDTH / HEIGHT) * height;
        for (int i = 0; i < text.length(); i++) {
            int c = text.charAt(i) - START;
            float row = c / 32;
            float column = c % 32;

            glBegin(GL_QUADS);

            glTexCoord2f((column * WIDTH) / 512, (row * HEIGHT) / 256);
            glVertex2f(cx, cy);

            glTexCoord2f(((column + 1) * WIDTH) / 512, (row * HEIGHT) / 256);
            glVertex2f(cx + width, cy);

            glTexCoord2f(((column + 1) * WIDTH) / 512, ((row + 1) * HEIGHT) / 256);
            glVertex2f(cx + width, cy + height);

            glTexCoord2f((column * WIDTH) / 512, ((row + 1) * HEIGHT) / 256);
            glVertex2f(cx, cy + height);

            glEnd();

            cx += width;
        }
        texture.unbind();
        glDisable(GL_BLEND);
    }

}
