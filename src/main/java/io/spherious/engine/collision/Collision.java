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

                //player velocity
                //calc new direction

                //alternative
                //make b neg
                //mult playervel by b
                //go from there

                Vector2D pra = reflect(playerVel, b);
                System.out.println(playerVel+"--"+b+"--"+pra);

                pra = pra.multiply(bouncy);

                p.setMovement(pra);

            }
            return true;
        }

        return false;

    }

    private static Vector2D reflect(Vector2D a, Vector2D b){
        double magA = a.magnitude();
        a.negate();

        double c = a.dot(b) * 2;
        Vector2D d = b.multiply(c);
        d.subtract(a);
        d.normalize();
        d.negate();

        return d.multiply(magA);

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
