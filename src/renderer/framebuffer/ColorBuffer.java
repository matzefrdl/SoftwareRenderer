package renderer.framebuffer;

import math.Vec3;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ColorBuffer implements Framebuffer {

    private BufferedImage image;

    public ColorBuffer(int w, int h) {
        this.image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
    }

    @Override
    public void resize(int w, int h) {
        this.image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
    }

    @Override
    public int getWidth() {
        return image.getWidth();
    }

    @Override
    public int getHeight() {
        return image.getHeight();
    }

    @Override
    public void setData(int x, int y, Vec3 data) {
        float r = (float) Math.max(0, Math.min(1.0, data.x));
        float g = (float) Math.max(0, Math.min(1.0, data.y));
        float b = (float) Math.max(0, Math.min(1.0, data.z));
        this.image.setRGB(x, y, new Color(r,g,b).getRGB());
    }

    @Override
    public Vec3 getData(int x, int y) {
        Color c = new Color(this.image.getRGB(x, y));
        return new Vec3(c.getRed() / 255.0, c.getGreen() / 255.0, c.getBlue() / 255.0);
    }

    @Override
    public void save(String path) {
        try {
            ImageIO.write(this.image, "PNG", new File(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object getHandle() {
        return image;
    }

    @Override
    public void clear() {
        this.image = new BufferedImage(this.image.getWidth(), this.image.getHeight(), BufferedImage.TYPE_INT_RGB);
    }

    @Override
    public BufferedImage getAWTImage() {
        return image;
    }
}
