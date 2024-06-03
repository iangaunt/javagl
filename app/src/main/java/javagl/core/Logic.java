package javagl.core;

/** An interface for establishing game logic. */
public interface Logic {
    /**
     * Initializes the logic of the game engine.
     * 
     * @throws Exception - An exception thrown from errors during initialization.
     */
    void init() throws Exception;

    /** Handles input to the game window. */
    void input();

    /** Updates the window every frame. */
    void update();

    /** Renders elements to the screen. */
    void render();

    /** Cleans up the window on close or crash. */
    void cleanup();
}
