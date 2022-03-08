package io.spherious.engine.collision;

import io.spherious.engine.math.Vector2D;
import io.spherious.engine.resources.GameObject;
import io.spherious.engine.resources.Player;


import java.awt.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;


public class Collision {

    /**
     * This function works in 3 stages:
     * First it gets the points inside the player
     * Next it loops over each of these points and checks if it is inside of the GameObject
     * if it is, then it adds it to vector b which keeps track of the sum of all points inside both
     * Next we normalize b.  b is the normal vector to the GameObject at the point of collision.
     * Next the player is teleported to the negative of the shortest vector between the player and the gameobject
     * The player's velocity is set to the reflection of the player's movement vector over b (the normal) times the
     * bounciness of the GameObject r
     *
     * @param r the object to collide with
     * @param p the player that's colliding
     * @return the player with changed position and velocity
     */
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

                b = b.multiply(new Vector2D(minDist, minDist).magnitude());


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
                //mult by bouncy


                Vector2D pra = reflect(playerVel, b);

                pra = pra.multiply(bouncy);

                //update movement

                p.setMovement(pra);

            }
            return true;
        }

        return false;

    }

    private static Vector2D reflect(Vector2D a, Vector2D b){
        double magA = a.magnitude();
        a.negate();
        a.normalize();
        b.normalize();

        double c = a.dot(b) * 2;
        Vector2D d = b.multiply(c);
        d.subtract(a);
        d.normalize();

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
