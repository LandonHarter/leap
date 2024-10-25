package org.landon;

import org.landon.core.Window;

public class Main {
    public static void main(String[] args) {
        Window window = new Window(800, 500, "LEAP Physics Engine");
        window.create();

        while (window.isOpen()) {
            window.update();


            window.endFrame();
        }
    }
}