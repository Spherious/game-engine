package io.spherious.engine.resources;

public class Vector {
    private float x;
    private float y;

    public Vector(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public static Vector subtract(Vector a, Vector b) {
        float ax = a.x, ay = a.y, bx = b.x, by = b.y;
        return new Vector(ax - bx, ay - by);
    }

    public float getX() {
        return this.x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return this.y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void add(Vector v) {
        this.x += v.getX();
        this.y += v.getY();
    }

    public void multElements(Vector v) {
        this.x *= v.getX();
        this.y *= v.getY();
    }

    public void negate() {
        this.x *= -1;
        this.y *= -1;
    }

    public void makeNeg() { //make all terms exclusively negative
        this.x *= this.x > 0 ? -1 : 1;
        this.y *= this.y > 0 ? -1 : 1;
    }

    public void normalize() {
        float oneOverMagnitude = this.invSqrt(this.x * this.x + this.y * this.y);
        this.x *= oneOverMagnitude;
        this.y *= oneOverMagnitude;
    }

    private float invSqrt(float x) { //more accurate than the traditional invsqrt because it uses newtons method more than once
        float xhalf = 0.5f * x;
        int i = Float.floatToIntBits(x);
        i = 0x5f3759df - (i >> 1);
        x = Float.intBitsToFloat(i);

        for (int j = 0; j < 10; j++) {
            x *= (1.5f - xhalf * x * x);
        }

        return x;
    }

    public float magnitude() {
        return (float) Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
    }

    public String toString() {
        return "(" + this.x + "," + this.y + ")";
    }


}
