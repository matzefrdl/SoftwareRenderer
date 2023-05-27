import math.Mat4x4;
import math.Vec3;
import mesh.Mesh;
import mesh.Terrain;
import renderer.Camera;
import renderer.ShadowMapInfo;
import renderer.ShadowRenderer;
import renderer.framebuffer.ColorBuffer;
import renderer.framebuffer.DepthBuffer;
import renderer.Renderer;
import util.Display;
import util.STLLoader;

public class Main {
    public static void main(String[] args) {

        Mat4x4 mat4x4 = new Mat4x4();
        Mat4x4 monkeyTrans = new Mat4x4();

        mat4x4.identity();

        Mesh mesh = new Terrain(30, 10).getMesh();
        Mesh monkey = new STLLoader().load("src/monkey.stl");

        ColorBuffer cb = new ColorBuffer(1280, 720);
        DepthBuffer db = new DepthBuffer(cb.getWidth(), cb.getHeight());

        DepthBuffer shadowBuffer = new DepthBuffer(1024, 1024);

        Camera cam = new Camera();
        cam.getPosition().y = -2;
        cam.getPosition().z = 10;
        cam.getPosition().x = 5;
        cam.getRotation().x = Math.PI / 8.0;

        Camera lightCamera = new Camera();
        lightCamera.setPosition(new Vec3(11.0, -11, 11));
        lightCamera.setRotation(new Vec3(Math.PI / 4, Math.PI / 4, 0));

        Renderer renderer = new Renderer();
        renderer.setProjection(Mat4x4.perspective(Math.toRadians(70.0), 1, 0.1f, 100.0));
        renderer.setColorBuffer(cb);
        renderer.setDepthBuffer(db);
        renderer.setCamera(cam);

        ShadowRenderer shadowRenderer = new ShadowRenderer();
        shadowRenderer.setProjection(Mat4x4.perspective(Math.toRadians(70.0), 1, 0.1f, 100.0));
        shadowRenderer.setColorBuffer(null);
        shadowRenderer.setDepthBuffer(shadowBuffer);
        shadowRenderer.setCamera(lightCamera);

        double[] x = new double[1];
        x[0] = 0;
        new Display(cb, () -> {
            x[0] += 0.02f;
            shadowRenderer.clear();
                shadowRenderer.setTransformation(mat4x4);
                shadowRenderer.render(mesh);
               // monkeyTrans.identity()
              //          .rotateX(x[0])
              //          .rotateY(x[0])
              //          .translate(new Vec3(5, -1.5, 5));
             //   shadowRenderer.setTransformation(monkeyTrans);
             //   shadowRenderer.render(monkey);

            renderer.addFragmentShaderUniform("shadowMap", shadowBuffer);
            renderer.addFragmentShaderUniform("lightMatrix", lightCamera.getTransform());
            renderer.clear();
                renderer.setTransformation(mat4x4);
                renderer.render(mesh);
              //  monkeyTrans.identity()
             //           .rotateX(x[0])
             //           .rotateY(x[0])
             //           .translate(new Vec3(5, -1.5, 5));
             //   renderer.setTransformation(monkeyTrans);
             //   renderer.render(monkey);
        }, 1280, 720);
    }
}