package io.spherious.engine.resources;

import io.spherious.engine.math.Vector2D;

import java.awt.*;
import java.util.ArrayList;

public class Player {
    private int size;
    private double locx;
    private double locy;
    private int defaultSize;

    private Vector2D movement;
    private ArrayList<Point> savedPoints = null;

    /**
     *
     * @param x starting movement x
     * @param y starting movement y
     * @param size size (radius)
     * @param locx starting location x
     * @param locy starting location y
     * @param defaultSize default size for respawn
     */
    public Player(float x, float y, int size, int locx, int locy, int defaultSize) {
        this.movement = new Vector2D(x, y);
        this.size = size;
        this.defaultSize = defaultSize;

        this.locx = locx;
        this.locy = locy;
    }

    public int getSize() {
        return this.size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public double getLocx() {
        return this.locx;
    }

    public void setLocx(int locx) {
        this.locx = locx;
    }

    public double getLocy() {
        return this.locy;
    }

    public void setLocy(int locy) {
        this.locy = locy;
    }

    public int getDefaultSize() {
        return this.defaultSize;
    }

    public Vector2D getMovement() {
        return this.movement;
    }

    public void setMovement(Vector2D v) {
        this.movement = v;
    }

    public void resetSavedPoints() {
        this.savedPoints = null;
    }
    //save the points in a circle of the player size around 0,0 to optimize

    /**
     * On the first run, it generates a circle of points around 0,0 with a radius of the player's size
     * Next it adds the location of the player to each of these points to get all points inside the player
     * at the current location
     *
     * On all subsequent runs, it uses the saved points around 0,0 and just shifts them - which increases speed
     *
     * @return all integer points inside the border or the player
     */
    public ArrayList<Point> getPointsInside() {
        ArrayList<Point> points = new ArrayList<>();
        if (this.savedPoints == null) {
            ArrayList<Point> pointsNonShift = new ArrayList<>();

            for (int i = -this.size; i < this.size; i++) {
                for (int j = -this.size; j < this.size; j++) {
                    if (i * i + j * j < (this.size / 2) * (this.size / 2)) {
                        Point p = new Point((int) (i + this.locx), (int) (j + this.locy));
                        Point p2 = new Point(i, j);
                        points.add(p);
                        pointsNonShift.add(p2);
                    }
                }
            }
            this.savedPoints = pointsNonShift;

        } else {
            for (Point p : this.savedPoints) {
                Point z = (Point) p.clone();
                z.setLocation(p.x + this.locx, p.y + this.locy);
                points.add(z);
            }
        }
        return points;

    }
}