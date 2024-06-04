package javagl;

import java.io.File;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import javagl.core.Logic;
import javagl.core.ObjectLoader;
import javagl.core.entity.Model;
import javagl.core.entity.Texture;
import javagl.core.managers.RenderManager;
import javagl.core.managers.WindowManager;
import javagl.core.ui.UI;
import javagl.core.ui.UITemplate;

public class TestGame implements Logic {
    private int direction = 0;
    private float color = 0.0f;

    private final RenderManager renderer;
    private final WindowManager window;
    private final ObjectLoader loader;

    private Model model;

    UI ui;

    public TestGame() {
        renderer = new RenderManager();
        window = App.getWindow();
        loader = new ObjectLoader();

        ui = new UI(loader);
    }

    @Override
    public void init() throws Exception {
        renderer.init();

        // model = loader.loadModel(vertices, textureCoords, indices);
        // model.setTexture(new Texture(loader.loadTexture("C:\\Users\\ianga\\Desktop\\Codespaces\\javagl\\app\\src\\main\\resources\\textures\\cobblestone.png")));

        model = ui.renderUI(new UITemplate(0.4f, 0.1f, 0.2f, 0.6f));

        System.out.println("initialized");
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
