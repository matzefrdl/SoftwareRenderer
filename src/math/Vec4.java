package math;

public class Vec4 {

    public double x, y, z, w;

    public Vec4(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vec4() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public Vec4(Vec3 v, double w) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        this.w = w;
    }

    public Vec4(Vec4 v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        this.w = v.w;
    }

    public Vec3 project() {
        return new Vec3(x / w, y / w, z / w);
    }

    public Vec3 getV3() {
        return new Vec3(x,y,z);
    }
}
