package agh.ics.oop.model;

import java.util.List;

public class RectangularMap extends AbstractWorldMap {

    // Attribute of Map is vector and value of it is Animal object present on the square
    // Import z abstract
    final int width;
    final int height;
    final Vector2d lowerLeft;
    final Vector2d upperRight;

    public RectangularMap(int width, int height) {
        super();
        if(width <=0 || height <= 0) {throw new IllegalArgumentException();}
        this.width = width;
        this.height = height;
        this.lowerLeft = new Vector2d(0, 0);
        this.upperRight = new Vector2d(width, height);
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
