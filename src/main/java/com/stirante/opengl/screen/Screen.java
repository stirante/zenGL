package com.stirante.opengl.screen;

import com.stirante.opengl.component.GLComponent;
import com.stirante.opengl.component.GLComponentGroup;
import com.stirante.opengl.input.MouseListener;

import java.util.ArrayList;

public abstract class Screen implements GLComponent {

    private final Window window;

    public Screen(Window window) {
        this.window = window;
    }

    private GLComponentGroup components = new GLComponentGroup();
    private ArrayList<MouseListener> mouseListeners = new ArrayList<>();

    public abstract void onAttach();

    public abstract void onDetach();

    public abstract void render2D(int width, int height);

    public GLComponentGroup getComponents() {
        return components;
    }

    public ArrayList<MouseListener> getMouseListeners() {
        return mouseListeners;
    }

    public void addMouseListener(MouseListener mouseListener) {
        mouseListeners.add(mouseListener);
    }

    public void removeMouseListener(MouseListener mouseListener) {
        mouseListeners.remove(mouseListener);
    }

    public Window getWindow() {
        return window;
    }
}
