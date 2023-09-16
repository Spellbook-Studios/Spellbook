package dk.sebsa.spellbook.math;

import dk.sebsa.mana.Logger;
import lombok.Getter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * Keeps track of time
 *
 * @author sebsn
 * @since 1.0.0
 */
public class Time {
    // Constant
    /**
     * One second in nano seconds
     */
    public static final long SECOND_NANO = 1000000000L;

    // Settings
    /**
     * Used to scale deltaTime
     */
    public static float timeScale = 1;

    // Time Values
    private static long startTime; // The moment we began time keeping
    private static long rawTime; // Time value that goes up at the rate of real time (It may be negative)
    private static long lastFrameTime; // When the last frame started

    private static long fpsCountTime; // Time instant when FPS was last counted
    private static int frames; // Frames since last FPS count
    private static int framesPerSecond;
    private static double averageFrameLength; // The average frame length this second. In millis
    private static double frameTimeThisSecond;

    /**
     * -- GETTER --
     *
     * @return the time between the start of the current frame and the start of the
     * last frame (in millis)
     */
    @Getter
    private static float deltaTime;
    /**
     * -- GETTER --
     *
     * @return the time between the start of the current frame and the start of the
     * last frame (in millis) unscaled
     */
    @Getter
    private static float unscaledDelta;

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    /**
     * Initializes time keeping
     *
     * @param logger Main logger
     */
    public static void init(final Logger logger) {
        logger.log("Initializing Time");
        startTime = System.nanoTime();
    }

    /**
     * Processes time and calculates stuff like deltaTime
     */
    public static void procsessFrame() {
        rawTime = System.nanoTime();
        final long time = (TimeUnit.MILLISECONDS.convert(rawTime - startTime, TimeUnit.NANOSECONDS));

        // Calculate frame time
        // When this frame started
        final long frameStartTime = rawTime;
        // How long the last frame was
        final long framePassedTime = frameStartTime - lastFrameTime;
        lastFrameTime = frameStartTime;
        frameTimeThisSecond = frameTimeThisSecond + framePassedTime;

        // Calculate Delta time
        final double rawDelta = framePassedTime / (double) SECOND_NANO;
        /*
         * if(rawDelta > 0.01f) {
         * deltaTime = (float) (0.01 * timeScale);
         * unscaledDelta = 0.01f;
         * }
         * else {
         */
        deltaTime = (float) (rawDelta * timeScale);
        unscaledDelta = (float) rawDelta;
        // }

        // FPS Count
        if (time - fpsCountTime >= 1000) {
            framesPerSecond = frames;
            frames = 0;
            fpsCountTime = time;

            averageFrameLength = (frameTimeThisSecond / framesPerSecond / 1000000);
            frameTimeThisSecond = 0;
        }
        frames++;
    }

    // Public Getters

    /**
     * @return time since the program started (in millis)
     */
    public static long getTime() {
        rawTime = System.nanoTime();
        return (TimeUnit.MILLISECONDS.convert(rawTime - startTime, TimeUnit.NANOSECONDS));
    }

    /**
     * @return The current time of the OS
     */
    public static String getNow() {
        return LocalTime.now().format(dateTimeFormatter);
    }

    /**
     * @return Gets the amount of frames within the last second
     */
    public static int getFPS() {
        return framesPerSecond;
    }

    /**
     * @return the average length of a frame, from the last second (in millis)
     */
    public static double getAFL() {
        return averageFrameLength;
    }
}
