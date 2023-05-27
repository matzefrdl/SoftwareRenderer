package mesh;

import math.Vec3;

public class Terrain {
    private Mesh mesh;

    public Terrain(int N, double size) {

        Vec3[] vertices = new Vec3[N*N];
        Vec3[] colors = new Vec3[N*N];

        double h = size / (N-1);

        for(int x = 0; x < N; x++) {
            for(int z = 0; z < N; z++) {
                double height = ((Math.cos(x*h * 2) * Math.sin(z*h * 2) * 0.5 + 0.5));
                vertices[z * N + x] = new Vec3(x * h, height, z * h);
                colors[z * N +x] = new Vec3(0.5, 0.8, 1);
            }
        }
        int K = N - 1;
        int[] indices = new int[K*K*6];
        int ptr = 0;
        for(int x = 0; x < K; x++) {
            for (int z = 0; z < K; z++) {
                int i0 = z * N + x;
                int i1 = z * N + (x+1);
                int i2 = (z+1) * N + (x+1);
                int i3 = (z+1) * N + x;
                indices[ptr++] = i0;
                indices[ptr++] = i1;
                indices[ptr++] = i2;
                indices[ptr++] = i2;
                indices[ptr++] = i3;
                indices[ptr++] = i0;
            }
        }
        this.mesh = new Mesh(indices, vertices, colors);
    }

    public Mesh getMesh() {
        return mesh;
    }
}
