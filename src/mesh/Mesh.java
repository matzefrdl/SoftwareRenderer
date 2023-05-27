package mesh;

import math.Vec3;

public class Mesh {

    private int[] indices;
    private Vec3[] vertices;

    private Vec3[] colors;

    public Mesh(int[] indices, Vec3[] vertices, Vec3[] colors) {
        this.indices = indices;
        this.vertices = vertices;
        this.colors = colors;
    }

    public Vec3[] getColors() {
        return colors;
    }

    public int[] getIndices() {
        return indices;
    }

    public Vec3[] getVertices() {
        return vertices;
    }
}
