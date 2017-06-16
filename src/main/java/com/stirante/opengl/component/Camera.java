package com.stirante.opengl.component;

import com.stirante.opengl.input.Keyboard;
import com.stirante.opengl.input.MouseListener;
import com.stirante.opengl.util.Ray;
import com.stirante.opengl.util.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Camera implements GLComponent, MouseListener {

    private float x, y, z;
    private float rx, ry, rz;
    private float fov, aspect, near, far;
    private float velX, velY, velZ;
    private World world;
    private boolean onGround = true;
    private final Skybox skybox;

    public boolean isFlying() {
        return flying;
    }

    public void setFlying(boolean flying) {
        this.flying = flying;
    }

    private boolean flying = false;
    private boolean mouseLocked = true;

    public Camera(float fov, float aspect, float near, float far) {
        x = z = y = velY = velX = velZ = rx = ry = rz = 0;
        this.fov = fov;
        this.aspect = aspect;
        this.near = near;
        this.far = far;
        skybox = new Skybox();
    }

    public Ray getRay() {
        return new Ray(new Vector3f(x, y, -z), new Vector3f(
                Math.cos(Math.toRadians(rz + 90)) * -Math.sin(Math.toRadians(-ry + 90)) * far,
                Math.cos(Math.toRadians(rz + 90)) * far,
                Math.cos(Math.toRadians(rz + 90)) * -Math.sin(Math.toRadians(-ry + 90)) * far).normalise());
    }

    public void setWorld(World world) {
        this.world = world;
        world.onPlayerMove(getX(), getY(), getZ());
    }

    public void setAspect(float aspect) {
        this.aspect = aspect;
        initProjection();
    }

    public void setFOV(float fov) {
        this.fov = fov;
        initProjection();
    }

    private void initProjection() {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        gluPerspective(fov, aspect, near, far);
        glMatrixMode(GL_MODELVIEW);
    }

    private float speed1 = 0.3f;
    private float speed2 = 0.1f;

    public void move(float locSpeed, float dir) {
        world.onPlayerMove(getX(), getY(), getZ());
        double rad = Math.toRadians(ry + 90 * dir);
        velZ += locSpeed * Math.sin(rad);
        velX -= locSpeed * Math.cos(rad);
        if (flying) {
            if (dir != 0) {
                velY -= locSpeed * Math.sin(Math.toRadians(rx));
            }
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
        if (rx < -60) rx = -60;
        if (rx > 60) rx = 60;
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
        skybox.initGL();
    }

    @Override
    public void renderGL() {
        glLoadIdentity();
        glRotatef(rx, 1, 0, 0);
        glRotatef(ry, 0, 1, 0);
        glRotatef(rz, 0, 0, 1);
        skybox.renderGL();
        glTranslatef(-x, -y, z);
    }

    @Override
    public void destroyGL() {
        skybox.destroyGL();
    }

    @Override
    public void update() {
        skybox.update();
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
            rotateY(-1.5f);
        }
        if (Keyboard.isKeyDown(GLFW_KEY_E)) {
            rotateY(1.5f);
        }
        if (Keyboard.isKeyDown(GLFW_KEY_SPACE) && onGround) {
            velY += 2;
            onGround = false;
        }
//        if (Keyboard.isKeyDown(GLFW_KEY_R)) {
//            rotateX(-0.75f);
//        }
//        if (Keyboard.isKeyDown(GLFW_KEY_F)) {
//            rotateX(0.75f);
//        }
        float lastX = x;
        float lastY = y;
        float lastZ = z;

        if (Math.abs(velX) < 0.01) velX = 0;
        if (Math.abs(velY) < 0.01) velY = 0;
        if (Math.abs(velZ) < 0.01) velZ = 0;

        x += velX;
        y += velY;
        z += velZ;
        if (x != lastX || lastY != y || z != lastZ)
            world.onPlayerMove(getX(), getY(), getZ());
        velY -= 0.3f;
        velX /= 3;
        velY /= 1.7;
        velZ /= 3;
    }

    private double lastX = -1;
    private double lastY = -1;

    @Override
    public void onMouseMove(double x, double y) {
        if (mouseLocked) {
            if (lastX != -1 || lastY != -1) {
                rotateX((float) (y - lastY));
                rotateY((float) (x - lastX));
            }
        }
        lastX = x;
        lastY = y;
    }

    @Override
    public void onMousePress(MouseButton button) {
    }

    @Override
    public void onMouseRelease(MouseButton button) {
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public boolean isMouseLocked() {
        return mouseLocked;
    }

    public void setMouseLocked(boolean mouseLocked) {
        this.mouseLocked = mouseLocked;
    }

    public void ground(float y) {
        setY(y);
        onGround = true;
    }
}
