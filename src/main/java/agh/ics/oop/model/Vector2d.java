package agh.ics.oop.model;

import java.util.Objects;

public class Vector2d {
    private final int x;
    private final int y;

    public Vector2d(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String toString() {
        return String.format("(%d,%d)", getX(), getY());
    }

    private boolean x_precedes(Vector2d vector1, Vector2d vector2) {
        return (vector1.getX()<= vector2.getX());
    }
    private boolean y_precedes(Vector2d vector1, Vector2d vector2) {
        return (vector1.getY()<=vector2.getY());
    }

    public boolean precedes(Vector2d other) {
        return (x_precedes(this, other) && y_precedes(this, other));
    }
    public boolean follows(Vector2d other) {
        return (x_precedes(other, this) && y_precedes(other, this));
    }

    public Vector2d add(Vector2d other) {
        return new Vector2d(this.getX() + other.getX(), this.getY() + other.getY());
    }

    public Vector2d subtract(Vector2d other) {
        return new Vector2d(this.getX() - other.getX(), this.getY() - other.getY());
    }

    public Vector2d upperRight(Vector2d other) {
        return new Vector2d(
                Math.max(this.getX(), other.getX()),
                Math.max(this.getY(), other.getY())
        );
    }

    public Vector2d lowerLeft(Vector2d other) {
        return new Vector2d(
                Math.min(this.getX(), other.getX()),
                Math.min(this.getY(), other.getY())
        );
    }

    public Vector2d opposite() {
        return new Vector2d(-this.getX(), -this.getY());
    }

    public boolean equals(Object other) {
        if(this == other) {
            return true;
        }
        if(!(other instanceof Vector2d)) {
            return false;
        }
        return (this.getX() == ((Vector2d) other).getX() && this.getY() == ((Vector2d) other).getY());
    }

    public int hashCode() {
        return Objects.hash(this.getX(), this.getY());
    }

}
