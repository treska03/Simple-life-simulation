package agh.ics.oop.model.map;

import agh.ics.oop.model.*;
import agh.ics.oop.model.creatures.Animal;
import agh.ics.oop.model.creatures.Plant;
import agh.ics.oop.model.creatures.WorldElement;
import agh.ics.oop.model.info.Constants;
import agh.ics.oop.model.info.ConstantsList;
import agh.ics.oop.model.util.*;
import agh.ics.oop.model.Vector2d;

import java.util.*;


public abstract class WorldMap {

    protected int simulationId;
    protected final Constants constants;
    protected List<MapChangeListener> observers = new ArrayList<>();
    protected Map<Vector2d, List<Animal>> animalPositions = new HashMap<>();
    protected final HashSet<Vector2d> plantPositions = new HashSet<>();
    protected List<Vector2d> noPlantsFieldsForJungle;
    protected List<Vector2d> noPlantsFieldsForSteps;


    public WorldMap(int simulationId) {
        this.simulationId = simulationId;
        this.constants = ConstantsList.getConstants(simulationId);
        this.noPlantsFieldsForJungle = PositionsGenerator.generateAllPositions(constants.getJungleBoundary());
        this.noPlantsFieldsForSteps = PositionsGenerator.generateStepsPositionList(
                constants.getMapBoundary(), constants.getJungleBoundary());
        initAnimals(constants.getStartingAnimalNumber());
    }

    public int getSimulationId() {
        return simulationId;
    }

    private void initAnimals(int animalsToAdd) {
        for(int i=0; i<animalsToAdd; i++) {
            addToAnimalMap(animalPositions, Animal.startingAnimal(simulationId));
        }
    }

    private void initPlants(int grassToAdd, List<Vector2d> noGrassFields) {
        /*
         add new Grass on randomly chosen unique positions to grassPositions
         and remove those positions from noGrassFields;
         positions are chosen using Fisher-Yates shuffle;
        */
        FisherYatesShuffle fisherYatesShuffle = new FisherYatesShuffle();
        List<Vector2d> newPositions = fisherYatesShuffle.getValues(grassToAdd, noGrassFields);
        for (Vector2d grassPosition : newPositions) {
            plantPositions.add(grassPosition);
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

    public String toString() {
        return new MapVisualizer(this).draw(constants.getMapBoundary().lowerLeft(), constants.getMapBoundary().upperRight());
    }

    public void removeDeadAnimals() {
        for(List<Animal> animalList: animalPositions.values()) {
            animalList.removeIf(animal -> animal.getCurrentEnergy() <= 0);
        }
    }

    public void moveAnimals() {
        Map<Vector2d, List<Animal>> newAnimalPositions = new HashMap<>();

        for(List<Animal> animalList: animalPositions.values()) {
            animalList.forEach(animal -> {
                moveSingleAnimal(animal);
                addToAnimalMap(newAnimalPositions, animal);
            });
        }

        animalPositions = newAnimalPositions;

    }

    abstract void moveSingleAnimal(Animal animal);

    public void feedAnimals() {
        for(Vector2d position : plantPositions) {
            List<Animal> animalsOnTile = animalPositions.get(position);
            if(!animalsOnTile.isEmpty()) {
                AnimalPrioritySorter.sortAnimals(animalsOnTile);

                animalsOnTile.get(0).consume();

                if(insideJungle(position)) noPlantsFieldsForJungle.add(position);
                else noPlantsFieldsForSteps.add(position);
            }
        }
        plantPositions.removeIf(pos -> animalPositions.get(pos) != null);
    }

    public void reproduceAnimals() {
        for(List<Animal> animalsOnTile : animalPositions.values()) {
            if(animalsOnTile.size() >= 2) {
                AnimalPrioritySorter.sortAnimals(animalsOnTile);
                Animal child = animalsOnTile.get(0).reproduce(animalsOnTile.get(1));
                if(child != null) animalsOnTile.add(child);
            }
        }
    }

    public void growPlants() {
        int plantsNumbers = constants.getDailyNewGrassNumber();
        int newJunglePlants = RandomNumberGenerator.getNumberOfNSuccessfulTrials(0.8, plantsNumbers);
        int newStepsPlants = plantsNumbers - newJunglePlants;

        if(newJunglePlants > noPlantsFieldsForJungle.size()) {
            newStepsPlants += (newJunglePlants- noPlantsFieldsForJungle.size());
            newJunglePlants = noPlantsFieldsForJungle.size();
        }

        if(newStepsPlants > noPlantsFieldsForSteps.size()) {
            newJunglePlants = Math.min(noPlantsFieldsForJungle.size(),
                    newJunglePlants + (newStepsPlants - noPlantsFieldsForSteps.size()));
            newStepsPlants = noPlantsFieldsForSteps.size();
        }

        initPlants(newJunglePlants, noPlantsFieldsForJungle);
        initPlants(newStepsPlants, noPlantsFieldsForSteps);
    }

    protected void addToAnimalMap(Map<Vector2d, List<Animal>> animalList, Animal animal) {
        List<Animal> destinationAnimalList = animalList.get(animal.getPosition());
        if(destinationAnimalList == null) {
            List<Animal> animalListNew = new ArrayList<>();
            animalListNew.add(animal);
            animalList.put(animal.getPosition(), animalListNew);
        }
        else {
            destinationAnimalList.add(animal);
        }
    }

    public WorldElement objectAt(Vector2d position) {
        if(!animalPositions.get(position).isEmpty()) {
            return animalPositions.get(position).get(0);
        }
        if(plantPositions.contains(position)){
            return new Plant(position);
        }
        return null;
    }

    private boolean insideJungle(Vector2d position) {
        return constants.getJungleBoundary().insideBoundary(position);
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

    public List<Vector2d> getNoPlantsFieldsForJungle() {
        return noPlantsFieldsForJungle;
    }

    public List<Vector2d> getNoPlantsFieldsForSteps() {
        return noPlantsFieldsForSteps;
    }
}
