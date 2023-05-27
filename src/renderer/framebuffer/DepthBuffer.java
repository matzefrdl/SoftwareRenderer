package renderer.framebuffer;

import math.Vec3;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class DepthBuffer implements Framebuffer {


    private int width;
    private int height;

    private double[] data;

    public DepthBuffer(int width, int height) {
        this.width = width;
        this.height = height;
        this.data = new double[width * height];
        clear();
    }

    @Override
    public void resize(int w, int h) {
        this.data = new double[w*h];
        clear();
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void setData(int x, int y, Vec3 data) {
        this.data[y * width + x] = data.x;
    }

    @Override
    public Vec3 getData(int x, int y) {
        double v = this.data[y * width + x];
        return new Vec3(v,v,v);
    }

    @Override
    public void save(String path) {
        BufferedImage bImg = getAWTImage();
        try {
            ImageIO.write(bImg, "PNG", new File(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object getHandle() {
        return data;
    }

    @Override
    public void clear() {
        Arrays.fill(data, 2);
    }

    @Override
    public BufferedImage getAWTImage() {
        BufferedImage bImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for(int i = 0; i < width; i++) {
            for (int j = 0;j < height; j++) {
                double x = getData(i, j).x;
                double ndc = x * 2.0 - 1.0;
                double ld = (2.0 * 1.0 * 100) / (100 + 0.1 - ndc * (100 - 0.1));

                float r =(float) Math.max(0, Math.min(1.0, ld/100.0f));
                bImg.setRGB(i, j, new Color(r,r,r).getRGB());
            }
        }
        return bImg;
    }
}
