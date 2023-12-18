package agh.ics.oop.model.map;

import agh.ics.oop.model.*;
import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.MapVisualizer;
import agh.ics.oop.model.util.RandomPositionGenerator;
import agh.ics.oop.model.util.Vector2d;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

abstract class AbstractWorldMap implements WorldMap {

    static AtomicInteger uniqueId = new AtomicInteger();
    protected int id;


    protected Boundary bounds;
    protected List<MapChangeListener> observers = new ArrayList<>();
    protected Map<Vector2d, Animal> animalPositions = new HashMap<>();
    private final Map<Vector2d, Grass> grassPositions = new HashMap<>();


    public Boundary getBounds() {
        return bounds;
    }

    public AbstractWorldMap() {
        this.id = uniqueId.getAndIncrement();
        this.bounds = new Boundary(new Vector2d(0, 0), new Vector2d(10, 10));
        initGrass();
    }

    public int getId() {
        return id;
    }

    private void initGrass() {
        RandomPositionGenerator randomPositionGenerator = new RandomPositionGenerator(
                bounds.lowerLeft(), bounds.upperRight(), 25
        );
        for (Vector2d grassPosition : randomPositionGenerator) {
            place(new Grass(grassPosition));
        }
    }

    @Override
    public void place(WorldElement obj) {
        if(!(obj instanceof Animal)) {
            grassPositions.put(obj.getPosition(),(Grass) obj);
        }
        else{
            animalPositions.put(obj.getPosition(),(Animal) obj);
        }
    }

    public String toString() {
        return new MapVisualizer(this).draw(bounds.lowerLeft(), bounds.upperRight());
    }

    @Override
    public void move(Animal animal){

        if(animal != animalPositions.get(animal.getPosition())) {return;}

        Vector2d startPos = animal.getPosition();

        animal.move();
        atMapChanged(String.format("Zwierze ruszylo sie z pozycji %s na pozycje %s", startPos, animal.getPosition()));
        animalPositions.remove(startPos);
        animalPositions.put(animal.getPosition(), animal);
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        return animalPositions.get(position) != null ? animalPositions.get(position) : grassPositions.get(position);
    }

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
