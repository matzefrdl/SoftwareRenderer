package math;

public class Mat4x4 {

    private double[][] data = new double[4][4];

    public Mat4x4() {
        identity();
    }

    public Mat4x4(Mat4x4 cpy) {

        for(int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                set(i,j, cpy.get(i,j));
            }
        }
    }

    public Mat4x4 mul(Mat4x4 other) {
        double[][] res = new double[4][4];
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {
                double sum = 0;
                for(int x = 0; x < 4; x++) {
                    sum += this.data[i][x] * other.get(x, j);
                }
                res[i][j] = sum;
            }
        }
        this.data = res;

        return this;
    }

    public Vec4 mul(Vec4 other) {
        double x = data[0][0] * other.x + data[0][1] * other.y + data[0][2] * other.z + data[0][3] * other.w;
        double y = data[1][0] * other.x + data[1][1] * other.y + data[1][2] * other.z + data[1][3] * other.w;
        double z = data[2][0] * other.x + data[2][1] * other.y + data[2][2] * other.z + data[2][3] * other.w;
        double w = data[3][0] * other.x + data[3][1] * other.y + data[3][2] * other.z + data[3][3] * other.w;
        other.x = x;
        other.y = y;
        other.z = z;
        other.w = w;
        return other;
    }

    public Mat4x4 translate(Vec3 v) {
        Mat4x4 tm = new Mat4x4();
        tm.set(0, 3, v.x);
        tm.set(1, 3, v.y);
        tm.set(2, 3, v.z);
        tm.mul(this);
        for(int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                set(i,j, tm.get(i,j));
            }
        }
        return this;
    }
    public Mat4x4 rotateY(double alpha) {
        Mat4x4 tm = new Mat4x4();
        tm.set(0, 0, (double) Math.cos(alpha));
        tm.set(2, 0, (double) -Math.sin(alpha));
        tm.set(0, 2, (double) Math.sin(alpha));
        tm.set(2, 2, (double) Math.cos(alpha));
        tm.mul(this);
        for(int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                set(i,j, tm.get(i,j));
            }
        }
        return this;
    }

    public Mat4x4 rotateX(double alpha) {
        Mat4x4 tm = new Mat4x4();
        tm.set(1, 1, Math.cos(alpha));
        tm.set(1, 2, -Math.sin(alpha));
        tm.set(2, 1, Math.sin(alpha));
        tm.set(2, 2, Math.cos(alpha));
        tm.mul(this);
        for(int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                set(i,j, tm.get(i,j));
            }
        }
        return this;
    }


    public static Mat4x4 perspective(double fovy, double aspect, double zNear, double zFar) {
        Mat4x4 proj = new Mat4x4();
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {
                proj.set(i,j,0);
            }
        }

        double h = Math.tan(fovy * 0.5);
        proj.set(0, 0, 1.0 / (aspect * h));
        proj.set(1, 1, 1.0 / h);
        proj.set(2, 2, (zFar + zNear) / (zNear - zFar));
        proj.set(2, 3, (zFar + zFar) * zNear / (zNear - zFar));
        proj.set(3, 2, -1.0);

        return proj;
    }

    public Mat4x4 identity() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                data[i][j] = 0;
            }
        }
        set(0, 0, 1);
        set(1, 1, 1);
        set(2, 2, 1);
        set(3, 3, 1);
        return this;
    }

    public Mat4x4 set(int i, int j, double value) {
        this.data[i][j] = value;
        return this;
    }

    public double get(int i, int j) {
        return this.data[i][j];
    }

}
