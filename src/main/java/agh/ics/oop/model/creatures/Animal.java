package agh.ics.oop.model.creatures;

import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.enums.MapDirection;
import agh.ics.oop.model.info.Constants;
import agh.ics.oop.model.info.ConstantsList;
import agh.ics.oop.model.util.PositionsGenerator;
import agh.ics.oop.model.util.RandomNumberGenerator;
import java.util.UUID;

public class Animal implements WorldElement {
    private final int simulationId;
    private final Constants constants;
    private final UUID id;
    private MapDirection orientation;
    private Vector2d position;
    private int currentEnergy;
    private int childrenNumber = 0;
    private Genome genome;

    private Animal(Vector2d start, int simulationId, Genome genes){
        this.position = start;
        this.simulationId = simulationId;
        this.id = UUID.randomUUID();
        this.orientation = MapDirection.values()[(RandomNumberGenerator.getRandomInRange(7))];
        this.constants = ConstantsList.getConstants(simulationId);
        this.currentEnergy = constants.getNewAnimalEnergy();
        this.genome = genes;
    }

    public static Animal fromParents(Animal p1, Animal p2) {
        Genome genome = Genome.fromParents(p1, p2);
        return new Animal(p1.getPosition(), p1.simulationId, genome);
    }

    public static Animal startingAnimal(int simulationId) {
        Genome genome = Genome.startingAnimalGenome(simulationId);
        Vector2d startPos = PositionsGenerator.generateRandomPosition(
                ConstantsList.getConstants(simulationId).getMapBoundary());
        return new Animal(startPos, simulationId, genome);
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
        this.currentEnergy += constants.getEnergyFromPlant();
    }

    public Animal reproduce(Animal animal) {

        if(currentEnergy < constants.getEnergyRequiredForReproduction() ||
           animal.getCurrentEnergy() < constants.getEnergyRequiredForReproduction()) {
            return null;
        }

        Animal child = Animal.fromParents(this, animal);

        this.removeEnergy(0); //TODO: FIX REMOVING ENERGY
        animal.removeEnergy(0);
        this.addChildrenCount();
        animal.addChildrenCount();
        this.addChildrenCountToAncestors();
        animal.addChildrenCountToAncestors();

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

    public Genome getGenes() {
        return genome;
    }

    public UUID getId() {
        return id;
    }

    public void removeEnergy(int energyToRemove) {
        this.currentEnergy -= energyToRemove;
    }

    public void addChildrenCount() {
        childrenNumber++;
    }
    public void addChildrenCountToAncestors() {
        return;
    }

    public int getChildrenNumber() {
        return childrenNumber;
    }

    public void setPosition(Vector2d position) {
        this.position = position;
    }

    // only for tests
    public void setCurrentEnergyForTests(int currentEnergy) {
        this.currentEnergy = currentEnergy;
    }

    // Only for tests
    public void setGenesForTests(Genome genes) {
        this.genome = genes;
    }
}
