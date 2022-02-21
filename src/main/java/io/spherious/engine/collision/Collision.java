package io.spherious.engine.collision;

import io.spherious.engine.resources.GameObject;
import io.spherious.engine.resources.Player;
import io.spherious.engine.resources.Vector;

import java.awt.*;
import java.util.ArrayList;


public class Collision {
    private boolean collideWithObject(GameObject r, Player p) {
        if (!r.compute()) //for backgrounds and sprites
            return false;

        ArrayList<Point> pointsInsidePlayer = p.getPointsInside();

        Vector playerVel = p.getMovement(),
                b = new Vector(0, 0),
                bouncy = new Vector(r.getBouncyness(), r.getBouncyness()),
                centerOfPlayer = new Vector((float) p.getLocx(), (float) p.getLocy());

        int amountOfPointsInPlayerAndObject = 0;
        ArrayList<Point> pointsInPlayerAndObject = new ArrayList<>();
        for (Point point : pointsInsidePlayer) {
            if (r.insideObject(point)) { //point is inside both the player and the object in question
                Vector pointCenter = Vector.subtract(centerOfPlayer, new Vector(point.x, point.y));
                b.add(pointCenter);
                amountOfPointsInPlayerAndObject++;
                pointsInPlayerAndObject.add(point);
            }
        }

        if (amountOfPointsInPlayerAndObject != 0) {
            if (r.isCollidable()) {

                //player location

                float minDist = Integer.MAX_VALUE;

                for (Point point : pointsInPlayerAndObject) {
                    float d = distanceSquared(point.x, point.y, p.getLocx(), p.getLocy());
                    if (d < minDist) {
                        minDist = d;
                    }
                }
                minDist = (float) Math.sqrt(minDist);
                minDist = (p.getSize() / 2f - minDist) * 2;

                b.normalize();

                b.multElements(new Vector(minDist, minDist));


                Vector playerLoc = new Vector((float) p.getLocx(), (float) p.getLocy());
                playerLoc.add(b);

                p.setLocx((int) (playerLoc.getX()));
                p.setLocy((int) (playerLoc.getY()));
                b.normalize();
                //player velocity
                b.makeNeg();

                playerVel.multElements(b);
                playerVel.multElements(bouncy);


                p.setMovement(playerVel);


            }
            return true;
        }

        return false;

    }

    private float distanceSquared(double x, double y, double x2, double y2) {
        return (float) ((float) Math.pow(y2 - y, 2) + Math.pow(x2 - x, 2));
    }
}