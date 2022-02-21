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

    public void subtract(Vector2D other) {
        this.subtract(other.getX(), other.getY());
    }

    public void multiply(double n) {
        this.x *= n;
        this.y  *= n;
    }

    public void divide(double n) {
        this.x /= n;
        this.y /= n;
    }

    public void negate() {
        this.x *= -1;
        this.y *= -1;
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
        return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.x, 2));
    }

    public

    public double dot(Vector2D other) {
        return (x * other.getX()) + (y * other.getY());
    }
}
