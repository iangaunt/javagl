package javagl.core.managers;

import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

public class WindowManager {
    // The FOV (field of view) of the window.
    public static final float FOV = (float) Math.toRadians(60);

    // Sets boundaries of rendered objects, preventing objects that are 
    // too close or too far from being rendered to save memory.
    public static final float Z_NEAR = 0.01f;
    public static final float Z_FAR = 1000f;

    // The title of the window.
    public final String title;

    // The width and height of the window.
    private int width, height;
    
    // The window, stored as a long value.
    private long window;

    // Boolean values to check if the window is reszized and to change VSync.
    private boolean resize, vSync;

    // A 4x4 matrix of float values to use for projection calculations.
    private final Matrix4f projectionMatrix;

    /**
     * Initializes the variables necessary to open the window.
     * 
     * @param title - The title of the window.
     * @param width - The width of the window.
     * @param height - The height of the window.
     * @param vSync - A toggle value for VSync.
     */
    public WindowManager(String title, int width, int height, boolean vSync) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.vSync = vSync;

        // Initializes a new identity projection matrix (1s on left to right diagonal).
        projectionMatrix = new Matrix4f();
    }

    /** Initializes a new window and sets its GL window hints. */
    public void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!GLFW.glfwInit()) throw new IllegalStateException("Unable to initialize GLFW");

        // Established some window hints for the OpenGL window.
        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
        GLFW.glfwWindowHint(GLFW.GLFW_DECORATED, GLFW.GLFW_FALSE);

        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);

        // Makes sure the window stays maximized or bordered.
        boolean maximized = false;
        if (width == 0 || height == 0) {
            height = 100;
            width = 100;

            GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, GLFW.GLFW_TRUE);
            maximized = true;
        }

        // Initializes the window with GLFW.
        window = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);
        if (window == MemoryUtil.NULL) throw new RuntimeException("Failed to create GLFW window");

        // Sets the current context.
        GLFW.glfwMakeContextCurrent(window);

        // Sets the resize buffer of the window to change its height and width when resized.
        GLFW.glfwSetFramebufferSizeCallback(window, (window, width, height) -> {
            this.width = width;
            this.height = height;
            this.setResize(true);
        });

        // Establishes a window force-close when the escape key is released.
        GLFW.glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE) {
                GLFW.glfwSetWindowShouldClose(window, true);
            }
        });

        // If the window is maximized, make it fill up the screen; if not, center it.
        if (maximized) {
            GLFW.glfwMaximizeWindow(window);
        } else {
            GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
            GLFW.glfwSetWindowPos(window, 
                (vidMode.width() - width) / 2,
                (vidMode.height() - height) / 2
            );
        }

        // Enables or disables VSync.
        if (getVSync()) GLFW.glfwSwapInterval(1);

        // Shows the window and establishes test codes and cull faces.
        GLFW.glfwShowWindow(window);

        GL.createCapabilities();

        // Enables some GL tests.
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_STENCIL_TEST);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }

    public void update() {
        GLFW.glfwSwapBuffers(window);
        GLFW.glfwPollEvents();
    }

    public void cleanup() {
        GLFW.glfwDestroyWindow(window);
    }

    // Updates the current projection matrix being stored in the window manager.
    public Matrix4f updateProjectionMatrix() {
        float aspectRatio = (float) width / height;
        return projectionMatrix.setPerspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
    }

    // Updates a new projection matrix without altering the projectionMatrix variable.
    public Matrix4f updateProjectionMatrix(Matrix4f mat, int width, int height) {
        float aspectRatio = (float) width / height;
        return mat.setPerspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
    }

    public void setClearColor(float r, float g, float b, float a) {
        GL11.glClearColor(r, g, b, a);
    }

    public boolean isKeyPressed(int key) {
        return GLFW.glfwGetKey(window, key) == GLFW.GLFW_PRESS;
    }

    public boolean windowShouldClose() {
        return GLFW.glfwWindowShouldClose(window);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        GLFW.glfwSetWindowTitle(window, title);
    }

    public boolean getVSync() {
        return vSync;
    }

    public boolean getResize() {
        return resize; 
    }

    public void setResize(boolean resize) {
        this.resize = resize; 
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height; 
    }

    public long getWindow() {
        return window;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }
}
