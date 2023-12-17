package agh.ics.oop.model;

import agh.ics.oop.model.util.Vector2d;

public interface WorldElement {

    /**
     * Getter for position of given element
     *
     * @return Vector2d position of obj
     */
    Vector2d getPosition();

    /**
     * Returns string representation of WorldElement object
     *
     * @return String representation
     */
    @Override
    String toString();

}
