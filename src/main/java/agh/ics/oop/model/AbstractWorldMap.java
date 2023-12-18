package agh.ics.oop.model;

import agh.ics.oop.model.util.MapVisualizer;
import agh.ics.oop.model.util.PositionAlreadyOccupiedException;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

// observable type
abstract class AbstractWorldMap implements WorldMap<WorldElement, Vector2d> {

    static AtomicInteger uniqueId = new AtomicInteger();
    protected int id;
    protected List<MapChangeListener> observers = new ArrayList<>();
    protected Map<Vector2d, Animal> animalPositions = new HashMap<>();

    public AbstractWorldMap() {
        this.id = uniqueId.getAndIncrement();
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return !(objectAt(position) instanceof Animal);
    }

    @Override
    public void place(WorldElement obj) throws PositionAlreadyOccupiedException {
        if(!(obj instanceof Animal animal)) {
            return;
        }
        if(!canMoveTo(animal.getPosition())) {
            throw new PositionAlreadyOccupiedException(animal.getPosition());
        }

        animalPositions.put(animal.getPosition(), animal);
    };


    public String toString() {
        return new MapVisualizer(this).draw(getCurrentBounds().lowerLeft(), getCurrentBounds().upperRight());
    }

    @Override
    public void move(WorldElement toMove) throws PositionAlreadyOccupiedException {
        if(!(toMove instanceof Animal animal)) {return;}

        if(animal != animalPositions.get(animal.getPosition())) {return;}

        Vector2d startPos = animal.getPosition();

        try {
            animal.move(this);
            atMapChanged(String.format("Zwierze ruszylo sie z pozycji %s na pozycje %s", startPos, animal.getPosition()));
            animalPositions.remove(startPos);
            animalPositions.put(animal.getPosition(), animal);
        }
        catch (PositionAlreadyOccupiedException exc) {
            if(isOccupied(exc.getPosition())) {
                throw exc;
            }
        }

    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return animalPositions.get(position) != null;
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        return animalPositions.get(position);
    }

    @Override
    public List<WorldElement> getElements() {
        return new ArrayList<>(animalPositions.values());
    }

    @Override
    public abstract Boundary getCurrentBounds();

    public void addObserver(MapChangeListener observer) {
        observers.add(observer);
    }

    public void removeObserver(MapChangeListener observer) {
        observers.remove(observer);
    }

    public void atMapChanged(String str) {
        for(MapChangeListener observer : observers) {
            observer.mapChanged(this, str);
        }
    }
}
