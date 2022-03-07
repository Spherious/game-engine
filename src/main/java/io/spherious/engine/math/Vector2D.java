package io.spherious.engine.math;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Vector2D {
    private double x;
    private double y;

    public Vector2D(Vector2D other) {
        this.x = other.getX();
        this.y = other.getY();
    }

    public Vector2D() {
        new Vector2D(0, 0);
    }

    public Vector2D copy() {
        return new Vector2D(this);
    }

    public void add(double x, double y) {
        this.x += x;
        this.y += y;
    }

    public void add(Vector2D other) {
        this.add(other.getX(), other.getY());
    }

    public void subtract(double x, double y) {
        this.x -= x;
        this.y -= y;
    }

    public void normalize() {
        double oneOverMagnitude = this.invSqrt((float) (this.x * this.x + this.y * this.y));
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

    public static Vector2D subtract(Vector2D a, Vector2D b) {
        double ax = a.x;
        double ay = a.y;
        double bx = b.x;
        double by = b.y;
        return new Vector2D(ax - bx, ay - by);
    }

    public void subtract(Vector2D other) {
        this.subtract(other.getX(), other.getY());
    }

    public Vector2D multiply(double n) {
        return new Vector2D(this.x * n, this.y  * n);
    }

    public Vector2D multiply(Vector2D v){
        return new Vector2D(this.x * v.x,this.y * v.y);
    }

    public void divide(double n) {
        this.x /= n;
        this.y /= n;
    }

    public void negate() {
        this.x *= -1;
        this.y *= -1;
    }

    public void makeNeg() { //used to simulate a bounce
        this.x *= this.x > 0.0F ? -1.0F : 1.0F;
        this.y *= this.y > 0.0F ? -1.0F : 1.0F;
    }

    public void pow(double n) {
        this.x = Math.pow(this.x, n);
        this.y = Math.pow(this.y, n);
    }

    public void square() {
        this.pow(2);
    }

    public void sqrt() {
        this.x = Math.sqrt(this.x);
        this.y = Math.sqrt(this.y);
    }

    public double magnitude() {
        return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
    }

    public double dot(Vector2D other) {
        return (x * other.getX()) + (y * other.getY());
    }
}
