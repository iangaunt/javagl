package javagl.core.managers;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
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
        
        // Creates two new shaders (vertex + fragment).
        shader.createVertexShader(Utils.loadResource("/shaders/vertex.vs"));
        shader.createFragmentShader(Utils.loadResource("/shaders/fragment.fs"));

        // Links the shader to the window.
        shader.link();

        // Creates a new uniform for the texture sampler.
        shader.createUniform("textureSampler");
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
        
        // Sets the value of the texture sampler and binds the shader.
        shader.bind();
        shader.setUniform("textureSampler", 0);

        // Binds the model's vertex array to the GL context.
        GL30.glBindVertexArray(model.getId());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);

        // Binds the model's texture to the GL context.
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        if (model.getTexture() != null) GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getId());

        // Draws the vertex array using GL_TRIANGLES and unbinds the vertex attribute array.
        GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);

        // Unbinds the model's vertex array from the GL context.
        GL30.glBindVertexArray(0);

        // Unbinds the shader.
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
