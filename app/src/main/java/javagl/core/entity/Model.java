package javagl.core.entity;

/** A class for handling 2D or 3D models. */
public class Model {
    // The id of the model.
    private int id;

    // The vertex count of the model.
    private int vertexCount;

    /**
     * Initializes a new model with a specified id
     * and a set number of vertices.
     * 
     * @param id - The ID of the model.
     * @param vertexCount - The number of vertices of the model.
     */
    public Model(int id, int vertexCount) {
        this.id = id;
        this.vertexCount = vertexCount;
    }

    /**
     * The getter method for the model id.
     * @return - The model id.
     */
    public int getId() {
        return id;
    }

    /**
     * The getter method for the vertex count.
     * @return - The number of vertices of the model.
     */
    public int getVertexCount() {
        return vertexCount;
    }
}
