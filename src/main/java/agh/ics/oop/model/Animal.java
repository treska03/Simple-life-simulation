package agh.ics.oop.model;

import agh.ics.oop.model.enums.MapDirection;
import agh.ics.oop.model.info.Constants;
import agh.ics.oop.model.info.ConstantsList;

import java.util.ArrayList;
import java.util.List;

public class Animal implements WorldElement {
    private final int simulationId;
    private final Constants constants;
    private MapDirection orientation;
    private Vector2d position;
    private int currentEnergy;


    private int childrenNumber;
    private Genes genes;

    public Animal(Vector2d start, int simulationId){
        this.simulationId = simulationId;
        this.orientation = MapDirection.NORTH;
        this.constants = ConstantsList.getConstants(simulationId);
        this.position = start;
    }

    public String toString() {
        return String.format("%s", orientation);
    }

    public boolean isAt(Vector2d atPos) {
        return position.equals(atPos);
    }

    public void move() {
        // Not working atm

        List<Vector2d> moveVectors = new ArrayList<>(
                List.of(MapDirection.NORTH.toUnitVector(), MapDirection.EAST.toUnitVector(), MapDirection.SOUTH.toUnitVector(), MapDirection.WEST.toUnitVector())
        );

        this.position = position.add(moveVectors.get(orientation.ordinal()));
    }

    public void consume() {
        this.currentEnergy += constants.getEnergyFromPlant();
    }

    public void reproduce(Animal animal) {
        //TODO Add reproduce implementation for animal
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

    public void setGenes(Genes genes) { // only for tests
        this.genes = genes;
    }

    public void addChildrenCount() {
        childrenNumber++;
    }

    public int getChildrenNumber() {
        return childrenNumber;
    }
}
