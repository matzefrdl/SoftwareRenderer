package renderer;

import math.Vec3;

public interface FragmentShader {

    Vec3 compute(VertexShaderOutput vso);

}
