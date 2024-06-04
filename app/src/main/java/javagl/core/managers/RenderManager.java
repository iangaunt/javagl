package javagl.core.managers;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import javagl.App;
import javagl.core.entity.Model;
import javagl.core.utils.Utils;

/** The class for rendering components onto the screen. */
public class RenderManager {
    @SuppressWarnings("unused")
    // The currently running window containing the app.
    private final WindowManager window;

    // The currently running shader context.
    private ShaderManager shader;

    /** Initializes a new RenderManager by fetching the window from the app. */
    public RenderManager() {
        window = App.getWindow();
    }

    /** Initializes a new RenderManager. */
    public void init() throws Exception {
        shader = new ShaderManager();
        
        shader.createVertexShader(Utils.loadResource("/shaders/vertex.vs"));
        shader.createFragmentShader(Utils.loadResource("/shaders/fragment.fs"));

        shader.link();
    }

    /**
     * Renders a given Model to the screen by binding a new vertex array
     * to the GL context and drawing the model's vertex array to the screen.
     * 
     * @param model - The model to render.
     */
    public void render(Model model) {
        // Clears the current GL11 buffer.
        clear();
        
        shader.bind();

        // Binds the model's vertex array to the GL context.
        GL30.glBindVertexArray(model.getId());
        GL30.glEnableVertexAttribArray(0);
        
        // Draws the vertex array using GL_TRIANGLES and unbinds the vertex attribute array.
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, model.getVertexCount());
        GL20.glDisableVertexAttribArray(0);

        // Unbinds the model's vertex array from the GL context.
        GL30.glBindVertexArray(0);

        shader.unbind();
    }

    /**
     * Returns the running shader manager of the render engine.
     * 
     * @return - The current shader manager.
     */
    public ShaderManager getShaderManager() {
        return shader;
    }
    
    /** Clears the current buffer. */
    public void clear() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    /** Cleans up the RenderManager. */
    public void cleanup() {
        shader.cleanup();
    }
}
