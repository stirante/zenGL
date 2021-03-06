package com.stirante.opengl.util;

import org.lwjgl.opengl.EXTTextureFilterAnisotropic;
import org.lwjgl.opengl.GL12;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL30.*;

public class Texture {
    private int width, height;
    private int texture;

    public Texture(String path) {
        texture = load(path);
    }

    public Texture(BufferedImage img) {
        texture = load(img);
    }

    private int load(String path) {
        try {
            BufferedImage image = ImageIO.read(new FileInputStream(path));
            return load(image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private int load(BufferedImage image) {
        int[] pixels;
        width = image.getWidth();
        height = image.getHeight();
        pixels = new int[width * height];
        image.getRGB(0, 0, width, height, pixels, 0, width);

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
