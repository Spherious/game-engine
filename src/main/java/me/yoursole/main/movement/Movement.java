package me.yoursole.main.movement;

import me.yoursole.main.resources.Player;

import java.awt.*;

public class Movement {

    public static void applyMovement(Point rel, Player p) {
        float dx = p.getMovement().getX();
        float dy = p.getMovement().getY();

        dx += (distancex(p.getLocx(), p.getLocy(), rel.x, rel.y) / 100);
        dy += (distancey(p.getLocx(), p.getLocy(), rel.x, rel.y) / 100);

        dx /= 1.1;
        dy /= 1.1;

        p.getMovement().setX(dx);
        p.getMovement().setY(dy);
    }
    private static float distancex(double x, double y, double x2, double y2) {
        return (float) (x2 - x);
    }

    private static float distancey(double x, double y, double x2, double y2) {
        return (float) (y2 - y);
    }
}
