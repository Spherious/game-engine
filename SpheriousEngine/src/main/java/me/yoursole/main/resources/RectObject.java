package me.yoursole.main.resources;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class RectObject extends GameObject {
    private int[] xyLower;
    private int[] xyUpper;

    public RectObject(int x1, int y1, int x2, int y2, float bouncyness, boolean isWinner, boolean isVisible, int sizeSet, boolean kills, boolean isCollidable, Runnable custom, String texturePath, boolean compute) {
        super(bouncyness, isWinner, isVisible, sizeSet, kills, isCollidable, custom, texturePath, compute);

        this.xyLower = new int[]{x1, y1};
        this.xyUpper = new int[]{x2, y2};

    }

    public void setXys(int x1, int y1, int x2, int y2) {
        this.xyLower = new int[]{x1, y1};
        this.xyUpper = new int[]{x2, y2};
    }

    public int[] getXyLower() {
        return this.xyLower;
    }

    public int[] getXyUpper() {
        return this.xyUpper;
    }


    @Override
    public boolean insideObject(Point p) {
        int x = p.x;
        int y = p.y;

        return (x > this.xyLower[0] && x < this.xyUpper[0]) && (y < this.xyUpper[1] && y > this.xyLower[1]);
    }

    @Override
    public Graphics2D paint(Graphics2D g2) {
        RectObject r = this;

        if (r.isVisible()) {
            Rectangle2D.Double rect = new Rectangle2D.Double(r.getXyLower()[0], r.getXyLower()[1], r.getXyUpper()[0] - r.getXyLower()[0], r.getXyUpper()[1] - r.getXyLower()[1]);
            BufferedImage b = null;

            try {
                b = ImageIO.read(r.getTexture());
            } catch (IOException | IllegalArgumentException ignored) {
            }

            Paint paint = b != null ? new TexturePaint(b, rect) : new GradientPaint(0, 0, Color.BLACK, 1, 1, r.isKills() ? Color.RED : Color.BLUE);
            g2.setPaint(paint);

            g2.fill(rect);
            g2.setPaint(null);
            g2.setColor(Color.BLACK);

        }
        return g2;
    }
}
