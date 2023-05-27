package math;

public class Vec2 {

    public double x, y;

    public Vec2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vec2() {
        this.x = 0;
        this.y = 0;
    }

    public Vec2(Vec2 v) {
        this.x = v.x;
        this.y = v.y;
    }

    public double dot(Vec2 other) {
        return other.x * x + other.y * y;
    }

    public Vec2 add(Vec2 other) {
        this.x += other.x;
        this.y += other.y;
        return this;
    }
    public Vec2 sub(Vec2 other) {
        this.x -= other.x;
        this.y -= other.y;
        return this;
    }

    public Vec2 mul(double scalar) {
        this.x *= scalar;
        this.y *= scalar;
        return this;
    }
}
