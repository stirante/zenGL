package com.stirante.opengl;

import com.stirante.opengl.screen.GameScreen;
import com.stirante.opengl.screen.Window;

public class Main {

    public static void main(String[] args) {
        Window window = new Window(1024, 768);
        window.run(new GameScreen(window));
    }

}
