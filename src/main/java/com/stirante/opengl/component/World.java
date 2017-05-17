package com.stirante.opengl.component;

import com.stirante.opengl.input.Keyboard;
import com.stirante.opengl.util.PolyUtils;
import com.stirante.opengl.util.Texture;
import com.stirante.opengl.util.SimplexNoise;
import com.stirante.opengl.util.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

public class World implements GLComponent {

    private float[][] noise;
    private int vbo = 0;
    private int size;
    private Water water;
    private Camera camera;
    private int width;
    private int height;

//    private Texture grass;
//    private Texture sea;

    public World(Camera camera, int width, int height) {
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
        water.initGL();
//        grass = new Texture("res/grass.jpg");
//        sea = new Texture("res/sea.jpg");
        //wygenerowanie pasków mapy. Wcześniej renderowałem używając TRIANLGE_STRIP i tak zostało
        ArrayList<ArrayList<Vector3f>> strip = PolyUtils.toStrips(noise);
        //tablica id bufferów
        //dla każdego paska robimy vertex buffer trójkątów
        size = 0;
        ArrayList<Float> data = new ArrayList<>();
        for (int s = 0; s < strip.size(); s++) {
            for (int v = 0; v < strip.get(s).size() - 2; v++) {
                Vector3f v0 = strip.get(s).get(v);
                Vector3f v1 = strip.get(s).get(v + 1);
                Vector3f v2 = strip.get(s).get(v + 2);
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
        if (y <= 1 && y >= 0)
            water.setPointLevel((int) x, (int) z, -0.8f);
        else if (y < 0)
            water.setPointLevel((int) x, (int) z, -0.8f * (-(y + 1) / 10f));
        //iluzja kolizji (ustawiam caly czas Y kamery na najnizszy punkt na mapie)
        camera.setY(getBottomAt(camera.getX(), camera.getZ()) + 1f);
        if (camera.getZ() > getHeight()-1)  camera.setZ(getHeight() - 1);
        if (camera.getZ() < 1)  camera.setZ(1);
        if (camera.getX() > getWidth()-1)  camera.setX(getWidth() - 1);
        if (camera.getX() < 1)  camera.setX(1);
    }

    @Override
    public void renderGL() {
//        grass.bind();
        //renderowanie mapy
        if (camera.getY() > 0)
            glColor3f(0.54509807f, 0.7647059f, 0.2901961f);
        else
            glColor3f(0.24509807f, 0.4647059f, 0.2901961f);
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
//        grass.unbind();
//        for (int i = 0; i < test.size(); i += 2) {
//            glBegin(GL_LINE_LOOP);
//            test.get(i).gl();
//            test.get(i + 1).gl();
//            glEnd();
//        }
        water.renderGL();
    }

    @Override
    public void destroyGL() {
//        sea.destroy();
//        grass.destroy();
        water.destroyGL();
    }

    @Override
    public void update() {
        if (Keyboard.isKeyDown(GLFW.GLFW_KEY_SPACE)) water.setPointLevel((int)camera.getX(), (int) camera.getZ(), 10);
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
