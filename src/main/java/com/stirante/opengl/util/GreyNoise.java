package com.stirante.opengl.util;

import org.lwjgl.opengl.EXTTextureFilterAnisotropic;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_BGRA;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public class GreyNoise {
    private int width;
    private int height;
    private int[] pixels;
    private int texture;

    public GreyNoise(int width, int height) {
        this.width = width;
        this.height = height;
        pixels = new int[width * height];
        for (int i = 0; i < width * height; i++) {
            int channel = (int) (Math.random() * 255);
            pixels[i] = 0xff000000 + (channel << 16) + (channel << 8) + channel;
        }
        this.texture = load();
    }

    private int load() {
        int result = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, result);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST_MIPMAP_LINEAR);
        IntBuffer intBuffer = BufferUtils.createIntBuffer(pixels);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_BGRA, GL_UNSIGNED_BYTE, intBuffer);
        glGenerateMipmap(GL_TEXTURE_2D);
        glTexParameterf(GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, 16.0f);
        glBindTexture(GL_TEXTURE_2D, 0);
        return result;
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, texture);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    private boolean destroyed;

    public void destroy() {
        if (!destroyed) {
            glDeleteTextures(texture);
            destroyed = true;
            texture = 0;
        }
    }

    @Override
    protected void finalize() throws Throwable {
        destroy();
        super.finalize();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
