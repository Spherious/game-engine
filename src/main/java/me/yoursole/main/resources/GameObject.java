package me.yoursole.main.resources;

import java.awt.*;
import java.io.File;

public abstract class GameObject {
    private float bouncyness;
    private boolean isWinner;
    private boolean isVisible;
    private int sizeSet;
    private boolean kills;
    private boolean isCollidable;
    private boolean compute;

    private Runnable r;
    private File texture;

    public GameObject(float bouncyness, boolean isWinner, boolean isVisible, int sizeSet, boolean kills, boolean isCollidable, Runnable custom, String texturePath, boolean compute) {
        this.bouncyness = bouncyness;
        this.isVisible = isVisible;
        this.sizeSet = sizeSet;
        this.isWinner = isWinner;
        this.kills = kills;
        this.isCollidable = isCollidable;

        this.r = custom;
        this.compute = compute;

        if (!texturePath.equals(""))
            this.texture = new File(texturePath);
        else
            this.texture = null;
    }

    public File getTexture() {
        return this.texture;
    }


    public float getBouncyness() {
        return this.bouncyness;
    }

    public boolean isWinner() {
        return this.isWinner;
    }

    public boolean isVisible() {
        return this.isVisible;
    }

    public int getSizeSet() {
        return this.sizeSet;
    }

    public boolean isKills() {
        return kills;
    }

    public boolean isCollidable() {
        return isCollidable;
    }

    public void runFunc() {
        r.run();
    }

    public boolean compute() {
        return this.compute;
    }

    public abstract boolean insideObject(Point p); //used for collisions

    public abstract Graphics2D paint(Graphics2D g2);

}
