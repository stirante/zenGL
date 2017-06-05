package com.stirante.opengl.component;

import com.stirante.opengl.input.Keyboard;
import com.stirante.opengl.util.AxisAlignedBoundingBox;
import com.stirante.opengl.util.Ray;
import com.stirante.opengl.util.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

public class CollisionTest implements GLComponent {

    private ArrayList<AxisAlignedBoundingBox> boxes = new ArrayList<>();
    private Ray r;
    private Camera camera;

    public CollisionTest(Camera camera) {
        this.camera = camera;
        boxes.add(new AxisAlignedBoundingBox(new Vector3f(30, 10, -30), new Vector3f(31, 11, -31)));
    }

    @Override
    public void initGL() {

    }

    @Override
    public void renderGL() {
        if (r != null) r.gl();
        Ray ray = camera.getRay();
        if (Keyboard.isKeyDown(GLFW.GLFW_KEY_X)) r = ray;
        ray.gl();
        for (AxisAlignedBoundingBox box : boxes) {
            glEnable(GL_COLOR_MATERIAL);
            if (intersects(box, ray))
                glColor3f(1f, 0f, 0f);
            else
                glColor3f(1f, 1f, 1f);
            renderBox(box);
        }
    }

    @Override
    public void destroyGL() {

    }

    @Override
    public void update() {

    }

    private void renderBox(AxisAlignedBoundingBox box) {
        glBegin(GL_QUADS);
        glVertex3d(box.getMin().x, box.getMin().y, box.getMin().z);
        glVertex3d(box.getMin().x, box.getMin().y, box.getMax().z);
        glVertex3d(box.getMax().x, box.getMin().y, box.getMax().z);
        glVertex3d(box.getMax().x, box.getMin().y, box.getMin().z);
        glEnd();
        glBegin(GL_QUADS);
        glVertex3d(box.getMin().x, box.getMax().y, box.getMin().z);
        glVertex3d(box.getMin().x, box.getMax().y, box.getMax().z);
        glVertex3d(box.getMax().x, box.getMax().y, box.getMax().z);
        glVertex3d(box.getMax().x, box.getMax().y, box.getMin().z);
        glEnd();
        glBegin(GL_QUADS);
        glVertex3d(box.getMin().x, box.getMin().y, box.getMin().z);
        glVertex3d(box.getMin().x, box.getMin().y, box.getMax().z);
        glVertex3d(box.getMin().x, box.getMax().y, box.getMax().z);
        glVertex3d(box.getMin().x, box.getMax().y, box.getMin().z);
        glEnd();
        glBegin(GL_QUADS);
        glVertex3d(box.getMax().x, box.getMin().y, box.getMin().z);
        glVertex3d(box.getMax().x, box.getMin().y, box.getMax().z);
        glVertex3d(box.getMax().x, box.getMax().y, box.getMax().z);
        glVertex3d(box.getMax().x, box.getMax().y, box.getMin().z);
        glEnd();
    }

    private boolean intersects(AxisAlignedBoundingBox box, Ray ray) {
        double t1 = (box.getMin().getX() - ray.getOrigin().getX()) * ray.getDirection().getX();
        double t2 = (box.getMax().getX() - ray.getOrigin().getX()) * ray.getDirection().getX();

        double tmin = Math.min(t1, t2);
        double tmax = Math.max(t1, t2);

        t1 = (box.getMin().getY() - ray.getOrigin().getY()) * ray.getDirection().getY();
        t2 = (box.getMax().getY() - ray.getOrigin().getY()) * ray.getDirection().getY();

        tmin = Math.max(tmin, Math.min(Math.min(t1, t2), tmax));
        tmax = Math.min(tmax, Math.max(Math.max(t1, t2), tmin));

        t1 = (box.getMin().getZ() - ray.getOrigin().getZ()) * ray.getDirection().getZ();
        t2 = (box.getMax().getZ() - ray.getOrigin().getZ()) * ray.getDirection().getZ();

        tmin = Math.max(tmin, Math.min(Math.min(t1, t2), tmax));
        tmax = Math.min(tmax, Math.max(Math.max(t1, t2), tmin));

        return tmax > Math.max(tmin, 0.0);
    }

}
