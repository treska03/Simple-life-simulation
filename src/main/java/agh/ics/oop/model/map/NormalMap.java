package agh.ics.oop.model.map;

import agh.ics.oop.model.Animal;
import agh.ics.oop.model.enums.MoveDirection;

public class NormalMap extends AbstractWorldMap {
    public NormalMap() {
        super();
    }

    @Override
    public void move(Animal animal, MoveDirection direction){
        super.move(animal, direction);
    }

}
