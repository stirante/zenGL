package com.stirante.opengl.component;

import com.stirante.opengl.util.Texture;
import com.stirante.opengl.util.SimplexNoise;
import com.stirante.opengl.util.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

public class World implements GLComponent {

    private float[][] noise;
    private int[] vbos;
    private int size;

    private Texture grass;
    private Texture sea;

    public World() {
        SimplexNoise sn = new SimplexNoise(100, 0.2, System.currentTimeMillis());
        double xStart = 0;
        double XEnd = 200;
        double yStart = 0;
        double yEnd = 200;

        int xResolution = 200;
        int yResolution = 200;

        noise = new float[xResolution][yResolution];

        for (int i = 0; i < xResolution; i++) {
            for (int j = 0; j < yResolution; j++) {
                int x = (int) (xStart + i * ((XEnd - xStart) / xResolution));
                int y = (int) (yStart + j * ((yEnd - yStart) / yResolution));
                noise[i][j] = (float) (1 + (sn.getNoise(x, y) * 30f));
            }
        }
    }

    public void initGL() {
        //broken lighting
        float[] lightPosition = {-2.19f, 1.36f, 11.45f, 1f};
        glEnable(GL_LIGHTING);
        glEnable(GL_LIGHT0);
        glLightf(GL_LIGHT0, GL_AMBIENT, 1f);
        glLightfv(GL_LIGHT0, GL_POSITION, lightPosition);

        grass = new Texture("res/grass.jpg");
        sea = new Texture("res/sea.jpg");
        ArrayList<ArrayList<Vector3f>> strip = new ArrayList<>();
        for (int x = 0; x < noise.length - 1; x++) {
            ArrayList<Vector3f> e = new ArrayList<>();
            strip.add(e);
            for (int z = 0; z < noise[x].length; z++) {
                e.add(new Vector3f(x, noise[x][z], -z));
                e.add(new Vector3f(x + 1, noise[x + 1][z], -z));
            }
        }
        vbos = new int[strip.size()];
        for (int s = 0; s < strip.size(); s++) {
            ArrayList<Float> data = new ArrayList<>();
            size = 0;
            for (int v = 0; v < strip.get(s).size() - 2; v++) {
                if ((v & 1) != 0) {
                    Vector3f normal = getNormal(new Vector3f(strip.get(s).get(v).x, strip.get(s).get(v).y, strip.get(s).get(v).z), new Vector3f(strip.get(s).get(v + 2).x, strip.get(s).get(v + 2).y, strip.get(s).get(v + 2).z), new Vector3f(strip.get(s).get(v + 1).x, strip.get(s).get(v + 1).y, strip.get(s).get(v + 1).z));

                    put(data, strip.get(s).get(v).x, strip.get(s).get(v).y, strip.get(s).get(v).z);
                    put(data, strip.get(s).get(v).x, strip.get(s).get(v).z);
                    put(data, normal);

                    put(data, strip.get(s).get(v + 2).x, strip.get(s).get(v + 2).y, strip.get(s).get(v + 2).z);
                    put(data, strip.get(s).get(v + 2).x, strip.get(s).get(v + 2).z);
                    put(data, normal);

                    put(data, strip.get(s).get(v + 1).x, strip.get(s).get(v + 1).y, strip.get(s).get(v + 1).z);
                    put(data, strip.get(s).get(v + 1).x, strip.get(s).get(v + 1).z);
                    put(data, normal);

                    size += 3;
                } else {
                    Vector3f normal = getNormal(new Vector3f(strip.get(s).get(v).x, strip.get(s).get(v).y, strip.get(s).get(v).z), new Vector3f(strip.get(s).get(v + 1).x, strip.get(s).get(v + 1).y, strip.get(s).get(v + 1).z), new Vector3f(strip.get(s).get(v + 2).x, strip.get(s).get(v + 2).y, strip.get(s).get(v + 2).z));

                    put(data, strip.get(s).get(v).x, strip.get(s).get(v).y, strip.get(s).get(v).z);
                    put(data, strip.get(s).get(v).x, strip.get(s).get(v).z);
                    put(data, normal);

                    put(data, strip.get(s).get(v + 1).x, strip.get(s).get(v + 1).y, strip.get(s).get(v + 1).z);
                    put(data, strip.get(s).get(v + 1).x, strip.get(s).get(v + 1).z);
                    put(data, normal);

                    put(data, strip.get(s).get(v + 2).x, strip.get(s).get(v + 2).y, strip.get(s).get(v + 2).z);
                    put(data, strip.get(s).get(v + 2).x, strip.get(s).get(v + 2).z);
                    put(data, normal);

                    size += 3;

                }
            }
            FloatBuffer buff = BufferUtils.createFloatBuffer(data.size());
            for (Float f : data) {
                buff.put(f);
            }
            buff.flip();
            int vbo = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glBufferData(GL_ARRAY_BUFFER, buff, GL_STATIC_DRAW);
//            glVertexAttribPointer(Shader.VERTEX_ATTRIB, 3, GL_FLOAT, false, 32, 0);
//            glEnableVertexAttribArray(Shader.VERTEX_ATTRIB);
//            glVertexAttribPointer(Shader.TCOORD_ATTRIB, 2, GL_FLOAT, false, 32, 3 * 4);
//            glEnableVertexAttribArray(Shader.TCOORD_ATTRIB);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            vbos[s] = vbo;
        }
    }

    @Override
    public void renderGL() {
        grass.bind();
        for (int vbo : vbos) {
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
        }
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        grass.unbind();
//        glColor3f(1f, 0f, 0f);
//        final float y = 0f;
//        for (int i = 0; i < 200; i++) {
//            glBegin(GL_LINE_LOOP);
//            glVertex3f(i, y, 0f);
//            glVertex3f(i, y, -200f);
//            glEnd();
//            glBegin(GL_LINE_LOOP);
//            glVertex3f(0f, y, -i);
//            glVertex3f(200f, y, -i);
//            glEnd();
//        }
//        glColor3f(1f, 1f, 1f);
        sea.bind();
        glBegin(GL_QUADS);
        glTexCoord2f(0f, 0f);
        glNormal3f(0, 1, 0);
        glVertex3f(0f, 1f, 0f);

        glTexCoord2f(0f, 200f);
        glNormal3f(0, 1, 0);
        glVertex3f(0f, 1f, -200f);

        glTexCoord2f(200f, 200f);
        glNormal3f(0, 1, 0);
        glVertex3f(200f, 1f, -200f);

        glTexCoord2f(200f, 0f);
        glNormal3f(0, 1, 0);
        glVertex3f(200f, 1f, 0f);

        glEnd();
        sea.unbind();
    }

    @Override
    public void destroyGL() {
        sea.destroy();
        grass.destroy();
    }

    @Override
    public void update() {

    }

    public Vector3f getNormal(Vector3f p1, Vector3f p2, Vector3f p3) {
        Vector3f output = new Vector3f();

        Vector3f calU = new Vector3f(p2.x - p1.x, p2.y - p1.y, p2.z - p1.z);
        Vector3f calV = new Vector3f(p3.x - p1.x, p3.y - p1.y, p3.z - p1.z);

        output.x = calU.y * calV.z - calU.z * calV.y;
        output.y = calU.z * calV.x - calU.x * calV.z;
        output.z = calU.x * calV.y - calU.y * calV.x;

        return output.normalise();
    }

    private void put(ArrayList<Float> buffer, float... floats) {
        for (float f : floats) {
            buffer.add(f);
        }
    }

    private void put(ArrayList<Float> buffer, Vector3f v) {
        buffer.add(v.x);
        buffer.add(v.y);
        buffer.add(v.z);
    }

}
