package app.misc;

public class StopWatch {
    private static long startTime;
    private static long endTime;
    private static boolean running = false;

    public static void start() {
        startTime = System.currentTimeMillis();
        running = true;
        System.out.println("Stopwatch started.");
    }

    public static void peek() {
        if (!running) {
            System.out.println("Stopwatch is not running.");
            return;
        }
        long now = System.currentTimeMillis();
        long elapsed = now - startTime;
        System.out.println("Currently elapsed time: " + elapsed + " ms");
    }

    public static void stop() {
        if (!running) {
            System.out.println("Stopwatch was not started.");
            return;
        }
        endTime = System.currentTimeMillis();
        running = false;

        long elapsed = endTime - startTime;
        System.out.println("Stopwatch stopped.");
        System.out.println("Elapsed time: " + elapsed + " ms");
    }
}
