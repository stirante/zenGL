package com.stirante.opengl.util;

import static org.lwjgl.opengl.GL11.*;

public class Ray {

    private final Vector3f origin;
    private final Vector3f direction;

    public Ray(Vector3f origin, Vector3f direction) {
        this.origin = origin;
        this.direction = direction;
    }

    public Vector3f getOrigin() {
        return origin;
    }

    public Vector3f getDirection() {
        return direction;
    }

    public void gl() {
//        glColor3f(1f, 1f, 1f);
//        glBegin(GL_LINE_LOOP);
//        glVertex3f(origin.x, origin.y, origin.z);
//        glVertex3f(origin.x + (1 * direction.x), origin.y + (1 * direction.y), origin.z + (1 * direction.z));
//        glEnd();
    }
}
