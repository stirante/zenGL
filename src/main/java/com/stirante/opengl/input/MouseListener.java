package com.stirante.opengl.input;

import static org.lwjgl.glfw.GLFW.*;

public interface MouseListener {

    enum MouseButton {
        LEFT, RIGHT, MIDDLE, UNKNOWN;

        public static MouseButton getById(int id) {
            switch (id) {
                case GLFW_MOUSE_BUTTON_LEFT:
                    return LEFT;
                case GLFW_MOUSE_BUTTON_RIGHT:
                    return RIGHT;
                case GLFW_MOUSE_BUTTON_MIDDLE:
                    return MIDDLE;
                default:
                    return UNKNOWN;
            }
        }
    }

    void onMouseMove(double x, double y);

    void onMousePress(MouseButton button);

    void onMouseRelease(MouseButton button);

}
