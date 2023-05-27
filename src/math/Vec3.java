package math;

public class Vec3 {

    public double x, y, z;

    public Vec3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public Vec3(Vec2 v, double z) {
        this.x = v.x;
        this.y = v.y;
        this.z = z;
    }

    public Vec3(Vec3 v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }

    public Vec3 cross(Vec3 other) {

        /*
        x   x
        y   y
        z   z
        y*z -
         */

        double x = this.y * other.z - this.z * other.y;
        double y = this.z * other.x - this.x * other.z;
        double z = this.x * other.y - this.y * other.x;
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public double dot(Vec3 other) {
        return other.x * x + other.y * y + other.z * z;
    }

    public Vec3 add(Vec3 other) {
        this.x += other.x;
        this.y += other.y;
        this.z += other.z;
        return this;
    }
    public Vec3 sub(Vec3 other) {
        this.x -= other.x;
        this.y -= other.y;
        this.z -= other.z;
        return this;
    }

    public Vec3 mul(double scalar) {
        this.x *= scalar;
        this.y *= scalar;
        this.z *= scalar;
        return this;
    }
    public Vec3 mul(double scalarX, double scalarY, double scalarZ) {
        this.x *= scalarX;
        this.y *= scalarY;
        this.z *= scalarZ;
        return this;
    }

    public Vec3 normalize() {
        return this.mul((double) (1.0 / Math.sqrt(this.dot(this))));
    }

    public Vec2 getV2() {
        return new Vec2(x,y);
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + ", " + z + "]";
    }
}
