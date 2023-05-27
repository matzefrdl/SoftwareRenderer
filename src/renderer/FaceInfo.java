package renderer;

import math.Vec3;

public class FaceInfo {
    public VertexShaderOutput vso0;
    public VertexShaderOutput vso1;
    public VertexShaderOutput vso2;

    public VertexShaderOutput computeFragmentShaderInput(double l1, double l2, double l3) {
        VertexShaderOutput vso = new VertexShaderOutput();
        vso.projection = interpolate(l1, l2, l3, vso0.projection, vso1.projection, vso2.projection);
        vso.preProjection = interpolate(l1, l2, l3, vso0.preProjection, vso1.preProjection, vso2.preProjection);
        vso.vertex = interpolate(l1, l2, l3, vso0.vertex, vso1.vertex, vso2.vertex);
        vso.color = interpolate(l1, l2, l3, vso0.color, vso1.color, vso2.color);
        vso.normal = interpolate(l1, l2, l3, vso0.normal, vso1.normal, vso2.normal);
        return vso;
    }

    private Vec3 interpolate(double a, double b, double c, Vec3 v0, Vec3 v1, Vec3 v2) {
        return       new Vec3(v0).mul(a)
                .add(new Vec3(v1).mul(b))
                .add(new Vec3(v2).mul(c));
    }
}
