package agh.ics.oop.model.util;

import agh.ics.oop.model.Vector2d;

public class PositionAlreadyOccupiedException extends Exception{
    public Vector2d getPosition() {
        return position;
    }

    private Vector2d position;
    public PositionAlreadyOccupiedException(Vector2d position) {
        super(String.format("Position (%d, %d) is already occupied)", position.getX(), position.getY()));
        this.position = position;
    }

    public PositionAlreadyOccupiedException() {
        super("Position already occupied");
    }
}
