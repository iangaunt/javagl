package javagl.core.entity;

/** A class for handling textures. */
public class Texture {
    // The ID of the texture.
    private final int id;

    /**
     * Initializes a new texture with a new ID.
     * 
     * @param id - The ID of the texture.
     */
    public Texture(int id) {
        this.id = id;
    }
    
    /**
     * The getter method for the texture ID.
     * 
     * @return - The texture ID.
     */
    public int getId() {
        return id;
    }
}
