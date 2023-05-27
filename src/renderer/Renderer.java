package renderer;

import math.Mat4x4;
import math.Vec3;
import math.Vec4;
import mesh.Mesh;
import renderer.framebuffer.DepthBuffer;
import renderer.framebuffer.Framebuffer;

import java.util.HashMap;

public class Renderer {

    private Mat4x4 projection;

    private Mat4x4 transformation = new Mat4x4();

    protected Framebuffer colorBuffer;
    protected Framebuffer depthBuffer;

    private Camera camera;

    private boolean wireframe;

    public void resize(int w, int h) {
        this.colorBuffer.resize(w, h);
        this.depthBuffer.resize(w, h);
    }

    public void setColorBuffer(Framebuffer colorBuffer) {
        this.colorBuffer = colorBuffer;
    }

    public void setDepthBuffer(Framebuffer depthBuffer) {
        this.depthBuffer = depthBuffer;
    }

    public void setProjection(Mat4x4 projection) {
        this.projection = projection;
    }

    public void setTransformation(Mat4x4 transformation) {
        this.transformation = transformation;
    }

    public Mat4x4 getTransformation() {
        return transformation;
    }

    public void setFs(FragmentShader fs) {
        this.fs = fs;
    }

    public void setVs(VertexShader vs) {
        this.vs = vs;
    }

    public VertexShader getVs() {
        return vs;
    }

    public FragmentShader getFs() {
        return fs;
    }

    public Renderer() {
        this.wireframe = true;
    }

    private HashMap<String, Object> additionalFragmentInputs = new HashMap<>();

    public void addFragmentShaderUniform(String name, Object obj) {
        additionalFragmentInputs.put(name, obj);
    }

    private VertexShader vs = (view, transform, proj, vertex, color) -> {
      VertexShaderOutput vso = new VertexShaderOutput();
      vso.color = color;
      vso.vertex = vertex;
      vso.preProjection = new Mat4x4(view).mul(transform).mul(new Vec4(vso.vertex, 1.0)).getV3();
      vso.projection = projection.mul(new Vec4(vso.preProjection, 1.0)).project();

      return vso;
    };

    private FragmentShader fs = (vso) -> {
        Vec3 N = vso.normal;
        Vec3 c = vso.color;
        Vec3 sun = new Vec3(-1,1,-1).normalize();

        DepthBuffer shadowMap = (DepthBuffer) additionalFragmentInputs.get("shadowMap");
        Mat4x4 lightMap = (Mat4x4) additionalFragmentInputs.get("lightMatrix");
        Vec4 lightSpaceVertex = lightMap.mul(transformation.mul(new Vec4(vso.vertex, 1.0)));
        Vec3 pointOnShadowMap = projection.mul(lightSpaceVertex).project();
        double currentDepth = pointOnShadowMap.z;
        pointOnShadowMap.mul(0.5).add(new Vec3(0.5, 0.5, 0.5));
        int px = (int) (pointOnShadowMap.x * shadowMap.getWidth());
        int py = (int) (pointOnShadowMap.y * shadowMap.getHeight());
        double nearestDepth = shadowMap.getData(px, py).x;

        if(nearestDepth + 0.001 < currentDepth) {
            return c.mul(0.2);
        }
        return c.mul(Math.max(N.dot(sun), 0.2));
    };

    public void render(Mesh mesh) {
        Vec3[] vertices = mesh.getVertices();
        Vec3[] colors = mesh.getColors();
        int[] indices = mesh.getIndices();
        Mat4x4 view = camera.getTransform();
        for(int i = 0; i < indices.length; i += 3) {
            Vec3 v0 = vertices[indices[i]];
            Vec3 v1 = vertices[indices[i+1]];
            Vec3 v2 = vertices[indices[i+2]];
            FaceInfo fi = new FaceInfo();
            fi.vso0 = vs.compute(view, transformation, projection, v0, colors[indices[i]]);
            fi.vso1 = vs.compute(view, transformation, projection, v1, colors[indices[i+1]]);
            fi.vso2 = vs.compute(view, transformation, projection, v2, colors[indices[i+2]]);

            Vec3 a = new Vec3(fi.vso0.preProjection).sub(fi.vso1.preProjection);
            Vec3 b = new Vec3(fi.vso2.preProjection).sub(fi.vso1.preProjection);
            Vec3 N = a.cross(b).normalize();
            fi.vso0.normal = N;
            fi.vso1.normal = N;
            fi.vso2.normal = N;

            iterateRow(fi);
        }
    }

    public void clear() {
        this.colorBuffer.clear();
        this.depthBuffer.clear();
    }

    private int getMin(double a, double b, double c, double ar, int max) {
        int min = getX(Math.min(a, Math.min(b, c)), ar, max);
        return Math.min(max, Math.max(0, min));
    }
    private int getMax(double a, double b, double c, double ar, int max) {
        int min = getX(Math.max(a, Math.max(b,c)), ar, max);
        return Math.min(max, Math.max(0, min));
    }

    private int getX(double a, double ar, int max) {
        return (int) ((a / ar * 0.5 + 0.5) * max);
    }

    private void iterateRow(FaceInfo fi) {
        double ar = (double) depthBuffer.getWidth() / (double) depthBuffer.getHeight();
        int minX = getMin(fi.vso0.projection.x,fi.vso1.projection.x,fi.vso2.projection.x, ar, depthBuffer.getWidth());
        int maxX = getMax(fi.vso0.projection.x,fi.vso1.projection.x,fi.vso2.projection.x, ar, depthBuffer.getWidth());
        for(int x = minX; x < maxX; x++) {
            iterateColumn(fi, x);
        }
    }

    private void iterateColumn(FaceInfo fi, int x) {
        double ar = (double) depthBuffer.getWidth() / (double) depthBuffer.getHeight();
        int minY = getMin(fi.vso0.projection.y,fi.vso1.projection.y,fi.vso2.projection.y, 1, depthBuffer.getHeight());
        int maxY = getMax(fi.vso0.projection.y,fi.vso1.projection.y,fi.vso2.projection.y, 1, depthBuffer.getHeight());
        for(int y = minY; y < maxY; y++) {
            Vec3 p = new Vec3(x, y, 0);
            Vec3 v0p = new Vec3(getX(fi.vso0.projection.x, ar, depthBuffer.getWidth()),
                    getX(fi.vso0.projection.y, 1, depthBuffer.getHeight()), 0);
            Vec3 v1p = new Vec3(getX(fi.vso1.projection.x, ar, depthBuffer.getWidth()),
                    getX(fi.vso1.projection.y, 1, depthBuffer.getHeight()), 0);
            Vec3 v2p = new Vec3(getX(fi.vso2.projection.x, ar, depthBuffer.getWidth()),
                    getX(fi.vso2.projection.y, 1, depthBuffer.getHeight()), 0);
            double det = computeDet(v0p, v1p, v2p);
            double L1 = computeL1(v0p, v1p, v2p, p) / det;
            double L2 = computeL2(v0p, v1p, v2p, p) / det;
            double L3 = 1.0 - L1 - L2;

            if (!isInsideTriangle(L1, L2, L3))
                continue;

            VertexShaderOutput vso = fi.computeFragmentShaderInput(L1, L2, L3);
            sendTriangle(vso, x, y);
        }
    }

    protected void sendTriangle(VertexShaderOutput vso, int x, int y) {
        if(vso.projection.z > depthBuffer.getData(x, y).x)
            return;
        colorBuffer.setData(x, y, fs.compute(vso));
        depthBuffer.setData(x, y, new Vec3(vso.projection.z,0,0));
    }
    private boolean isInsideTriangle(double L1, double L2, double L3) {
        if(L1 < 0 || L1 > 1 ||
                L2 < 0 || L2 > 1 ||
                L3 < 0 || L3 > 1) return false;
        return (L1 < 0.05 || L2 < 0.05 ||  L3 <  0.05) || wireframe;
    }

    private double computeL1(Vec3 v1, Vec3 v2, Vec3 v3, Vec3 p) {
        return (v2.y - v3.y) * (p.x - v3.x) + (v3.x - v2.x) * (p.y - v3.y);
    }

    private double computeL2(Vec3 v1, Vec3 v2, Vec3 v3, Vec3 p) {
        return (v3.y - v1.y) * (p.x - v3.x) + (v1.x - v3.x) * (p.y - v3.y);
    }

    private double computeDet(Vec3 v1, Vec3 v2, Vec3 v3) {
        return (v2.y - v3.y) * (v1.x - v3.x) + (v3.x - v2.x) * (v1.y - v3.y);
    }

    public void setWireframe(boolean wireframe) {
        this.wireframe = !wireframe;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }
}
