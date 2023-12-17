package agh.ics.oop.model;

import agh.ics.oop.model.enums.MapDirection;
import agh.ics.oop.model.enums.MoveDirection;
import agh.ics.oop.model.util.Vector2d;

import java.util.ArrayList;
import java.util.List;

public class Animal implements WorldElement {
    private MapDirection orientation;
    private Vector2d position;

    public Animal() {
        this(new Vector2d(2, 2));
    }

    public Animal(Vector2d start){
        this.orientation = MapDirection.NORTH;
        this.position = start;
    }

    public String toString() {
        return String.format("%s", orientation);
    }

    public boolean isAt(Vector2d atPos) {
        return position.equals(atPos);
    }

    public void move(MoveDirection direction) {

        List<Vector2d> moveVectors = new ArrayList<>(
                List.of(MapDirection.NORTH.toUnitVector(), MapDirection.EAST.toUnitVector(), MapDirection.SOUTH.toUnitVector(), MapDirection.WEST.toUnitVector())
        );

        switch(direction) {
            case RIGHT -> orientation = orientation.next();
            case LEFT -> orientation = orientation.previous();
            case FORWARD -> {
                this.position = position.add(moveVectors.get(orientation.ordinal()));
            }
            case BACKWARD -> {
                this.position = position.subtract(moveVectors.get(orientation.ordinal()));
            }
        }
    }

    public MapDirection getOrientation() {
        return orientation;
    }

    public Vector2d getPosition() {
        return position;
    }

}
