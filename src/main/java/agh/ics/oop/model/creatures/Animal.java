package agh.ics.oop.model.creatures;

import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.enums.MapDirection;
import agh.ics.oop.model.info.Constants;
import agh.ics.oop.model.info.ConstantsList;
import agh.ics.oop.model.util.PositionsGenerator;
import agh.ics.oop.model.util.RandomNumberGenerator;

public class Animal implements WorldElement {
    private final int simulationId;
    private final Constants constants;
    private MapDirection orientation;
    private Vector2d position;
    private int currentEnergy;
    private int childrenNumber = 0;
    private Genes genes;

    private Animal(Vector2d start, int simulationId, Genes genes){
        this.position = start;
        this.simulationId = simulationId;
        this.orientation = MapDirection.values()[(RandomNumberGenerator.getRandomInRange(7))];
        this.constants = ConstantsList.getConstants(simulationId);
        this.currentEnergy = constants.getNewAnimalEnergy();
        this.genes = genes;
    }

    public static Animal fromParents(Animal p1, Animal p2) {
        Genes genes = Genes.fromParents(p1, p2);
        return new Animal(p1.getPosition(), p1.simulationId, genes);
    }

    public static Animal startingAnimal(int simulationId) {
        Genes genes = Genes.startingAnimalGenes(simulationId);
        Vector2d startPos = PositionsGenerator.generateRandomPosition(
                ConstantsList.getConstants(simulationId).getMapBoundary());
        return new Animal(startPos, simulationId, genes);
    }

    public String toString() {
        return String.format("%s", orientation);
    }

    public boolean isAt(Vector2d atPos) {
        return position.equals(atPos);
    }

    public void move() {
        // get new orientation and move according to it forward
        orientation = orientation.rotate(genes.getCurrentMove());
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

    public Genes getGenes() {
        return genes;
    }

    public void setCurrentEnergy(int currentEnergy) { // only for tests
        this.currentEnergy = currentEnergy;
    }

    public void removeEnergy(int energyToRemove) {
        this.currentEnergy -= energyToRemove;
    }

    public void setGenes(Genes genes) { // only for tests
        this.genes = genes;
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
}
