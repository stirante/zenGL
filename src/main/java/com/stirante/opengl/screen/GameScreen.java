package com.stirante.opengl.screen;

import com.stirante.opengl.component.Camera;
import com.stirante.opengl.component.Image;
import com.stirante.opengl.component.Text;
import com.stirante.opengl.component.World;
import org.lwjgl.opengl.GL11;

public class GameScreen extends Screen {

    private Camera camera;
    private World world;
    private Text fpsText;
    private Text upsText;
    private int renders = 0;
    private int updates = 0;
    private long renderTime = System.currentTimeMillis();
    private long updateTime = System.currentTimeMillis();
    private int fps = 0;
    private int ups = 0;
    private Image image;

    public GameScreen(Window window) {
        super(window);
        camera = new Camera(
                45f,
                1,
                0.1f,
                1000.0f);
        world = new World();
        addComponent(camera);
        addComponent(world);
        addMouseListener(camera);
    }

    @Override
    public void initGL() {
        fpsText = getWindow().getFontRenderer().getText("FPS: ", 5, 5, 16);
        upsText = getWindow().getFontRenderer().getText("UPS: ", 5, 21, 16);
        image = new Image("res/grass.jpg");
        image.initGL();
        image.setHeight(100);
        image.setWidth(100);
    }

    @Override
    public void renderGL() {
        renders++;
        if (System.currentTimeMillis() - renderTime >= 1000) {
            fps = renders;
            renders = 0;
            renderTime = System.currentTimeMillis();
        }
    }

    @Override
    public void render2D(int width, int height) {
        GL11.glColor3f(1f, 1f, 1.0f);
        fpsText.renderGL();
        upsText.renderGL();
        getWindow().getFontRenderer().drawText(String.valueOf(fps), fpsText.getWidth(), 5, 16);
        getWindow().getFontRenderer().drawText(String.valueOf(ups), upsText.getWidth(), 21, 16);
        image.renderGL();
    }

    @Override
    public void destroyGL() {

    }

    @Override
    public void update() {
        updates++;
        if (System.currentTimeMillis() - updateTime >= 1000) {
            ups = updates;
            updates = 0;
            updateTime = System.currentTimeMillis();
        }
    }

    @Override
    public void onAttach() {
        camera.setAspect((float) getWindow().getWidth() / (float) getWindow().getHeight());
    }

    @Override
    public void onDetach() {

    }
}
