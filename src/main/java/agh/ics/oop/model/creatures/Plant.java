package agh.ics.oop.model.creatures;

import agh.ics.oop.model.util.Vector2d;

public class Plant implements WorldElement {

    public Vector2d getPosition() {
        return position;
    }
    private Vector2d position;

    public Plant(Vector2d position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "*";
    }


}
