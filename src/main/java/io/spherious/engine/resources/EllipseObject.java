package io.spherious.engine.resources;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class EllipseObject extends GameObject{
    private Point center;
    private float xR;
    private float yR;

    public EllipseObject(Point center, float xR, float yR, float bouncyness, boolean isWinner, boolean isVisible, int sizeSet, boolean kills, boolean isCollidable, Runnable custom, String texturePath, boolean compute) {
        super(bouncyness, isWinner, isVisible, sizeSet, kills, isCollidable, custom, texturePath, compute);

        this.center = center;
        this.xR = xR;
        this.yR = yR;

    }

    @Override
    public boolean insideObject(Point p) {
        //Ellipse Formula
        //((x-centerX)^2)/xRadius^2  +  ((y-centerY)^2)/yRadius^2 = 1

        double xComponent = (Math.pow(p.x - this.center.x, 2) / Math.pow(this.xR, 2));
        double yComponent = (Math.pow(p.y - this.center.y, 2) / Math.pow(this.yR, 2));
        return xComponent+yComponent <= 1;
    }

    @Override
    public Graphics2D paint(Graphics2D g2) {
        EllipseObject r = this;

        //upper left is centerX-xradius, centerY-yradius
        //width is 2*xradius
        //height is 2*yradius

        if (r.isVisible()) {
            Ellipse2D ellipse = new Ellipse2D.Double(this.center.x - this.xR,this.center.y - this.yR,this.xR*2, this.yR*2);
            BufferedImage b = null;

            try {
                b = ImageIO.read(r.getTexture());
            } catch (IOException | IllegalArgumentException ignored) {
            }

            Paint paint = new GradientPaint(0, 0, Color.BLACK, 1, 1, r.isKills() ? Color.RED : Color.BLUE);
            g2.setPaint(paint);

            g2.fill(ellipse);
            g2.setPaint(null);
            g2.setColor(Color.BLACK);

        }
        return g2;
    }
}
