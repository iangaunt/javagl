package javagl.core.managers;

import java.util.HashMap;
import java.util.Map;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;

/** The manager class for shader functionality. */
public class ShaderManager {
    // The ID of the program.
    private final int programID;

    // The IDs of the vertex shader and the fragment shader.
    private int vertexShaderID, fragmentShaderID;

    private final Map<String, Integer> uniforms;

    /**
     * Initializes a new shader manager.
     * 
     * @throws Exception - An error caused by initialization of the shader program.
     */
    public ShaderManager() throws Exception {
        programID = GL20.glCreateProgram();
        if (programID == 0) throw new Exception("Could not create shader");

        uniforms = new HashMap<String, Integer>();
    }

    public void createUniform(String uniformName) throws Exception {
        int uniformLocation = GL20.glGetUniformLocation(programID, uniformName);
        if (uniformLocation < 0) throw new Exception("Could not find the uniform " + uniformName);

        uniforms.put(uniformName, uniformLocation);
    }

    public void setUniform(String uniformName, Matrix4f value) {
        try (
            MemoryStack stack = MemoryStack.stackPush()
        ) {
            GL20.glUniformMatrix4fv(
                uniforms.get(uniformName), false, 
                value.get(stack.mallocFloat(16))
            );
        }
    }

    public void setUniform(String uniformName, int value) {
        GL20.glUniform1i(uniforms.get(uniformName), value);
    }

    /**
     * Creates a new vertex shader from a given shader code.
     * 
     * @param shaderCode - The shader code of the vertex shader.
     * @throws Exception - An exception caused by the initialization of the vertex shader.
     */
    public void createVertexShader(String shaderCode) throws Exception {
        vertexShaderID = createShader(shaderCode, GL20.GL_VERTEX_SHADER);
    }

    /**
     * Creates a new fragment shader from a given shader code.
     * 
     * @param shaderCode - The shader code of the fragment shader.
     * @throws Exception - An exception caused by the initialization of the fragment shader.
     */
    public void createFragmentShader(String shaderCode) throws Exception {
        fragmentShaderID = createShader(shaderCode, GL20.GL_FRAGMENT_SHADER);
    }

    /**
     * Creates a new shader with a specified shader code and shader type, and then 
     * attaches it to the program.
     * 
     * @param shaderCode - The shader code for the new shader.
     * @param shaderType - The shader type for the new shader.
     * @return - The shader ID of the new shader.
     * @throws Exception - An error caused during shader initialization.
     */
    public int createShader(String shaderCode, int shaderType) throws Exception {
        // Creates a new shader ID from the specified shader type.
        int shaderID = GL20.glCreateShader(shaderType);
        if (shaderID == 0) throw new Exception("Error creating shader of type " + shaderType);

        // Compiles the shader object.
        GL20.glShaderSource(shaderID, shaderCode);
        GL20.glCompileShader(shaderID);
        if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == 0) 
            throw new Exception("Error compiling shader code of type " + shaderType);

        // Attaches to the shader ID to the program ID and returns it. 
        GL20.glAttachShader(programID, shaderID);
        return shaderID;
    }

    /**
     * Links the vertex shaders and fragment shaders to the program 
     * @throws Exception
     */
    public void link() throws Exception {
        GL20.glLinkProgram(programID);
        if (GL20.glGetProgrami(programID, GL20.GL_LINK_STATUS) == 0) throw new Exception("Error linking shader code");

        if (vertexShaderID != 0) GL20.glDetachShader(programID, vertexShaderID);
        if (fragmentShaderID != 0) GL20.glDetachShader(programID, fragmentShaderID);

        GL20.glValidateProgram(programID);
        if (GL20.glGetProgrami(programID, GL20.GL_VALIDATE_STATUS) == 0) 
            throw new Exception("Unable to validate shader code");
    }

    /**
     * Returns the current program ID of the shader engine.
     * 
     * @return - The current program ID.
     */
    public int getProgramId() {
        return programID;
    }

    /** Binds the program ID to the GL context. */
    public void bind() {
        GL20.glUseProgram(programID);
    }

    /** Unbinds the program ID from the GL context. */
    public void unbind() {
        GL20.glUseProgram(0);
    }

    /** Cleans up the shader manager. */
    public void cleanup() {
        unbind();
        if (programID != 0) GL20.glDeleteProgram(programID);
    }
}
