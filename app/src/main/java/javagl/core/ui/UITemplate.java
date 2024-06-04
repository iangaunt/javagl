package javagl.core.ui;

import org.joml.Vector4f;

public class UITemplate {
    private float width, height;
    private float x, y;

    private Vector4f color;

    public UITemplate(float x, float y, float width, float height, Vector4f color) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public UITemplate(float x, float y, float width, float height) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.color = new Vector4f(0.0f, 0.0f, 0.0f, 0.0f);
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Vector4f getColor() {
        return color;
    }
}
