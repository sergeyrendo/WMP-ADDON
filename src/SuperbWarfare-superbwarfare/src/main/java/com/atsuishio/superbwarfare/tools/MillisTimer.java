package com.atsuishio.superbwarfare.tools;

public class MillisTimer {

    public long startTime;
    private boolean started = false;

    public void start() {
        if (!started) {
            started = true;
            startTime = System.currentTimeMillis();
        }
    }

    public boolean started() {
        return started;
    }

    public void stop() {
        started = false;
    }

    public long getProgress() {
        if (!started) {
            return 0;
        }
        return System.currentTimeMillis() - startTime;
    }

    public void setProgress(long progress) {
        startTime = System.currentTimeMillis() - progress;
    }

}
