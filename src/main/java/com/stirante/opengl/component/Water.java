package com.stirante.opengl.component;

import com.stirante.opengl.util.CubeTexture;
import com.stirante.opengl.util.PolyUtils;
import com.stirante.opengl.util.Vector3f;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_REFLECTION_MAP;

public class Water implements GLComponent {

    private static final double FORCE_MULTIPLIER = 0.005;
    private static final double DAMPING = 0.99;
    private int width;
    private int height;
    private float[][] levels;
    private double[][] forces;
    private float baseLevel = 0;
    private float[][] randoms;
    private float[][] targetRandoms;
    private float[][] finalMap;
    private float randomStep = 60;
    private ArrayList<ArrayList<Vector3f>> strip;
    private CubeTexture texture;

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
                targetRandoms[i][j] = (float) Math.random() / 8f;
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
                    targetRandoms[i][j] = (float) Math.random() / 8f;
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
                if (i < width - 1) force += 0.005 * (levels[i + 1][j] - level);
                if (j > 0) force += 0.005 * (levels[i][j - 1] - level);
                if (j < height - 1) force += 0.005 * (levels[i][j + 1] - level);
                force += 0.005 * (baseLevel - level);
                forces[i][j] = (DAMPING * forces[i][j]) + force;
                levels[i][j] += forces[i][j];
            }
        }
    }

    @Override
    public void initGL() {
        texture = new CubeTexture("res/sky2");
    }

    public void setPointLevel(int x, int z, float level) {
        levels[x][z] = level;
    }

    @Override
    public void renderGL() {
        if (strip == null) return;
        glEnable(GL_REFLECTION_MAP);
        glEnable(GL_TEXTURE_GEN_S);
        glEnable(GL_TEXTURE_GEN_T);
        glEnable(GL_TEXTURE_GEN_R);
        glEnable(GL_BLEND);
        texture.bind();
        glTexGeni(GL_S, GL_TEXTURE_GEN_MODE, GL_REFLECTION_MAP);
        glTexGeni(GL_T, GL_TEXTURE_GEN_MODE, GL_REFLECTION_MAP);
        glTexGeni(GL_R, GL_TEXTURE_GEN_MODE, GL_REFLECTION_MAP);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
//        glColor4f(0.22745098f, 0.65882355f, 1f, 0.5f);
        glColor4f(0.42745098f, 0.85882355f, 1f, 0.8f);
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
        texture.unbind();
        glDisable(GL_BLEND);
        glDisable(GL_TEXTURE_GEN_S);
        glDisable(GL_TEXTURE_GEN_T);
        glDisable(GL_TEXTURE_GEN_R);
        glDisable(GL_REFLECTION_MAP);
        glColor4f(1f, 1f, 1f, 1f);
    }

    @Override
    public void destroyGL() {

    }

    @Override
    public void update() {
        recalculate();
        recalculate();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                finalMap[i][j] = levels[i][j] + (randoms[i][j] + ((targetRandoms[i][j] - randoms[i][j]) * (randomStep / 60f)));
            }
        }
        strip = PolyUtils.toStrips(finalMap);
    }
}
