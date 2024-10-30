package org.landon.util;

import org.landon.core.Time;

import javax.swing.*;
import java.awt.*;

public final class LoadingUtil {

    private static JFrame frame;
    private static LoadingWindow window;
    private static int timeRunning = 1;
    private static double startTime = 0;

    public static void openLoadingScreen(String message) {
        frame = new JFrame(message + " (Active for 0s)");
        frame.setSize(400, 100);
        frame.setLocationRelativeTo(null);
        frame.setAlwaysOnTop(true);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setResizable(false);

        window = new LoadingWindow(message);
        frame.add(window);

        frame.setVisible(true);

        startTime = Time.getTime();
        ThreadUtil.run(() -> {
            while (frame != null) {
                updateProgressBar();
            }
        });
    }

    public static void closeLoadingBar() {
        if (frame == null) return;

        frame.setVisible(false);
        frame.dispose();
        frame = null;
        timeRunning = 1;
    }


    private static void updateProgressBar() {
        double time = Time.getTime();
        if (time - startTime > 1) {
            startTime = time;
            frame.setTitle(window.message + " (Active for " + timeRunning + "s)");
            timeRunning++;
        }

        window.repaint();
    }

    private static class LoadingWindow extends Canvas {

        public float dst = 0;

        private final float addAmount = 15;
        private final float cutoff = 0.833333333f;
        private final float speed = 0.35f;
        private final Color green = new Color(0, 190, 20);

        private final String message;

        public LoadingWindow(String message) {
            this.message = message;
        }

        @Override
        public void paint(Graphics g) {
            dst += addAmount / 1000;
            if (dst >= cutoff - 0.01f) {
                dst = 0;
            }

            g.setColor(new Color(0.8f, 0.8f, 0.8f));
            g.fillRect(14, 15, 360, 30);

            g.setColor(green);
            g.fillRect(Math.round(14 + (360 * dst)), 15, 60, 30);

            try {
                Thread.sleep(((Float)(addAmount * (1 / speed))).longValue());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
