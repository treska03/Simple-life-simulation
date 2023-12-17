package agh.ics.oop.model;

public interface MapChangeListener {
    void mapChanged(WorldMap<WorldElement, Vector2d> worldMap, String message);

}
