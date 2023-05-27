package mesh;

import math.Vec3;

import java.util.Random;

public class SimpleNoise {

    private final Random rng = new Random();

    public double generate(double x, double z) {
        return ((Math.cos(x) * Math.sin(z) * 0.5 + 0.5));
    }

}
