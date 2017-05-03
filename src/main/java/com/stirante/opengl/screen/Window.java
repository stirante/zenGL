package com.stirante.opengl.screen;

import com.stirante.opengl.component.GLComponent;
import com.stirante.opengl.input.Keyboard;
import com.stirante.opengl.input.MouseListener;
import com.stirante.opengl.util.FontRenderer;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {
    private long handle;

    private static final long DELTA = 50;
    private FontRenderer fontRenderer;
    private long lastUpdate = -1L;
    private Screen screen;
    private final int width;
    private final int height;

    public Window(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void run(Screen screen) {
        this.screen = screen;
        System.out.println("LWJGL " + Version.getVersion());
        init();
        loop();
        glfwFreeCallbacks(handle);
        glfwDestroyWindow(handle);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        //redirect errors to System.err
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        handle = glfwCreateWindow(width, height, "OpenGL", NULL, NULL);
        if (handle == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        //some callbacks
        glfwSetKeyCallback(handle, new Keyboard());
        glfwSetCursorPosCallback(handle, (l, v, v1) -> onMouseMoved(v, v1));
        glfwSetMouseButtonCallback(handle, (l, i, i1, i2) -> onMouseAction(i, i1, i2));

        //center window
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            glfwGetWindowSize(handle, pWidth, pHeight);

            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            glfwSetWindowPos(
                    handle,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        }

        glfwMakeContextCurrent(handle);
        glfwSwapInterval(1);//v-sync

        glfwShowWindow(handle);
    }


    private void onMouseAction(int button, int action, int mods) {
        MouseListener.MouseButton mouseButton = MouseListener.MouseButton.getById(button);
        if (action == GLFW_PRESS) {
            for (MouseListener mouseListener : screen.getMouseListeners()) {
                mouseListener.onMousePress(mouseButton);
            }
        } else if (action == GLFW_RELEASE) {
            for (MouseListener mouseListener : screen.getMouseListeners()) {
                mouseListener.onMouseRelease(mouseButton);
            }
        }
    }

    private void onMouseMoved(double x, double y) {
        for (MouseListener mouseListener : screen.getMouseListeners()) {
            mouseListener.onMouseMove(x, y);
        }
    }

    private void loop() {
        GL.createCapabilities();
        System.out.println("OpenGL: " + glGetString(GL_VERSION));
        initGL();

        while (!glfwWindowShouldClose(handle)) {
            while (lastUpdate == -1L || System.currentTimeMillis() - lastUpdate >= DELTA) {
                update();
                if (lastUpdate == -1L) lastUpdate = System.currentTimeMillis();
                else lastUpdate += DELTA;
            }
            for (GLComponent component : screen.getNewComponents()) {
                component.initGL();
            }
            screen.getNewComponents().clear();
            for (GLComponent component : screen.getOldComponents()) {
                component.destroyGL();
            }
            screen.getOldComponents().clear();
            renderGL();
            glfwSwapBuffers(handle);
            glfwPollEvents();
        }
    }

    private void update() {
        screen.update();
        for (GLComponent component : screen.getComponents()) {
            component.update();
        }
    }

    private void renderGL() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glColor3f(1f, 1f, 1f);
        screen.renderGL();
        for (GLComponent component : screen.getComponents()) {
            component.renderGL();
        }

        glMatrixMode(GL_PROJECTION);
        glPushMatrix();
        glLoadIdentity();
        glOrtho(0.0f, width, height, 0.0f, -1f, 1f);

        glMatrixMode(GL_MODELVIEW);
        glPushMatrix();
        glLoadIdentity();

        glDisable(GL_DEPTH_TEST);
        glDisable(GL_LIGHTING);

        screen.render2D(width, height);

        glEnable(GL_DEPTH_TEST);
        glEnable(GL_LIGHTING);

        glMatrixMode(GL_PROJECTION);
        glPopMatrix();

        glMatrixMode(GL_MODELVIEW);
        glPopMatrix();
    }

    private void initGL() {
        glEnable(GL11.GL_TEXTURE_2D);
        glShadeModel(GL11.GL_SMOOTH);
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClearDepth(1.0);
        glEnable(GL11.GL_DEPTH_TEST);
        glDepthFunc(GL11.GL_LEQUAL);

        GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);

        //antialiasing?
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_POINT_SMOOTH);
        glEnable(GL_LINE_SMOOTH);
        glEnable(GL_MULTISAMPLE);

        fontRenderer = new FontRenderer();

        if (screen != null) {
            screen.onAttach();
            screen.initGL();
        }
    }

    public void setScreen(Screen screen) {
        if (this.screen != null) {
            this.screen.onDetach();
            this.screen.destroyGL();
        }
        this.screen = screen;
        if (this.screen != null) {
            this.screen.onAttach();
            this.screen.initGL();
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public FontRenderer getFontRenderer() {
        return fontRenderer;
    }
}
