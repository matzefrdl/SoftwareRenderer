package renderer;

import math.Vec3;

public class ShadowRenderer extends Renderer {

    @Override
    public void clear() {
        this.depthBuffer.clear();
    }

    @Override
    protected void sendTriangle(VertexShaderOutput vso, int x, int y) {
        if(vso.projection.z > depthBuffer.getData(x, y).x)
            return;
        depthBuffer.setData(x, y, new Vec3(vso.projection.z,0,0));
    }
}
