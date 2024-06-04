package javagl.core;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import javagl.core.entity.Model;
import javagl.core.utils.Utils;

public class ObjectLoader {
    // A list of VAOs (vertex array objects).
    private List<Integer> vaos = new ArrayList<Integer>();

    // A list of VBOs (vertex buffer objects).
    private List<Integer> vbos = new ArrayList<Integer>();

    /**
     * Loads a model from a set of vertices and a set
     * of indices, which tells the computer which order to 
     * render specific vertices.
     * 
     * @param vertices - The list of vertices. 
     * @param indices - The order of vertices to render in the "vertices" array.
     * @return - The completed Model object.
     */
    public Model loadModel(float[] vertices, int[] indices) {
        // Creates a new VAO with a new ID.
        int id = createVAO();

        // Stores the indices in a buffer and then makes an attribute list. 
        storeIndicesBuffer(indices);
        storeDataInAttributeList(0, 3, vertices);
        unbind();

        // Returns a new model with the proper amount of vertices.
        return new Model(id, vertices.length / 3);
    }

    /**
     * Creates a new vertex array object and adds it to
     * the running list.
     * 
     * @return - The id of the new VAO.
     */
    private int createVAO() {
        int id = GL30.glGenVertexArrays();
        vaos.add(id);
        GL30.glBindVertexArray(id);
        return id;
    }

    /**
     * Stores an array of indices into a vertex
     * buffer object and then stores the buffer into
     * an element array.
     * 
     * @param indices - The array of indices to store.
     */
    private void storeIndicesBuffer(int[] indices) {
        int vbo = GL15.glGenBuffers();
        vbos.add(vbo);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vbo);

        IntBuffer buffer = Utils.storeDataInIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    /**
     * Creates a new attribute list with a VBO and then stores
     * the attribute number and vertex count in tandem with the array
     * of float data.
     * 
     * @param attributeNumber - The attribute number of the vertex.
     * @param vertexCount - The numbers of vertices.
     * @param data - The vertex data.
     */
    private void storeDataInAttributeList(int attributeNumber, int vertexCount, float[] data) {
        int vbo = GL15.glGenBuffers();
        vbos.add(vbo);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);

        FloatBuffer buffer = Utils.storeDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeNumber, vertexCount, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    /** Unbinds the vertex array. */
    private void unbind() {
        GL30.glBindVertexArray(0);
    }

    /** Deletes all active VAO and VBO objects. */
    public void cleanup() {
        for (int vao : vaos) GL30.glDeleteVertexArrays(vao);
        for (int vbo : vbos) GL30.glDeleteBuffers(vbo);
    }
}
