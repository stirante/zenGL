package com.stirante.opengl.util;

import org.lwjgl.opengl.EXTTextureFilterAnisotropic;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL30.*;

public class CubeTexture {
    private int width, height;
    private int texture;

    public CubeTexture(String basePath) {
        texture = load(basePath);
    }

    private IntBuffer loadImage(String path) {
        try {
            BufferedImage image = ImageIO.read(new FileInputStream(path));
            width = image.getWidth();
            height = image.getHeight();
            int[] pixels = new int[width * height];
            image.getRGB(0, 0, width, height, pixels, 0, width);
            return BufferUtils.createIntBuffer(pixels);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private int load(String basePath) {
        IntBuffer xn = loadImage(basePath + "_xn.png");
        IntBuffer xp = loadImage(basePath + "_xp.png");
        IntBuffer yn = loadImage(basePath + "_yn.png");
        IntBuffer yp = loadImage(basePath + "_yp.png");
        IntBuffer zn = loadImage(basePath + "_zn.png");
        IntBuffer zp = loadImage(basePath + "_zp.png");

        int result = glGenTextures();
        glEnable(GL_TEXTURE_CUBE_MAP);
        glBindTexture(GL_TEXTURE_CUBE_MAP, result);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);

        glTexImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_X, 0, GL_RGBA, width, height, 0, GL_BGRA, GL_UNSIGNED_BYTE, xn);
        glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X, 0, GL_RGBA, width, height, 0, GL_BGRA, GL_UNSIGNED_BYTE, xp);
        glTexImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, 0, GL_RGBA, width, height, 0, GL_BGRA, GL_UNSIGNED_BYTE, yn);
        glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_Y, 0, GL_RGBA, width, height, 0, GL_BGRA, GL_UNSIGNED_BYTE, yp);
        glTexImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, 0, GL_RGBA, width, height, 0, GL_BGRA, GL_UNSIGNED_BYTE, zn);
        glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_Z, 0, GL_RGBA, width, height, 0, GL_BGRA, GL_UNSIGNED_BYTE, zp);

        glGenerateMipmap(GL_TEXTURE_CUBE_MAP);
        glTexParameterf(GL_TEXTURE_CUBE_MAP, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, 16.0f);
        glBindTexture(GL_TEXTURE_CUBE_MAP, 0);
        glDisable(GL_TEXTURE_CUBE_MAP);
        return result;
    }

    public void bind() {
        glEnable(GL_TEXTURE_CUBE_MAP);
        glBindTexture(GL_TEXTURE_CUBE_MAP, texture);
    }

    public void unbind() {
        glDisable(GL_TEXTURE_CUBE_MAP);
        glBindTexture(GL_TEXTURE_CUBE_MAP, 0);
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
