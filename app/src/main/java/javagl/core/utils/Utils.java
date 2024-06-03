package javagl.core.utils;

import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import org.lwjgl.system.MemoryUtil;

/** A utilities class for useful methods. */
public class Utils {
    /**
     * Stores a data of float values into a float buffer. Float
     * buffers are useful for accelerating graphics performance
     * with OpenGL and LWJGL.
     * 
     * @param data - A one-dimensional array of float values.
     * @return - A float buffer containing these values.
     */
    public static FloatBuffer storeDataInFloatBuffer(float[] data) {
        // Allocates memory for a float buffer that can fit data.
        FloatBuffer buffer = MemoryUtil.memAllocFloat(data.length);

        // Flips the buffer, enabling new read-write operations, and returns it.
        buffer.put(data).flip();
        return buffer;
    }

    /**
     * Stores a data of int values into a int buffer. Int
     * buffers are useful for accelerating graphics performance
     * with OpenGL and LWJGL.
     * 
     * @param data - A one-dimensional array of int values.
     * @return - A float buffer containing these values.
     */
    public static IntBuffer storeDataInIntBuffer(int[] data) {
        // Allocates memory for an int buffer that can fit data.
        IntBuffer buffer = MemoryUtil.memAllocInt(data.length);

        // Flips the buffer, enabling new read-write operations, and returns it.
        buffer.put(data).flip();
        return buffer;
    }

    public static String loadResource(String filename) throws Exception {
        String result;

        System.out.println(Utils.class.getResourceAsStream(filename));

        try (
            InputStream in = Utils.class.getResourceAsStream(filename);
            Scanner scanner = new Scanner(in, StandardCharsets.UTF_8.name())) {
                result = scanner.useDelimiter("\\A").next();
            }
        
        return result;
    }
}
