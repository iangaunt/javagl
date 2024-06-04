package javagl.core.ui;

import java.util.ArrayList;
import java.util.List;
import javagl.core.ObjectLoader;
import javagl.core.entity.Model;

public class UI {
    private ObjectLoader loader;
    private List<UITemplate> templates;

    private int[] rectangleIndices = {0, 1, 3, 0, 2, 3};
    float[] textureCoords = {0, 0, 0, 1, 1, 1, 1, 0};
    
    public UI(ObjectLoader loader) {
        this.loader = loader;
        this.templates = new ArrayList<UITemplate>();
    }

    public UI(ObjectLoader loader, List<UITemplate> templates) {
        this.loader = loader;
        this.templates = templates;
    }

    public Model renderUI(UITemplate t) throws Exception {
        float x = t.getX();
        float y = t.getY();

        float dx = t.getWidth();
        float dy = t.getHeight();

        float[] vertices = {
            x - 0.5f, y + dy - 0.5f, 0f,
            x - 0.5f, y - 0.5f, 0f,
            x + dx - 0.5f, y - 0.5f, 0f,
            x + dx - 0.5f, y + dy - 0.5f, 0f,
        };

        return loader.loadModel(vertices, textureCoords, rectangleIndices);
    }
}
