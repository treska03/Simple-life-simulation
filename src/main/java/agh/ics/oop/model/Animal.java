package agh.ics.oop.model;

import agh.ics.oop.model.enums.MapDirection;
import agh.ics.oop.model.info.Constants;
import agh.ics.oop.model.info.ConstantsList;
import agh.ics.oop.model.util.RandomNumberGenerator;

import java.util.ArrayList;
import java.util.List;

public class Animal implements WorldElement {
    private final int simulationId;
    private final Constants constants;
    private MapDirection orientation;
    private Vector2d position;
    private int currentEnergy;
    private int childrenNumber = 0;
    private final Animal parent1;
    private final Animal parent2;
    private Genes genes;

    public Animal(Vector2d start, int simulationId, Animal parent1, Animal parent2){
        this.position = start;
        this.simulationId = simulationId;
        this.orientation = MapDirection.values()[(RandomNumberGenerator.getRandomInRange(7))];
        this.constants = ConstantsList.getConstants(simulationId);
        this.currentEnergy = constants.getNewAnimalEnergy();

        this.parent1 = parent1;
        this.parent2 = parent2;
        if(parent1 != null & parent2 != null) genes = Genes.fromParents(parent1, parent2);
        else genes = new Genes(simulationId); // I dont think it works right now
    }

    public Animal(Vector2d start, int simulationId) {
        this(start, simulationId, null, null);
    }

    public String toString() {
        return String.format("%s", orientation);
    }

    public boolean isAt(Vector2d atPos) {
        return position.equals(atPos);
    }

    public void move() {
        // get new orientation and move according to it forward
        orientation = MapDirection.values()[(orientation.ordinal() + genes.getCurrentMove())];
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

        Animal child = new Animal(position, simulationId, this, animal);

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
