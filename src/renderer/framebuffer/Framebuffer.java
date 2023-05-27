package renderer.framebuffer;

import math.Vec3;

import java.awt.image.BufferedImage;

public interface Framebuffer {

    int getWidth();
    int getHeight();

    void resize(int w, int h);

    void setData(int x, int y, Vec3 data);
    Vec3 getData(int x, int y);
    void save(String path);

    Object getHandle();

    void clear();

    BufferedImage getAWTImage();
}
