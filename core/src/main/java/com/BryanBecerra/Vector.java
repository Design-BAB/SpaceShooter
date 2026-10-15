package com.BryanBecerra;

/** A mutable two-dimensional vector whose angles are measured in degrees. */
public final class Vector {
    private double x;
    private double y;

    public Vector() {
    }

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static Vector fromAngle(double angleDegrees, double length) {
        double radians = Math.toRadians(angleDegrees);
        return new Vector(Math.cos(radians) * length, Math.sin(radians) * length);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getLength() {
        return Math.hypot(x, y);
    }

    public double getAngle() {
        return Math.toDegrees(Math.atan2(y, x));
    }

    public Vector set(double x, double y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vector add(Vector other) {
        x += other.x;
        y += other.y;
        return this;
    }

    public Vector scale(double factor) {
        x *= factor;
        y *= factor;
        return this;
    }

    public Vector rotate(double angleDegrees) {
        double radians = Math.toRadians(angleDegrees);
        double rotatedX = x * Math.cos(radians) - y * Math.sin(radians);
        y = x * Math.sin(radians) + y * Math.cos(radians);
        x = rotatedX;
        return this;
    }

    public Vector copy() {
        return new Vector(x, y);
    }
}
