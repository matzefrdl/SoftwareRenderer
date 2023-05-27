package renderer;

import math.Mat4x4;
import renderer.framebuffer.DepthBuffer;

public class ShadowMapInfo {

    private DepthBuffer db;
    private Camera lightCamera;

    public ShadowMapInfo(DepthBuffer db, Camera lightCamera) {
        this.db = db;
        this.lightCamera = lightCamera;
    }

    public DepthBuffer getDb() {
        return db;
    }

    public Camera getLightCamera() {
        return lightCamera;
    }
}
