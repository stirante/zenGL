package com.stirante.opengl.input;

import org.lwjgl.glfw.GLFWKeyCallback;

import static org.lwjgl.glfw.GLFW.*;

public class Keyboard extends GLFWKeyCallback {

    public static boolean[] keys = new boolean[65535];

    public static final int KEY_W = GLFW_KEY_W;
    public static final int KEY_S = GLFW_KEY_S;
    public static final int KEY_D = GLFW_KEY_D;
    public static final int KEY_A = GLFW_KEY_A;
    public static final int KEY_Q = GLFW_KEY_Q;
    public static final int KEY_E = GLFW_KEY_E;

    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        if (key < 0 || key >= 65535) return;
        keys[key] = action != GLFW_RELEASE;
    }

    public static boolean isKeyDown(int keycode) {
        return keys[keycode];
    }

}
