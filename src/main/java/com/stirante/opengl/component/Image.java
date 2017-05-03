package com.stirante.opengl.component;

import com.stirante.opengl.util.Texture;

import java.awt.image.BufferedImage;

import static org.lwjgl.opengl.GL11.*;

public class Image implements GLComponent {

    private Texture texture;
    private float x = 0;
    private float y = 0;
    private float s1 = 0;
    private float t1 = 0;
    private float s2 = 1;
    private float t2 = 1;
    private float width;
    private float height;

    public Image(String path) {
        texture = new Texture(path);
    }

    public Image(BufferedImage bufferedImage) {
        texture = new Texture(bufferedImage);
    }

    public Image(Texture texture) {
        this.texture = texture;
    }

    @Override
    public void initGL() {
        width = texture.getWidth();
        height = texture.getHeight();
    }

    @Override
    public void renderGL() {
        texture.bind();
        glBegin(GL_QUADS);
        glTexCoord2f(s1, t1);
        glVertex2f(x, y);
        glTexCoord2f(s2, t1);
        glVertex2f(x + width, y);
        glTexCoord2f(s2, t2);
        glVertex2f(x + width, y + height);
        glTexCoord2f(s1, t2);
        glVertex2f(x, y + height);
        glEnd();
        texture.unbind();
    }

    @Override
    public void destroyGL() {
        texture.destroy();
    }

    @Override
    public void update() {

    }

    public void setVertexCoord(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setTextureCoordFrom(float s, float t) {
        this.s1 = s;
        this.t1 = t;
    }

    public void setTextureCoordTo(float s, float t) {
        this.s2 = s;
        this.t2 = t;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public Texture getTexture() {
        return texture;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}
