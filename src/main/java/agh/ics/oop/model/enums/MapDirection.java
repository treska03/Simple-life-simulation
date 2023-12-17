package agh.ics.oop.model.enums;

import agh.ics.oop.model.util.Vector2d;

public enum MapDirection {
    NORTH,
    EAST,
    SOUTH,
    WEST;

    public String toString() {
        return switch(this) {
            case NORTH -> "N";
            case EAST -> "E";
            case SOUTH -> "S";
            case WEST -> "W";
        };
    }
    public MapDirection next() {
        return switch(this) {
            case NORTH -> EAST;
            case EAST -> SOUTH;
            case SOUTH -> WEST;
            case WEST -> NORTH;
        };
    }

    public MapDirection previous() {
        return switch(this) {
            case NORTH -> WEST;
            case WEST -> SOUTH;
            case SOUTH -> EAST;
            case EAST -> NORTH;
        };
    }

    public Vector2d toUnitVector() {
        return switch(this) {
            case NORTH -> new Vector2d(0, 1);
            case EAST -> new Vector2d(1, 0);
            case SOUTH -> new Vector2d(0, -1);
            case WEST -> new Vector2d(-1, 0);
        };
    }
}
