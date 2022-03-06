package io.spherious.engine.collision;

import io.spherious.engine.math.Vector2D;
import io.spherious.engine.resources.GameObject;
import io.spherious.engine.resources.Player;


import java.awt.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;


public class Collision {
    public static boolean collideWithObject(GameObject r, Player p) {
        if (!r.compute()) //for backgrounds and sprites
            return false;

        ArrayList<Point> pointsInsidePlayer = p.getPointsInside();

        Vector2D playerVel = p.getMovement(),
                b = new Vector2D(0, 0),
                bouncy = new Vector2D(r.getBouncyness(), r.getBouncyness()),
                centerOfPlayer = new Vector2D((float) p.getLocx(), (float) p.getLocy());

        int amountOfPointsInPlayerAndObject = 0;
        ArrayList<Point> pointsInPlayerAndObject = new ArrayList<>();
        for (Point point : pointsInsidePlayer) {
            if (r.insideObject(point)) { //point is inside both the player and the object in question
                Vector2D pointCenter = Vector2D.subtract(centerOfPlayer, new Vector2D(point.x, point.y));
                b.add(pointCenter);
                amountOfPointsInPlayerAndObject++;
                pointsInPlayerAndObject.add(point);
            }
        }

        if (amountOfPointsInPlayerAndObject != 0) {
            if (r.isCollidable()) {

                //player location
                Optional<Point> points = pointsInPlayerAndObject.stream().min((o1, o2) -> {
                    float d1 = distanceSquared(o1.x, o1.y, p.getLocx(), p.getLocy());
                    float d2 = distanceSquared(o2.x, o2.y, p.getLocx(), p.getLocy());
                    return Float.compare(d1, d2);
                });

                Point closest = points.orElseGet(Point::new);

                float minDist = distance(closest.x,closest.y,p.getLocx(),p.getLocy());

                minDist = (p.getSize() / 2f - minDist) * 2;

                b.normalize();

                b.multiply(new Vector2D(minDist, minDist));


                Vector2D playerLoc = new Vector2D((float) p.getLocx(), (float) p.getLocy());
                playerLoc.add(b);

                p.setLocx((int) (playerLoc.getX()));
                p.setLocy((int) (playerLoc.getY()));

                b.normalize();
                //player velocity
                //calc new direction
                Vector2D vec1 = playerVel.copy();
                Vector2D vec2 = new Vector2D(b.getY(), -1*b.getX());
                Vector2D normal = b.copy();

                double dpa = vec1.dot(vec2);
                Vector2D pra = vec2.multiply(dpa);
                double dpb = vec1.dot(normal);
                Vector2D prb = normal.multiply(dpb);
                pra.subtract(prb);

                if(normal.getX()>0 && normal.getY()<0){
                    vec2 = vec2.multiply(-1);

                    dpa = vec1.dot(vec2);
                    pra = vec2.multiply(dpa);
                    dpb = vec1.dot(normal);
                    prb = normal.multiply(dpb);
                    pra.subtract(prb);
                }

                pra = pra.multiply(bouncy);

                p.setMovement(pra);

            }
            return true;
        }

        return false;

    }

    public static void collideWithWall(Player p, float dimx, float dimy, float compression) {
        float halfSize = p.getSize() / 2f;
        if (p.getLocx() + halfSize >= dimx || p.getLocx() - halfSize < 0) { //side wall
            p.getMovement().setX(p.getMovement().getX() * -1);
            p.setLocx(p.getLocx() < dimx / 2f ? (int) (p.getSize() / 2f + (compression)) : (int) (dimx - p.getSize() / 2f - (compression)));
        }

        if (p.getLocy() + halfSize >= dimy || p.getLocy() - halfSize < 0) { //top/bottom wall
            p.getMovement().setY(p.getMovement().getY() * -1);
            p.setLocy((int) (p.getLocy() < dimy / 2f ? p.getSize() / 2f + (compression) : dimy - p.getSize() / 2f - (compression)));
        }
    }

    private static float distanceSquared(double x, double y, double x2, double y2) {
        return (float) ((float) Math.pow(y2 - y, 2) + Math.pow(x2 - x, 2));
    }

    private static float distance(double x, double y, double x2, double y2) {
        return (float) Math.sqrt(Math.pow(y2 - y, 2) + Math.pow(x2 - x, 2));
    }
}
