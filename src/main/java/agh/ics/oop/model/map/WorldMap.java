package agh.ics.oop.model.map;

import agh.ics.oop.model.*;
import agh.ics.oop.model.algorithms.FisherYatesShuffle;
import agh.ics.oop.model.creatures.Animal;
import agh.ics.oop.model.creatures.Plant;
import agh.ics.oop.model.creatures.WorldElement;
import agh.ics.oop.model.info.Constants;
import agh.ics.oop.model.info.ConstantsList;
import agh.ics.oop.model.util.*;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.info.Stats;
import agh.ics.oop.model.info.StatsList;
import java.util.*;


public abstract class WorldMap {

    protected int simulationId;
    protected final Constants constants;
    protected final Stats stats;
    protected final List<ChangeListener> observers = new ArrayList<>();
    protected Map<Vector2d, List<Animal>> animalPositions = new HashMap<>();
    private final HashSet<Vector2d> plantPositions = new HashSet<>();
    private final List<Vector2d> noPlantsFieldsForJungle;
    private final List<Vector2d> noPlantsFieldsForSteps;


    public WorldMap(int simulationId) {
        this.simulationId = simulationId;
        this.constants = ConstantsList.getConstants(simulationId);
        if (constants == null) {
            throw new IllegalArgumentException(simulationId + " is a wrong id.");
        }
        this.stats = StatsList.getStats(simulationId);
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
            Animal animal = Animal.startingAnimal(simulationId);
            addToAnimalMap(animalPositions, animal);
            stats.reportAddingStartingAnimal(animal);
        }
    }

    private void initPlants(int grassToAdd, List<Vector2d> noGrassFields) {
        /*
         add new Grass on randomly chosen unique positions to grassPositions
         and remove those positions from noGrassFields;
         positions are chosen using Fisher-Yates shuffle;
        */
        FisherYatesShuffle<Vector2d> fisherYatesShuffle = new FisherYatesShuffle<>();
        List<Vector2d> newPositions = fisherYatesShuffle.getValues(grassToAdd, noGrassFields);
        plantPositions.addAll(newPositions);
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

    public void reduceAnimalEnergy() {
        for(List<Animal> animalList: animalPositions.values()) {
            for (Animal animal : animalList) {
                animal.removeEnergy(constants.getDailyEnergyLoss());
            }
        }
    }

    public void removeDeadAnimals() {
        for(List<Animal> animalList: animalPositions.values()) {
            Iterator<Animal> iterator = animalList.iterator();
            while (iterator.hasNext()) {
                Animal animal = iterator.next();
                if (animal.getCurrentEnergy() <= 0){
                    iterator.remove();
                    stats.reportDeathOfAnimal(animal);
                }
            }
        }
        Iterator<Map.Entry<Vector2d, List<Animal>>> iterator = animalPositions.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Vector2d, List<Animal>> entry = iterator.next();
            List<Animal> animalsOnTile = entry.getValue();

            if (animalsOnTile.isEmpty()) {
                iterator.remove();
            }
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
            if(animalsOnTile != null) {
                MaximalAnimalsFinder maximalAnimalsFinder = new MaximalAnimalsFinder();
                Animal maximalAnimal = maximalAnimalsFinder.getOneMax(animalsOnTile);
                maximalAnimal.consume();
                stats.reportPlantConsumption();

                if(insideJungle(position)) noPlantsFieldsForJungle.add(position);
                else noPlantsFieldsForSteps.add(position);
            }
        }
        plantPositions.removeIf(pos -> animalPositions.get(pos) != null);
    }

    public void reproduceAnimals() {
        for(List<Animal> animalsOnTile : animalPositions.values()) {
            if(animalsOnTile.size() >= 2) {
                MaximalAnimalsFinder maximalAnimalsFinder = new MaximalAnimalsFinder();
                List<Animal> maximalAnimals = maximalAnimalsFinder.getTwoMax(animalsOnTile);
                Animal parent1 = maximalAnimals.get(0);
                Animal parent2 = maximalAnimals.get(1);
                Animal child = parent1.reproduce(parent2);
                if(child != null) {
                    animalsOnTile.add(child);
                    stats.reportAddingAnimalHavingParents(child, parent1, parent2);
                }

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
        if(!(animalPositions.get(position) == null)) {
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

    public void addObserver(ChangeListener observer) {
        observers.add(observer);
    }

    public void removeObserver(ChangeListener observer) {
        observers.remove(observer);
    }

    public void atMapChanged(String str) {
        for(ChangeListener observer : observers) {
            observer.mapChanged(this, str, stats);
        }
    }

    public HashSet<Vector2d> getPlantPositions() {
        return plantPositions;
    }

    public List<Vector2d> getNoPlantsFieldsForJungle() {
        return noPlantsFieldsForJungle;
    }

    public List<Vector2d> getNoPlantsFieldsForSteps() {
        return noPlantsFieldsForSteps;
    }

    public Map<Vector2d, List<Animal>> getAnimalPositions() {
        return animalPositions;
    }

    // only for tests
    public void setAnimalPositionsForTests(Map<Vector2d, List<Animal>> animalPositions) {
        // delete previous animals from stats
        stats.reportClearingAnimalListForTests();

        // replace the hash set of existing animals
        this.animalPositions = animalPositions;

        // add new animals to stats
        for (List<Animal> animalList : animalPositions.values()){
            for (Animal animal : animalList){
                stats.reportAddingStartingAnimal(animal);
            }
        }
    }

    // only for tests
    public void setPlantPositionsForTests(HashSet<Vector2d> newPlantPositions) {
        // remove all plants
        for (Vector2d position : plantPositions){
            if (insideJungle(position)){
                noPlantsFieldsForJungle.add(position);
            }
            else {
                noPlantsFieldsForSteps.add(position);
            }
        }
        plantPositions.clear();

        // set new plants
        for (Vector2d position : newPlantPositions){
            if (insideJungle(position)){
                noPlantsFieldsForJungle.remove(position);
            }
            else {
                noPlantsFieldsForJungle.remove(position);
            }
            plantPositions.add(position);
        }
    }
}
