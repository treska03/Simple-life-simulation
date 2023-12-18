package agh.ics.oop.model;

import java.util.List;

public class RectangularMap extends AbstractWorldMap {

    // Attribute of Map is vector and value of it is Animal object present on the square
    // Import from abstract

    final Vector2d lowerLeft = Constances.LOWER_LEFT;
    final Vector2d upperRight = Constances.UPPER_RIGHT;

    public RectangularMap() {
        super();
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return super.canMoveTo(position) && getCurrentBounds().insideBoundary(position);
    }

    @Override
    public Boundary getCurrentBounds() {
        return new Boundary(lowerLeft, upperRight);
    }

}
