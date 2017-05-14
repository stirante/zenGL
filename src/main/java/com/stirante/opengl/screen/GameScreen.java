package com.stirante.opengl.screen;

import com.stirante.opengl.component.Camera;
import com.stirante.opengl.component.Image;
import com.stirante.opengl.component.Text;
import com.stirante.opengl.component.World;
import com.stirante.opengl.input.Keyboard;
import org.lwjgl.glfw.GLFW;

import static org.lwjgl.opengl.GL11.*;

public class GameScreen extends Screen {

    private Camera camera;
    private World world;
//    private Text fpsText;
//    private Text upsText;
//    private int renders = 0;
//    private int updates = 0;
//    private long renderTime = System.currentTimeMillis();
//    private long updateTime = System.currentTimeMillis();
//    private int fps = 0;
//    private int ups = 0;
    private boolean mouseLocked = true;

    public GameScreen(Window window) {
        super(window);
        camera = new Camera(
                45f,
                1,
                0.1f,
                1000.0f);
        world = new World();
        getComponents().addComponent(camera);
        getComponents().addComponent(world);
        addMouseListener(camera);
    }

    @Override
    public void initGL() {
//        fpsText = getWindow().getFontRenderer().getText("FPS: ", 5, 5, 16);
//        upsText = getWindow().getFontRenderer().getText("UPS: ", 5, 21, 16);
        //lighting
        glEnable(GL_COLOR_MATERIAL);
        glEnable(GL_LIGHTING);
        glEnable(GL_LIGHT0);
        float[] lightPosition = {0, 1, 0, 1f};
        glLightfv(GL_LIGHT0, GL_POSITION, lightPosition);
    }

    @Override
    public void renderGL() {
//        renders++;
//        if (System.currentTimeMillis() - renderTime >= 1000) {
//            fps = renders;
//            renders = 0;
//            renderTime = System.currentTimeMillis();
//        }
    }

    @Override
    public void render2D(int width, int height) {
//        fpsText.renderGL();
//        upsText.renderGL();
//        getWindow().getFontRenderer().drawText(String.valueOf(fps), fpsText.getWidth(), 5, 16);
//        getWindow().getFontRenderer().drawText(String.valueOf(ups), upsText.getWidth(), 21, 16);
    }

    @Override
    public void destroyGL() {

    }

    @Override
    public void update() {
//        updates++;
//        if (System.currentTimeMillis() - updateTime >= 1000) {
//            ups = updates;
//            updates = 0;
//            updateTime = System.currentTimeMillis();
//        }
        if (Keyboard.isKeyDown(GLFW.GLFW_KEY_ESCAPE)) getWindow().close();
        //odblokowuje mysz (roboczo, nie dziala idealnie)
        if (Keyboard.isKeyDown(GLFW.GLFW_KEY_LEFT_CONTROL)) {
            if (mouseLocked) {
                camera.setMouseLocked(false);
                getWindow().unlockMouse();
                mouseLocked = !mouseLocked;
            }
            else {
                camera.setMouseLocked(true);
                getWindow().lockMouse();
                mouseLocked = !mouseLocked;
            }
        }
        //iluzja kolizji (ustawiam caly czas Y kamery na najnizszy punkt na mapie)
        camera.setY(world.getBottomAt(camera.getX(), camera.getZ()) + 1f);
    }

    @Override
    public void onAttach() {
        camera.setAspect((float) getWindow().getWidth() / (float) getWindow().getHeight());
        getWindow().lockMouse();
    }

    @Override
    public void onDetach() {
        getWindow().unlockMouse();
    }
}
