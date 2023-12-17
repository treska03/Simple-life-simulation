package agh.ics.oop.model;


import agh.ics.oop.model.util.PositionAlreadyOccupiedException;

import java.util.List;
import java.util.UUID;

/**
 * The interface responsible for interacting with the map of the world.
 * Assumes that Vector2d and MoveDirection classes are defined.
 *
 * @author apohllo, idzik
 */
public interface WorldMap<T, P> extends MoveValidator<P> {

    /**
     *
     * @return id of the map
     */
    int getId();

    /**
     * Place a T type object on the map.
     *
     * @param obj The object to place on the map.
     * @return True if the object was placed. The object cannot be placed if the move is not valid.
     */
    void place(T obj) throws PositionAlreadyOccupiedException;

    /**
     * Moves a T object (if it is present on the map) according to specified direction.
     * If the move is not possible, this method has no effect.
     */
    void move(T toMove , MoveDirection direction) throws PositionAlreadyOccupiedException;

    /**
     * Return true if given position on the map is occupied. Should not be
     * confused with canMove since there might be empty positions where the
     * T type object cannot move.
     *
     * @param position Position to check.
     * @return True if the position is occupied.
     */
    boolean isOccupied(P position);

    /**
     * Return a T type object at a given position.
     *
     * @param position The position of the T type object.
     * @return T object or null if the position is not occupied.
     */
    T objectAt(P position);

    /**
     * Return List<Type> of Type elements
     *
     * @return List<Type> containing all elements of map.
     * */
    List<T> getElements();

    /**
     * Return Boundary record holding lowerLeft and upperRight attributes
     * Used to determine boundary of the map
     *
     * @return Boundary record object.
     */
    Boundary getCurrentBounds();
    public void addObserver(MapChangeListener observer);

}