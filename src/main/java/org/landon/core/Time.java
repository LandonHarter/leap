package org.landon.core;

public final class Time {

    private static final double timeOpened = System.nanoTime();
    private static float time = 0;
    private static double timeStarted = 0;
    private static double delta = 0;

    public static void startFrame() {
        timeStarted = System.nanoTime() / 1e9;
    }

    public static void endFrame() {
        double timeEnded = System.nanoTime() / 1e9;
        delta = timeEnded - timeStarted;
    }

    public static double getTime() {
        time = (float)((System.nanoTime() - timeOpened) * 1E-9);
        return time;
    }

    public static double getDelta() {
        return delta;
    }

}
