package com.stirante.opengl.util;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.glNormal3f;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex3f;

public class PolyUtils {

    public static Vector3f getNormal(Vector3f p1, Vector3f p2, Vector3f p3) {
        Vector3f output = new Vector3f();

        Vector3f calU = new Vector3f(p2.x - p1.x, p2.y - p1.y, p2.z - p1.z);
        Vector3f calV = new Vector3f(p3.x - p1.x, p3.y - p1.y, p3.z - p1.z);

        output.x = calU.y * calV.z - calU.z * calV.y;
        output.y = calU.z * calV.x - calU.x * calV.z;
        output.z = calU.x * calV.y - calU.y * calV.x;

        return output.normalise();
    }

    public static ArrayList<ArrayList<Vector3f>> toStrips(float[][] map) {
        ArrayList<ArrayList<Vector3f>> strips = new ArrayList<>();
        for (int x = 0; x < map.length - 1; x++) {
            ArrayList<Vector3f> e = new ArrayList<>();
            strips.add(e);
            for (int z = 0; z < map[x].length; z++) {
                e.add(new Vector3f(x, map[x][z], -z));
                e.add(new Vector3f(x + 1, map[x + 1][z], -z));
            }
        }
        return strips;
    }

    public static void poly(ArrayList<Float> data, Vector3f v0, Vector3f v1, Vector3f v2) {
        Vector3f normal = getNormal(new Vector3f(v0.x, v0.y, v0.z), new Vector3f(v1.x, v1.y, v1.z), new Vector3f(v2.x, v2.y, v2.z));

        put(data, v0.x, v0.y, v0.z);
        put(data, v0.x, v0.z);
        put(data, normal);

        put(data, v1.x, v1.y, v1.z);
        put(data, v1.x, v1.z);
        put(data, normal);

        put(data, v2.x, v2.y, v2.z);
        put(data, v2.x, v2.z);
        put(data, normal);
    }

    public static void poly(Vector3f v0, Vector3f v1, Vector3f v2) {
        Vector3f normal = getNormal(new Vector3f(v0.x, v0.y, v0.z), new Vector3f(v1.x, v1.y, v1.z), new Vector3f(v2.x, v2.y, v2.z));

        glNormal3f(normal.x, normal.y, normal.z);
        glTexCoord2f(v0.x, v0.z);
        glVertex3f(v0.x, v0.y, v0.z);

        glNormal3f(normal.x, normal.y, normal.z);
        glTexCoord2f(v1.x, v1.z);
        glVertex3f(v1.x, v1.y, v1.z);

        glNormal3f(normal.x, normal.y, normal.z);
        glTexCoord2f(v2.x, v2.z);
        glVertex3f(v2.x, v2.y, v2.z);
    }

    private static void put(ArrayList<Float> buffer, float... floats) {
        for (float f : floats) {
            buffer.add(f);
        }
    }

    private static void put(ArrayList<Float> buffer, Vector3f v) {
        buffer.add(v.x);
        buffer.add(v.y);
        buffer.add(v.z);
    }

}
