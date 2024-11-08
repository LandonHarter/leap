package org.landon.core.profiling;

public class ProfilingTimer {

    private long begin, end;
    private long finalTime;

    public ProfilingTimer(boolean start) {
        if (start) startSampling();
    }

    public ProfilingTimer() {
        this(true);
    }

    public void startSampling() {
        begin = System.nanoTime();
    }

    public void endSampling() {
        end = System.nanoTime();
        finalTime = end - begin;
    }

    public float getTime() {
        return finalTime / 1000000.0f;
    }

}