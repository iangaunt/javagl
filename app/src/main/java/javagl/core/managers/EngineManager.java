package javagl.core.managers;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

import javagl.App;
import javagl.core.Logic;
import javagl.core.utils.Constants;

/** The class for handling runtime of the game engine. */
public class EngineManager {
    // The amount of nanoseconds in one second.
    public static final long NANOSECOND = 1000000000L;
    
    // The number of desires frames per second.
    public static final float FRAMERATE = 1000;

    // The number of actual frames per second.
    private static int fps;
    
    // The amount of time in between one frame.
    public static float frametime = 1.0f / FRAMERATE;

    // Checks if the engine is running.
    private boolean isRunning;

    // Fetches the window and establishes an error callback for debugging.
    private WindowManager window;
    private GLFWErrorCallback errorCallback;

    // Creates a new set of Logic rules to run.
    private Logic gameLogic;

    /**
     * Starts the window and sets up the error callback.
     * 
     * @throws Exception - An exception caused by a GLFW initialization failure.
     */
    private void init() throws Exception {
        GLFW.glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
        window = App.getWindow();
        gameLogic = App.getGame();
        
        window.init();
        gameLogic.init();
    }

    /**
     * Attempts to open and begin rendering in the game window.
     * 
     * @throws Exception - Any issues encounted within initialization or rendering.
     */
    public void start() throws Exception {
        init();
        if (isRunning) return;
        run();
    }

    /** Runs the window, counting frames and performing render and input checks. */
    public void run() {
        // Enables the window's running state.
        this.isRunning = true;

        // Counts the current number of frames.
        int frames = 0;
        long frameCounter = 0;

        // Calculates the number of nanoseconds passed since the last tick.
        long lastTime = System.nanoTime();

        // Counts the amount of time that has not been processed by the renderer.
        double unprocessedTime = 0;

        while (isRunning) {
            boolean render = false;

            // Calculates the amount of time passed since the last render cycle.
            long startTime = System.nanoTime();
            long passedTime = startTime - lastTime;
            lastTime = startTime;

            // Counts the number of frames passed.
            unprocessedTime += passedTime / (double) NANOSECOND;
            frameCounter += passedTime;

            // Helps with input and output.
            input();

            // Continues rendering until the amount of unprocessed time has passed the frame time.
            // This accounts for dropped frames in the case of a render delay.
            while (unprocessedTime > frametime) {
                render = true;
                unprocessedTime -= frametime;

                if (window.windowShouldClose()) stop();
                if (frameCounter >= NANOSECOND) {
                    setFps(frames);
                    window.setTitle(Constants.TITLE + " - " + fps + " fps");

                    frames = 0;
                    frameCounter = 0;
                }
            }

            // Updates the frame renderer and increases the frame ticker.
            if (render) {
                update();
                render();
                frames++;
            }
        }

        // Cleans up the window once the engine is done.
        cleanup();
    }

    /** Turns off the engine. */
    private void stop() {
        if (!isRunning) return;
        isRunning = false;
    }

    /** Handles input in the engine. */
    private void input() {
        gameLogic.input();
    }

    /** Updates the content of the window. */
    private void render() {
        gameLogic.render();
        window.update();
    }

    /** Updates the state of the window. */
    private void update() {
        gameLogic.update();
    }

    /** Cleans up the window and terminates the GLFW process. */
    private void cleanup() {
        window.cleanup();
        gameLogic.cleanup();
        errorCallback.free();
        GLFW.glfwTerminate();
    }

    /**
     * The getter method for the FPS value.
     * @return - The FPS of the window display.
     */
    public static int getFps() {
        return fps;
    }

    /**
     * The setter method for the FPS value.
     * @param fps - The new FPS of the window display.
     */
    public static void setFps(int fps) {
        EngineManager.fps = fps;
    }
}
