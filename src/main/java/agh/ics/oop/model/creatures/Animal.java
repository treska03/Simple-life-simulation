package agh.ics.oop.model.creatures;

import agh.ics.oop.model.info.Stats;
import agh.ics.oop.model.info.StatsList;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.enums.MapDirection;
import agh.ics.oop.model.info.Constants;
import agh.ics.oop.model.info.ConstantsList;
import agh.ics.oop.model.util.PositionsGenerator;
import agh.ics.oop.model.util.RandomNumberGenerator;
import java.util.UUID;

public class Animal implements WorldElement {
    private final int simulationId;
    private final Constants constants;
    private final Stats stats;
    private final UUID id;
    private MapDirection orientation;
    private Vector2d position;
    private int currentEnergy;
    private int childrenNumber = 0;
    private int numberOfEatenPlants = 0;
    private final int dateOfBirth;
    private Genome genome;

    private Animal(Vector2d start, int simulationId, Genome genome, int energy){
        this.position = start;
        this.simulationId = simulationId;
        this.id = UUID.randomUUID();
        this.orientation = MapDirection.values()[(RandomNumberGenerator.getRandomInRange(7))];
        this.constants = ConstantsList.getConstants(simulationId);
        this.currentEnergy = energy;
        this.stats = StatsList.getStats(simulationId);
        this.dateOfBirth = stats.getDay();
        this.genome = genome;
    }

    public static Animal fromParents(Animal p1, Animal p2) {
        Constants constants = ConstantsList.getConstants(p1.getSimulationId());
        Genome genome = Genome.fromParents(p1, p2);
        int energy = 2 * constants.getEnergyUsedForReproduction();
        return new Animal(p1.getPosition(), p1.simulationId, genome, energy);
    }

    public static Animal startingAnimal(int simulationId) {
        Constants constants = ConstantsList.getConstants(simulationId);
        Genome genome = Genome.startingAnimalGenome(simulationId);
        Vector2d startPos = PositionsGenerator.generateRandomPosition(constants.getMapBoundary());
        int energy = constants.getNewAnimalEnergy();
        return new Animal(startPos, simulationId, genome, energy);
    }

    public String toString() {
        return String.format("%s", orientation);
    }

    public boolean isAt(Vector2d atPos) {
        return position.equals(atPos);
    }

    public void move() {
        // get new orientation and move according to it forward
        orientation = orientation.rotate(genome.getCurrentMove());
        position = position.add(orientation.toUnitVector());
    }

    public void consume() {
        currentEnergy += constants.getEnergyFromPlant();
        numberOfEatenPlants++;
    }

    public Animal reproduce(Animal animal) {

        if(currentEnergy < constants.getEnergyRequiredForReproduction() ||
           animal.getCurrentEnergy() < constants.getEnergyRequiredForReproduction()) {
            return null;
        }

        Animal child = Animal.fromParents(this, animal);

        this.removeEnergy(constants.getEnergyUsedForReproduction());
        animal.removeEnergy(constants.getEnergyUsedForReproduction());
        this.addChildrenCount();
        animal.addChildrenCount();

        return child;
    }

    public MapDirection getOrientation() {
        return orientation;
    }

    public void spinAnimal() {
        orientation = orientation.rotate(4);
    }

    public Vector2d getPosition() {
        return position;
    }

    public int getCurrentEnergy() {
        return currentEnergy;
    }

    public Genome getGenome() {
        return genome;
    }

    public UUID getId() {
        return id;
    }

    public int getSimulationId() {
        return simulationId;
    }

    public void removeEnergy(int energyToRemove) {
        this.currentEnergy -= energyToRemove;
    }

    public void addChildrenCount() {
        childrenNumber++;
    }

    public int getChildrenNumber() {
        return childrenNumber;
    }

    public int getNumberOfEatenPlants() {
        return numberOfEatenPlants;
    }

    public int getDateOfBirth() {
        return dateOfBirth;
    }

    public void setPosition(Vector2d position) {
        this.position = position;
    }

    // only for tests
    public void setCurrentEnergyForTests(int currentEnergy) {
        this.currentEnergy = currentEnergy;
    }

    // only for tests
    public void setGenesForTests(Genome genes) {
        this.genome = genes;
    }
}
