package agh.ics.oop.model.map;

import agh.ics.oop.model.creatures.Animal;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.PositionsGenerator;

public class PortalMap extends WorldMap{

    public PortalMap(int simulationId) {
        super(simulationId);
    }

    @Override
    void moveSingleAnimal(Animal animal) {
        animal.move();
        Boundary mapBoundary = constants.getMapBoundary();
        Vector2d currPos = animal.getPosition();

        if(!mapBoundary.insideBoundary(currPos)) {
            animal.setPosition(PositionsGenerator.generateRandomPosition(mapBoundary));
        }
    }
}
