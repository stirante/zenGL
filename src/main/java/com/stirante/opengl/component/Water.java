package com.stirante.opengl.component;

import com.stirante.opengl.util.PolyUtils;
import com.stirante.opengl.util.Vector3f;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

public class Water implements GLComponent {

    public static final double FORCE_MULTIPLIER = 0.005;
    public static final double DAMPING = 0.99;
    private int width;
    private int height;
    private float[][] levels;
    private double[][] forces;
    private float baseLevel = 0;
    private float[][] randoms;
    private float[][] targetRandoms;
    private float[][] finalMap;
    private float randomStep = 60;

    private float a = 0;

    public Water(int width, int height) {
        this.width = width;
        this.height = height;
        levels = new float[width][height];
        forces = new double[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                forces[i][j] = 0;
            }
        }
        randoms = new float[width][height];
        targetRandoms = new float[width][height];
        finalMap = new float[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                targetRandoms[i][j] = (float) Math.random()/8f;
            }
        }
        recalculate();
    }

    private void recalculate() {
        if (randomStep >= 60) {
            randomStep = 0;
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    randoms[i][j] = targetRandoms[i][j];
                    targetRandoms[i][j] = (float) Math.random()/8f;
                }
            }
        } else {
            randomStep++;
        }
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                float level = levels[i][j];
                float force = 0;
                if (i > 0) force += FORCE_MULTIPLIER * (levels[i - 1][j] - level);
                if (i < width-1) force += 0.005 * (levels[i + 1][j] - level);
                if (j > 0) force += 0.005 * (levels[i][j - 1] - level);
                if (j < height-1) force += 0.005 * (levels[i][j + 1] - level);
                force += 0.005 * (baseLevel - level);
                forces[i][j] = (DAMPING * forces[i][j]) + force;
                levels[i][j] += forces[i][j];
                finalMap[i][j] = levels[i][j] + (randoms[i][j] + ((targetRandoms[i][j] - randoms[i][j]) * (randomStep/60f)));
            }
        }
//        for (int i = 0; i < 200; i++) {
//            for (int j = 0; j < 200; j++) {
//                levels[i][j] = ((float) Math.sin((i + a) / 10f) / 4f) +
//                        ((float) Math.sin((i + a) / 5f) / 10f) +
//                        (randoms[i][j] + ((targetRandoms[i][j] - randoms[i][j]) * (randomStep/60f)));
//            }
//        }
    }

    @Override
    public void initGL() {

    }

    public void setPointLevel(int x, int z, float level) {
        levels[x][z] = level;
    }

    @Override
    public void renderGL() {
        ArrayList<ArrayList<Vector3f>> strip = PolyUtils.toStrips(finalMap);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glColor4f(0.22745098f, 0.65882355f, 1f, 0.5f);
        glBegin(GL_TRIANGLES);
        for (int s = 0; s < strip.size(); s++) {
            for (int v = 0; v < strip.get(s).size() - 2; v++) {
                Vector3f v0 = strip.get(s).get(v);
                Vector3f v1 = strip.get(s).get(v + 1);
                Vector3f v2 = strip.get(s).get(v + 2);
                if ((v & 1) != 0) {
                    PolyUtils.poly(v0, v2, v1);
                } else {
                    PolyUtils.poly(v0, v1, v2);
                }
            }
        }
        glEnd();
        glDisable(GL_BLEND);
        glColor4f(1f, 1f, 1f, 1f);
    }

    @Override
    public void destroyGL() {

    }

    @Override
    public void update() {
        a += 0.2f;
        if (a > 200) a = 0;
        recalculate();
        recalculate();
    }
}
