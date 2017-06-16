package com.stirante.opengl.component;

import com.stirante.opengl.input.Keyboard;
import com.stirante.opengl.util.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;

public class World implements GLComponent {

    private float[][] noise;
    private int vbo = 0;
    private int size;
    private Water water;
    private Camera camera;
    private int width;
    private int height;

    private Texture grass;

//    private Shader shader;
//    private GreyNoise noiseTex;
//
//    private long startTime;

    public World(Camera camera, int width, int height) {
//        startTime = System.currentTimeMillis();
        this.camera = camera;
        this.width = width;
        this.height = height;
        water = new Water(width, height);
        SimplexNoise sn = new SimplexNoise(100, 0.2, System.currentTimeMillis());
        double xStart = 0;
        double yStart = 0;

        noise = new float[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int x = (int) (xStart + i * (((double) width - xStart) / width));
                int y = (int) (yStart + j * (((double) height - yStart) / height));
                noise[i][j] = (float) (1 + (sn.getNoise(x, y) * 30f));
            }
        }
        camera.setWorld(this);
    }

    public void initGL() {
//        noiseTex = new GreyNoise(2048, 2048);
//        shader = new Shader("res/shaders/test");
//        shader.setUniform1i("tex", 0);
//        shader.setUniform1i("noise", 1);
//        glActiveTexture(GL_TEXTURE1);
//        noiseTex.bind();
//        glActiveTexture(GL_TEXTURE0);
        water.initGL();
        grass = new Texture("res/grass.jpg");
        ArrayList<ArrayList<Vector3f>> strip = PolyUtils.toStrips(noise);
        size = 0;
        ArrayList<Float> data = new ArrayList<>();
        for (ArrayList<Vector3f> aStrip : strip) {
            for (int v = 0; v < aStrip.size() - 2; v++) {
                Vector3f v0 = aStrip.get(v);
                Vector3f v1 = aStrip.get(v + 1);
                Vector3f v2 = aStrip.get(v + 2);
                if ((v & 1) != 0) {
                    PolyUtils.poly(data, v0, v2, v1);
                } else {
                    PolyUtils.poly(data, v0, v1, v2);
                }
                size += 3;
            }
        }
        FloatBuffer buff = BufferUtils.createFloatBuffer(data.size());
        for (Float f : data) {
            buff.put(f);
        }
        buff.flip();
        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, buff, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public void onPlayerMove(float x, float y, float z) {
        float bot = getBottomAt(x, z) + 1;
        if (camera.getY() < bot) camera.ground(bot);
        if (y <= 1 && y >= 0)
            water.setPointLevel((int) x, (int) z, -0.9f);
        else if (y < 0)
            water.setPointLevel((int) x, (int) z, (-0.9f) * (-(y + 1) / 10f));
        if (camera.getZ() > getHeight() - 1) camera.setZ(getHeight() - 1);
        if (camera.getZ() < 1) camera.setZ(1);
        if (camera.getX() > getWidth() - 1) camera.setX(getWidth() - 1);
        if (camera.getX() < 1) camera.setX(1);
    }

    @Override
    public void renderGL() {
//        float value = (float) (System.currentTimeMillis() - startTime) / 1000f;
//        shader.setUniform1f("globalTime", value);
//        glActiveTexture(GL_TEXTURE1);
//        noiseTex.bind();
//        glActiveTexture(GL_TEXTURE0);
//        shader.enable();
        grass.bind();
        if (camera.getY() > 0)
            glColor3f(1, 1, 1);
        else
            glColor3f(0.3f, 0.4f, 1);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glVertexPointer(3, GL_FLOAT, 8 * 4, 0);
        glTexCoordPointer(2, GL_FLOAT, 8 * 4, 4 * 3);
        glNormalPointer(GL_FLOAT, 8 * 4, 4 * 5);

        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        glEnableClientState(GL_NORMAL_ARRAY);
        glDrawArrays(GL_TRIANGLES, 0, size);
        glDisableClientState(GL_NORMAL_ARRAY);
        glDisableClientState(GL_TEXTURE_COORD_ARRAY);
        glDisableClientState(GL_VERTEX_ARRAY);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glColor3f(1f, 1f, 1f);
        grass.unbind();
        water.renderGL();
//        shader.disable();
    }

    @Override
    public void destroyGL() {
        grass.destroy();
        water.destroyGL();
    }

    @Override
    public void update() {
        if (Keyboard.isKeyDown(GLFW.GLFW_KEY_C)) water.setPointLevel((int) camera.getX(), (int) camera.getZ(), 10);
        water.update();
    }

    public float getBottomAt(float x, float z) {
        if (x < 0 || z < 0) return 0;
        float y1 = noise[(int) x][(int) z];
        float y2 = noise[(int) x + 1][(int) z];
        float y3 = noise[(int) x][(int) z + 1];
        float y4 = noise[(int) x + 1][(int) z + 1];
        float xDiff = x - (int) x;
        float zDiff = z - (int) z;
        float y12 = y1 + ((y2 - y1) * xDiff);
        float y34 = y3 + ((y4 - y3) * xDiff);
        return y12 + ((y34 - y12) * zDiff);
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
