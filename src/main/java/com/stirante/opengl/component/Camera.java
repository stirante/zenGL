package com.stirante.opengl.component;

import com.stirante.opengl.input.Keyboard;
import com.stirante.opengl.input.MouseListener;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Camera implements GLComponent, MouseListener {

    private float x, y, z;
    private float rx, ry, rz;
    private float fov, aspect, near, far;

    public Camera(float fov, float aspect, float near, float far) {
        x = z = 0;
        y = -20;
        rx = 0;
        ry = 0;
        rz = 0;

        this.fov = fov;
        this.aspect = aspect;
        this.near = near;
        this.far = far;
    }

    public void setAspect(float aspect) {
        this.aspect = aspect;
    }

    private void initProjection() {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        gluPerspective(fov, aspect, near, far);
        glMatrixMode(GL_MODELVIEW);
    }

    public void setView() {
        glLoadIdentity();
        glRotatef(rx, 1, 0, 0);
        glRotatef(ry, 0, 1, 0);
        glRotatef(rz, 0, 0, 1);
        glTranslatef(x, y, z);
    }

    private float speed1 = 1f;
    private float speed2 = 0.5f;

    public void move(float locSpeed, float dir) {
        double rad = Math.toRadians(ry + 90 * dir);
        z += locSpeed * Math.sin(rad);
        x += locSpeed * Math.cos(rad);
        if (dir != 0) {
            y += locSpeed * Math.sin(Math.toRadians(rx));
        }
    }

    public void moveForward() {
        move(speed1, 1);
    }

    public void moveBackward() {
        move(-speed1, 1);
    }

    public void moveRight() {
        move(-speed2, 0);
    }

    public void moveLeft() {
        move(speed2, 0);
    }

    public void rotateY(float amt) {
        ry += amt;
    }

    public void rotateX(float amt) {
        rx += amt;
    }

    private static void gluPerspective(float fov, float aspect, float near, float far) {
        FloatBuffer matrix = BufferUtils.createFloatBuffer(16);
        float sine, cotangent, deltaZ;
        float radians = (float) (fov / 2 * Math.PI / 180);

        deltaZ = far - near;
        sine = (float) Math.sin(radians);

        if ((deltaZ == 0) || (sine == 0) || (aspect == 0)) {
            return;
        }

        cotangent = (float) Math.cos(radians) / sine;

        matrix.put(0 * 4 + 0, cotangent / aspect);
        matrix.put(1 * 4 + 1, cotangent);
        matrix.put(2 * 4 + 2, -(far + near) / deltaZ);
        matrix.put(2 * 4 + 3, -1);
        matrix.put(3 * 4 + 2, -2 * near * far / deltaZ);
        matrix.put(3 * 4 + 3, 0);

        glMultMatrixf(matrix);
    }

    @Override
    public void initGL() {
        initProjection();
    }

    @Override
    public void renderGL() {
        setView();
    }

    @Override
    public void destroyGL() {

    }

    @Override
    public void update() {
        if (Keyboard.isKeyDown(GLFW_KEY_W)) {
            moveForward();
        }
        if (Keyboard.isKeyDown(GLFW_KEY_S)) {
            moveBackward();
        }
        if (Keyboard.isKeyDown(GLFW_KEY_A)) {
            moveLeft();
        }
        if (Keyboard.isKeyDown(GLFW_KEY_D)) {
            moveRight();
        }
        if (Keyboard.isKeyDown(GLFW_KEY_Q)) {
            rotateY(-0.75f);
        }
        if (Keyboard.isKeyDown(GLFW_KEY_E)) {
            rotateY(0.75f);
        }
        if (Keyboard.isKeyDown(GLFW_KEY_R)) {
            rotateX(-0.75f);
        }
        if (Keyboard.isKeyDown(GLFW_KEY_F)) {
            rotateX(0.75f);
        }
    }

    private boolean pressed = false;
    private double lastX = 0;
    private double lastY = 0;

    @Override
    public void onMouseMove(double x, double y) {
        if (pressed) {
            rotateX((float) (y - lastY));
            rotateY((float) (x - lastX));
        }
        lastX = x;
        lastY = y;
    }

    @Override
    public void onMousePress(MouseButton button) {
        if (button == MouseButton.LEFT) pressed = true;
    }

    @Override
    public void onMouseRelease(MouseButton button) {
        if (button == MouseButton.LEFT) pressed = false;
    }
}
