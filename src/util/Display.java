package util;

import renderer.framebuffer.Framebuffer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Display extends JPanel {

    private Framebuffer framebuffer;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        BufferedImage bImg = this.framebuffer.getAWTImage();
        g.drawImage(bImg, 0, 0, this.getWidth(), this.getHeight(), null);
    }

    public Display(Framebuffer fb, RepaintCallback cb, int width, int height) {
        this.framebuffer = fb;
        JFrame frame = new JFrame("Display");
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.setResizable(false);
        frame.setVisible(true);

        long last = System.nanoTime();
        double targetFps = 30.0;
        double targetSleepTime = 1000.0 / targetFps;
        while(true) {
            long now = System.nanoTime();
            double dtInMs = (now - last) / 1_000_000.0;

            if(dtInMs >= targetSleepTime) {
                last = now;
                cb.repainted();
                repaint();
            }
        }
    }
}

