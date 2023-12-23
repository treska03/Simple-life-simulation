package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.info.Constants;
import agh.ics.oop.model.info.ConstantsList;
import agh.ics.oop.model.map.WorldMap;
import agh.ics.oop.model.Vector2d;
import java.util.ArrayList;
import java.util.List;

public class Simulation implements Runnable{
    private final int simulationId;
    private final Constants constants;
    private final WorldMap gameMap;
    private final List<Animal> animalList = new ArrayList<>();

    public Simulation(WorldMap map, List<Vector2d> startPositions, int simulationId) {
        this.simulationId = simulationId;
        this.constants = ConstantsList.getConstants(simulationId);
        this.gameMap = map;
        initAnimals(gameMap, startPositions);
    }


    private void initAnimals(WorldMap map, List<Vector2d> startPositions) {
        for(Vector2d position : startPositions) {
            Animal newAnimal = new Animal(position, simulationId);
            map.place(newAnimal);
            animalList.add(newAnimal);
        }
    }

    @Override
    public void run() {
        int i = 0;

        for(int tick = 0; tick < constants.getNumberOfTicks(); tick++) {
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
