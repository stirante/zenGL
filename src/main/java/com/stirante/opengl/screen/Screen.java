package com.stirante.opengl.screen;

import com.stirante.opengl.component.GLComponent;
import com.stirante.opengl.input.MouseListener;

import java.util.ArrayList;

public abstract class Screen implements GLComponent {

    private final Window window;

    public Screen(Window window) {
        this.window = window;
    }

    private ArrayList<GLComponent> components = new ArrayList<>();
    private ArrayList<GLComponent> toInit = new ArrayList<>();
    private ArrayList<GLComponent> toRemove = new ArrayList<>();
    private ArrayList<MouseListener> mouseListeners = new ArrayList<>();

    public abstract void onAttach();

    public abstract void onDetach();

    public abstract void render2D(int width, int height);

    public ArrayList<GLComponent> getComponents() {
        return components;
    }

    public ArrayList<MouseListener> getMouseListeners() {
        return mouseListeners;
    }

    ArrayList<GLComponent> getNewComponents() {
        return toInit;
    }

    ArrayList<GLComponent> getOldComponents() {
        return toRemove;
    }

    public void addMouseListener(MouseListener mouseListener) {
        mouseListeners.add(mouseListener);
    }

    public void removeMouseListener(MouseListener mouseListener) {
        mouseListeners.remove(mouseListener);
    }

    public void addComponent(GLComponent component) {
        toInit.add(component);
        components.add(component);
    }

    public void removeComponent(GLComponent component) {
        toRemove.add(component);
        components.remove(component);
    }

    public Window getWindow() {
        return window;
    }
}
