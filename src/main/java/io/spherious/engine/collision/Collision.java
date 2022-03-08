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
     * Hello, you're reading this - so welcome to hell
     * This alg makes sense when explained on paper, but in code looks like I mashed my keyboard
     *
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

        //all points inside the player's circle
        ArrayList<Point> pointsInsidePlayer = p.getPointsInside();

        //playerVel is the player's movement vector
        //b will soon by the vector normal to the object at the point of collision
        //bouncy is the bounciness of the object
        //centerOfPlayer is the center of the player's circle
        Vector2D playerVel = p.getMovement(),
                b = new Vector2D(0, 0),
                bouncy = new Vector2D(r.getBouncyness(), r.getBouncyness()),
                centerOfPlayer = new Vector2D((float) p.getLocx(), (float) p.getLocy());

        //---
        //This section determines two things:
            //A: is the player currently hitting anything, and
            //B: if so, how is it colliding

        //if this is 0 after we run the alg we know that there is no overlap between the player
        //and the object, thus they can't be colliding
        int amountOfPointsInPlayerAndObject = 0;

        //just all the points inside both the player and the object being collided with
        ArrayList<Point> pointsInPlayerAndObject = new ArrayList<>();
        for (Point point : pointsInsidePlayer) {
            if (r.insideObject(point)) { //point is inside both the player and the object in question

                //vector from the point being checked (which is inside the overlap region) to the center of the player
                Vector2D pointCenter = Vector2D.subtract(centerOfPlayer, new Vector2D(point.x, point.y));

                //add this vector to b, once all vectors that go from a point in the overlapping region to the center of the
                //player are added to b, and b is normalized - it will be normal to the surface at the point of collision
                //this is because we are taking an average "direction" of all the vectors
                b.add(pointCenter);

                //nothing special here, just update these for later
                amountOfPointsInPlayerAndObject++;
                pointsInPlayerAndObject.add(point);
            }
        }
        //---

        if (amountOfPointsInPlayerAndObject != 0) {
            if (r.isCollidable()) {

                //this finds the point in the overlapping region that is closest to the center of the player
                //When colliding with straight edges this point should be on the normal vector (b).
                //We use this point to teleport the player that far in the direction of b so that the player
                //is totally outside the shape after one frame of collision, so there isn't any weirdness with
                //the player movement vector being added to over and over by the collision stuff.  If that happens
                //sometimes the player will be launched at mach 10 off the screen which is funny but also bad
                Optional<Point> points = pointsInPlayerAndObject.stream().min((o1, o2) -> {
                    float d1 = distanceSquared(o1.x, o1.y, p.getLocx(), p.getLocy());
                    float d2 = distanceSquared(o2.x, o2.y, p.getLocx(), p.getLocy());
                    return Float.compare(d1, d2);
                });

                //so this will never be a "new Point" java just is dumb and thinks its possible
                Point closest = points.orElseGet(Point::new);


                float minDist = distance(closest.x,closest.y,p.getLocx(),p.getLocy());

                minDist = (p.getSize() / 2f - minDist) * 2;

                b.normalize();

                b = b.multiply(new Vector2D(minDist, minDist).magnitude());


                Vector2D playerLoc = new Vector2D((float) p.getLocx(), (float) p.getLocy());
                playerLoc.add(b);

                //teleport the player outside the colliding shape, using the mid distance and the direction 'b'
                p.setLocx((int) (playerLoc.getX()));
                p.setLocy((int) (playerLoc.getY()));

                //player velocity
                //calc new direction

                //alternative
                //make b neg
                //mult playervel by b
                //go from there
                //mult by bouncy

                //reflect the player's movement so it bounces properly
                Vector2D pra = reflect(playerVel, b);

                //boop
                pra = pra.multiply(bouncy);

                //update movement
                p.setMovement(pra);

            }
            return true;
        }

        return false;
        //thanks for reading about my awful collision system
    }

    /**
     * Algorithm from https://www.fabrizioduroni.it/2017/08/25/how-to-calculate-reflection-vector/
     * You need to negate the vector at the start because the alg on the website is designed for light
     * which is moving in the other direction
     *
     * @param a Movement Vector
     * @param b Normal Vector to colliding surface at the point of collision
     * @return
     */
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

    /**
     *
     * @param p Player that is being checked for collision
     * @param dimx x dimension of the window
     * @param dimy y dimension of the window
     * @param compression compression ratio of the game window
     */
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

    /**
     * Gets the distance between two points squared.  Used to check if a distance is smaller
     * than another distance without actually squarerooting anything because its slower
     * note that if a < b then sqrt(a) < sqrt(b)
     * @param x
     * @param y
     * @param x2
     * @param y2
     * @return
     */
    private static float distanceSquared(double x, double y, double x2, double y2) {
        return (float) ((float) Math.pow(y2 - y, 2) + Math.pow(x2 - x, 2));
    }

    private static float distance(double x, double y, double x2, double y2) {
        return (float) Math.sqrt(Math.pow(y2 - y, 2) + Math.pow(x2 - x, 2));
    }
}
