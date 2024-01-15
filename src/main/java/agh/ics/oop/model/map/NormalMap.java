package agh.ics.oop.model.map;

import agh.ics.oop.model.creatures.Animal;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.util.Boundary;

public class NormalMap extends WorldMap {

    public NormalMap(int simulationId) {
        super(simulationId);
    }

    @Override
    protected void moveSingleAnimal(Animal animal) {
        animal.move();
        Boundary mapBoundary = constants.getMapBoundary();
        Vector2d currPos = animal.getPosition();

        if(!mapBoundary.insideBoundary(currPos)) {
            if(currPos.getY() > mapBoundary.upperRight().getY()) {
                animal.setPosition(new Vector2d(currPos.getX() - animal.getOrientation().toUnitVector().getX(), mapBoundary.upperRight().getY()));
                animal.spinAnimal();
            }
            else if(currPos.getY() < mapBoundary.lowerLeft().getY()) {
                animal.setPosition(new Vector2d(currPos.getX() - animal.getOrientation().toUnitVector().getX(), mapBoundary.lowerLeft().getY()));
                animal.spinAnimal();
            }
            else if(currPos.getX() < mapBoundary.lowerLeft().getX()) animal.setPosition(new Vector2d(mapBoundary.upperRight().getX(), currPos.getY()));
            else if(currPos.getX() > mapBoundary.upperRight().getX()) animal.setPosition(new Vector2d(mapBoundary.lowerLeft().getX(), currPos.getY()));
        }
    }
}
