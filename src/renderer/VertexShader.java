package renderer;

import math.Mat4x4;
import math.Vec3;

public interface VertexShader {
    public VertexShaderOutput compute(Mat4x4 view, Mat4x4 transform, Mat4x4 perspective, Vec3 vertex, Vec3 color);
}
