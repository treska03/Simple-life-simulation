package agh.ics.oop.model;

public class Grass implements WorldElement {

    public Vector2d getPosition() {
        return position;
    }
    private Vector2d position;

    public Grass(Vector2d position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "*";
    }


}
