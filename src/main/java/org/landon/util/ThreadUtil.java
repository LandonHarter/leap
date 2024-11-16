package org.landon.util;

import java.util.HashMap;
import java.util.UUID;

public final class ThreadUtil {

    private static HashMap<String, Thread> threads = new HashMap<>();

    public static boolean ThreadExists(String threadName) {
        return threads.containsKey(threadName);
    }

    public static Thread run(Runnable action) {
        String name = UUID.randomUUID().toString();

        Thread thread = new Thread(action);
        thread.setName(name);
        thread.start();

        threads.put(name, thread);
        return thread;
    }

    public static void run(Runnable action, String name) {
        Thread thread = new Thread(action);
        thread.setName(name);
        thread.start();

        threads.put(name, thread);
    }

    public static void kill(String thread) {
        if (!threads.containsKey(thread)) {
            System.out.println("Thread does not exist");
            return;
        }

        threads.get(thread).interrupt();
    }

}
