package agh.ics.oop.model.map;

import agh.ics.oop.model.*;
import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.FisherYatesShuffle;
import agh.ics.oop.model.util.MapVisualizer;
import agh.ics.oop.model.util.RandomPositionGenerator;
import agh.ics.oop.model.Vector2d;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Math.min;

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

    // proponuje usunąć initGrass i zastąpić ją initGrass2
    // Przyjmijmy:
    // t - ilość dni w grze
    // r - liczb nowo generowanych roślin w jednym tiku gry
    // n - łączna liczba pól w grze
    // stosując initGrass mamy złożoność O(n*t)
    // stosując initGrass2 oraz generateAllPositions mamy złożoność O(n + r*t)
    // initGrass2 byłby stosowany każdego dnia, a
    // generateAllPositions tylko 1 raz - przy tworzeniu mapy
    // ten i powyższe komentarze do usunięcia
    private void initGrass() {
        RandomPositionGenerator randomPositionGenerator = new RandomPositionGenerator(
                bounds.lowerLeft(), bounds.upperRight(), 25
        );
        for (Vector2d grassPosition : randomPositionGenerator) {
            place(new Grass(grassPosition));
        }
    }

    private void initGrass2(int grassToAdd, List<Vector2d> barren) {
        // add new Grass on randomly chosen unique positions to grassPositions
        // and remove those positions from barren;
        // barren is the list that stores fields that have no grass;
        // positions are chosen using Fisher-Yates shuffle;
        FisherYatesShuffle fisherYatesShuffle = new FisherYatesShuffle();
        List<Vector2d> newPositions = fisherYatesShuffle.getValues(grassToAdd, barren);
        for (Vector2d grassPosition : newPositions) {
            grassPositions.put(grassPosition, new Grass(grassPosition));
        }
        for (int i = 0; i < grassToAdd; i++) {
            // Fisher-Yates shuffle place chosen positions at the end of the list
            barren.remove(barren.remove(barren.size() - 1));
        }
    }

    // metoda skopiowana z RandomPositionGenerator tylko, że bez permutowania
    // ten i powyższy komentarz do usunięcia
    private List<Vector2d> generateAllPositions(int startX, int startY, int finishX, int finishY) {
        // store all positions in the list;
        // it will only be used when initializing the map
        List<Vector2d> allPositions = new ArrayList<>();
        for(int x=startX; x<=finishX; x++) {
            for(int y=startY; y<=finishY; y++) {
                allPositions.add(new Vector2d(x, y));
            }
        }
        return allPositions;
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
