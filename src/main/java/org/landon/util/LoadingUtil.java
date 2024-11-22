package org.landon.util;

import org.landon.core.Time;

import javax.swing.*;
import java.awt.*;

public final class LoadingUtil {

    private static JFrame frame;
    private static int timeRunning = 1;
    private static double startTime = 0;
    private static String message = "";

    public static void openLoadingScreen(String message) {
        LoadingUtil.message = message;

        frame = new JFrame(message + " (Active for 0s)");
        frame.setIconImage(new ImageIcon("resources/icons/logos/logo-fill-light.png").getImage());
        frame.setSize(400, 100);
        frame.setLocationRelativeTo(null);
        frame.setAlwaysOnTop(true);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());
        frame.add(Box.createRigidArea(new Dimension(0, 10)), BorderLayout.NORTH);
        frame.add(Box.createRigidArea(new Dimension(0, 10)), BorderLayout.SOUTH);
        frame.add(Box.createRigidArea(new Dimension(10, 0)), BorderLayout.EAST);
        frame.add(Box.createRigidArea(new Dimension(10, 0)), BorderLayout.WEST);

        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setPreferredSize(new Dimension(100, 50));
        progressBar.setForeground(new Color(0, 190, 20));
        progressBar.setBorderPainted(false);
        frame.add(progressBar, BorderLayout.CENTER);

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

            if (frame != null) {
                frame.setTitle(message + " (Active for " + timeRunning + "s)");
            }
            timeRunning++;
        }
    }

}
