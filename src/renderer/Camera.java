package renderer;

import math.Mat4x4;
import math.Vec3;

public class Camera {

    private Mat4x4 transform = new Mat4x4();

    private Vec3 position = new Vec3();
    private Vec3 rotation = new Vec3();

    public void setPosition(Vec3 position) {
        this.position = position;
    }

    public void setRotation(Vec3 rotation) {
        this.rotation = rotation;
    }

    public Vec3 getPosition() {
        return position;
    }

    public Vec3 getRotation() {
        return rotation;
    }

    public Mat4x4 getTransform() {
        return transform.identity()
                .translate(new Vec3(-position.x, -position.y, -position.z))
                .rotateY(-rotation.y)
                .rotateX(-rotation.x);
    }
}
