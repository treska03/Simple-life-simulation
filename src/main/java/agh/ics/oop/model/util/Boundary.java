package agh.ics.oop.model.util;

import java.util.List;

public record Boundary(Vector2d lowerLeft, Vector2d upperRight) {

    public static Boundary fromList(List<Vector2d> list) {
        if(list.isEmpty()) {
            Vector2d lowerLeft = new Vector2d(0, 0);
            Vector2d upperRight = new Vector2d(0, 0);
            return new Boundary(lowerLeft, upperRight);
        }
        Vector2d lowerLeftPosition = list.get(0);
        for(Vector2d vector : list) {
            lowerLeftPosition = lowerLeftPosition.lowerLeft(vector);
        }
        Vector2d upperRightPosition = list.get(0);
        for(Vector2d vector : list) {
            upperRightPosition = upperRightPosition.upperRight(vector);
        }
        return new Boundary(lowerLeftPosition, upperRightPosition);
    }

    public int numberOfFieldsInsideBoundary() {
        int width = upperRight().getX() - lowerLeft().getX() + 1;
        int height = upperRight().getY() - lowerLeft().getY() + 1;
        return height * width;
    }

    public boolean insideBoundary(Vector2d toTest) {
        return toTest.follows(lowerLeft) && toTest.precedes(upperRight);
    }

}
