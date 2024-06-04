package javagl;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import javagl.core.Logic;
import javagl.core.ObjectLoader;
import javagl.core.entity.Model;
import javagl.core.entity.Texture;
import javagl.core.managers.RenderManager;
import javagl.core.managers.WindowManager;

public class TestGame implements Logic {
    private int direction = 0;
    private float color = 0.0f;

    private final RenderManager renderer;
    private final WindowManager window;
    private final ObjectLoader loader;

    private Model model;

    public TestGame() {
        renderer = new RenderManager();
        window = App.getWindow();
        loader = new ObjectLoader();
    }

    @Override
    public void init() throws Exception {
        renderer.init();

        float[] vertices = {
            -0.5f,  0.5f, 0f,
            -0.5f, -0.5f, 0f,
             0.5f, -0.5f, 0f,
             0.5f,  0.5f, 0f,
        };

        int[] indices = {0, 1, 3, 3, 1, 2};

        float[] textureCoords = {
            0, 0,
            0, 1, 
            1, 1, 
            1, 0
        };

        model = loader.loadModel(vertices, textureCoords, indices);
        model.setTexture(new Texture(loader.loadTexture("textures/cobblestone.png")));
    }

    @Override
    public void input() {
        if (window.isKeyPressed(GLFW.GLFW_KEY_UP)) {
            direction = 1;
        } else if (window.isKeyPressed(GLFW.GLFW_KEY_DOWN)){ 
            direction = -1;
        } else {
            direction = 0;
        }
    }

    @Override
    public void update() {
        color += direction * 0.01f;

        if (color > 1.0f) color = 1.0f;
        if (color < 0.0f) color = 0.0f;
    }

    @Override 
    public void render() {
        if (window.getResize()) {
            GL11.glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResize(true);
        }

        window.setClearColor(color * 0.15f, color * 0.15f, color * 0.3f, 0.0f);
        renderer.render(model);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        loader.cleanup();
    }

    public RenderManager getRenderManager() {
        return renderer;
    }
}
