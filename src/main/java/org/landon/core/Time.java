package org.landon.core;

public final class Time {

    private static double timeStarted = 0;
    private static double delta = 0;

    public static void startFrame() {
        timeStarted = System.nanoTime() / 1e9;
    }

    public static void endFrame() {
        double timeEnded = System.nanoTime() / 1e9;
        delta = timeEnded - timeStarted;
    }

    public static double getDelta() {
        return delta;
    }

}
