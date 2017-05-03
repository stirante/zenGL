package com.stirante.opengl.component;

import java.util.ArrayList;

public class GLComponentGroup implements GLComponent {

    private ArrayList<GLComponent> components = new ArrayList<>();
    private ArrayList<GLComponent> toInit = new ArrayList<>();
    private ArrayList<GLComponent> toRemove = new ArrayList<>();

    public ArrayList<GLComponent> getComponents() {
        return components;
    }

    @Override
    public void initGL() {
        for (GLComponent component : components) {
            component.initGL();
        }
    }

    @Override
    public void renderGL() {
        for (GLComponent component : toInit) {
            component.initGL();
        }
        toInit.clear();
        for (GLComponent component : toRemove) {
            component.destroyGL();
        }
        toRemove.clear();
        for (GLComponent component : components) {
            component.renderGL();
        }
    }

    @Override
    public void destroyGL() {
        for (GLComponent component : components) {
            component.destroyGL();
        }
    }

    @Override
    public void update() {
        for (GLComponent component : components) {
            component.update();
        }
    }

    public void addComponent(GLComponent component) {
        toInit.add(component);
        components.add(component);
    }

    public void removeComponent(GLComponent component) {
        toRemove.add(component);
        components.remove(component);
    }
}
