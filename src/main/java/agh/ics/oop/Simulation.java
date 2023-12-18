package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.map.WorldMap;
import agh.ics.oop.model.util.Vector2d;
import java.util.ArrayList;
import java.util.List;

public class Simulation implements Runnable{
    private final WorldMap gameMap;
    private final List<Animal> animalList = new ArrayList<>();

    public Simulation(WorldMap map, List<Vector2d> startPositions) {
        this.gameMap = map;
        initAnimals(gameMap, startPositions);
    }


    private void initAnimals(WorldMap map, List<Vector2d> startPositions) {
        for(Vector2d position : startPositions) {
            Animal newAnimal = new Animal(position);
            map.place(newAnimal);
            animalList.add(newAnimal);
        }
    }

    @Override
    public void run() {
        int i = 0;

        for(int tick = 0; tick < Constances.getNumberOfTicks(); tick++) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Animal currentAnimal = animalList.get(i);
            gameMap.move(currentAnimal);
            i = (i+1) % (animalList.size());
        }
    }

    public List<Animal> getAnimalList() {
        return animalList;
    }
}
