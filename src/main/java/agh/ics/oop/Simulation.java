package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.info.Constants;
import agh.ics.oop.model.info.ConstantsList;
import agh.ics.oop.model.info.Stats;
import agh.ics.oop.model.info.StatsList;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.map.WorldMap;

import java.util.ArrayList;
import java.util.List;

public class Simulation implements Runnable{
    //TODO Add List containing all animals that were ever present (dead too)
    private final int simulationId;
    private final Constants constants;
    private final Stats stats;
    private final WorldMap gameMap;
    private final List<Animal> animalList = new ArrayList<>();

    public Simulation(WorldMap map, List<Vector2d> startPositions, int simulationId) {
        this.simulationId = simulationId;
        this.constants = ConstantsList.getConstants(simulationId);
        this.stats = StatsList.getStats(simulationId);
        this.gameMap = map;
        initAnimals(gameMap, startPositions);
    }


    private void initAnimals(WorldMap map, List<Vector2d> startPositions) {
        for(Vector2d position : startPositions) {
            Animal newAnimal = new Animal(position, simulationId);
//            map.place(newAnimal);
            animalList.add(newAnimal);
        }
    }

    @Override
    public void run() {
        int i = 0;

        for(int tick = 0; tick < stats.getNumberOfTicks(); tick++) {

            gameMap.removeDeadAnimals();
            gameMap.moveAnimals();
            gameMap.feedAnimals();
            gameMap.reproduceAnimals();
            gameMap.growPlants();

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
