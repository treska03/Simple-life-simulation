package agh.ics.oop.model.map;


import agh.ics.oop.model.*;
import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.Vector2d;

/**
 * The interface responsible for interacting with the map of the world.
 * Assumes that Vector2d and MoveDirection classes are defined.
 *
 * @author apohllo, idzik
 */
public interface WorldMap{

    /**
     *
     * @return id of the map
     */
    int getId();

    /**
     * Place a WorldElement type object on the map.
     *
     * @param obj The object to place on the map.
     * @return True if the object was placed. The object cannot be placed if the move is not valid.
     */
    void place(WorldElement obj);

    /**
     * Moves an Animal (if it is present on the map).
     */
    void move(Animal toMove);

    /**
     * Return a WorldElement type object at a given position.
     *
     * @param position of the T type object.
     * @return WorldElement object or null if the position is not occupied.
     */
    WorldElement objectAt(Vector2d position);

    /**
     *
     * @return bounds of map
     */
    Boundary getBounds();


    public void addObserver(MapChangeListener observer);

}