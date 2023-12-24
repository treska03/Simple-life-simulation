package agh.ics.oop.model.map;

import agh.ics.oop.model.*;
import agh.ics.oop.model.info.Constants;
import agh.ics.oop.model.info.ConstantsList;
import agh.ics.oop.model.util.FisherYatesShuffle;
import agh.ics.oop.model.util.MapVisualizer;
import agh.ics.oop.model.Vector2d;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


public class WorldMap {

    static AtomicInteger uniqueId = new AtomicInteger();
    protected int id;
    protected final Constants constants;
    protected List<MapChangeListener> observers = new ArrayList<>();
    protected Map<Vector2d, Animal> animalPositions = new HashMap<>();
    protected final Map<Vector2d, Grass> grassPositions = new HashMap<>();
    protected List<Vector2d> noGrassFieldsForJungle = new ArrayList<>();
    protected List<Vector2d> noGrassFieldsForSteps = new ArrayList<>();


    public WorldMap() {
        this.id = uniqueId.getAndIncrement();
        this.constants = ConstantsList.getConstants(id);
        /*
         W 2 poniższyc linijakch kodu musiałem ustawić jakieś wartości, by błedu nie wywalało,
         to są te wszytskie, gdzie jest 0 wpisane;
         To jakie one faktycznie mają być, zostawiam już Tobie;
         Dałem listę to argumentów przy initGrass,
         bo w przeciwnym razie, by sie strasznie kod dublował;
         Ten cały blok komentarzy do usunięcia
        */
        initGrass(0, noGrassFieldsForJungle);
        initGrass(0, noGrassFieldsForSteps);
    }

    public int getId() {
        return id;
    }

    private void initGrass(int grassToAdd, List<Vector2d> noGrassFields) {
        /*
         add new Grass on randomly chosen unique positions to grassPositions
         and remove those positions from noGrassFields;
         positions are chosen using Fisher-Yates shuffle;
        */
        FisherYatesShuffle fisherYatesShuffle = new FisherYatesShuffle();
        List<Vector2d> newPositions = fisherYatesShuffle.getValues(grassToAdd, noGrassFields);
        for (Vector2d grassPosition : newPositions) {
            grassPositions.put(grassPosition, new Grass(grassPosition));
        }
        for (int i = 0; i < grassToAdd; i++) {
            /*
             Fisher-Yates shuffle place chosen positions at the end of the list;
             And because we pass the list noGrassFields by reference,
             Fisher-Yates shuffle changed that list everywhere;
             That's why we know that the chosen elements
             are at the end of the list noGrassFields;
             It's used because it reduces the time complexity
            */
            noGrassFields.remove(noGrassFields.remove(noGrassFields.size() - 1));
        }
    }

    public void place(WorldElement obj) {
        if(!(obj instanceof Animal)) {
            grassPositions.put(obj.getPosition(),(Grass) obj);
        }
        else{
            animalPositions.put(obj.getPosition(),(Animal) obj);
        }
    }

    public String toString() {
        return new MapVisualizer(this).draw(constants.getMapBoundary().lowerLeft(), constants.getMapBoundary().upperRight());
    }

    public void move(Animal animal){

        if(animal != animalPositions.get(animal.getPosition())) {return;}

        Vector2d startPos = animal.getPosition();

        animal.move();
        atMapChanged(String.format("Zwierze ruszylo sie z pozycji %s na pozycje %s", startPos, animal.getPosition()));
        animalPositions.remove(startPos);
        animalPositions.put(animal.getPosition(), animal);
    }

    public void feedAnimal(Animal animal) {
        Vector2d position = animal.getPosition();
        if(grassPositions.get(position) == null) {return;}
        animal.consume();

        grassPositions.remove(position);
        if(constants.getJungleBoundary().insideBoundary(position)) {
            noGrassFieldsForJungle.add(position);
        }
        else {
            noGrassFieldsForSteps.add(position);
        }
    }

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

    public List<Vector2d> getNoGrassFieldsForJungle() {
        return noGrassFieldsForJungle;
    }

    public List<Vector2d> getNoGrassFieldsForSteps() {
        return noGrassFieldsForSteps;
    }
}
