package agh.ics.oop;

import agh.ics.oop.model.info.Constants;
import agh.ics.oop.model.info.ConstantsList;
import agh.ics.oop.model.info.Stats;
import agh.ics.oop.model.info.StatsList;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.map.WorldMap;
import java.util.List;

public class Simulation implements Runnable{
    //TODO Add List containing all animals that were ever present (dead too)
    private final int simulationId;
    private final Constants constants;
    private final Stats stats;
    private final WorldMap gameMap;

    public Simulation(WorldMap map, List<Vector2d> startPositions, int simulationId) {
        this.simulationId = simulationId;
        this.constants = ConstantsList.getConstants(simulationId);
        this.stats = StatsList.getStats(simulationId);
        this.gameMap = map;
    }

    @Override
    public void run() {

        for (int day = 1; day <= 10; day++) { // end condition only temporarily

            gameMap.reduceAnimalEnergy();
            gameMap.removeDeadAnimals();
            gameMap.moveAnimals();
            gameMap.feedAnimals();
            gameMap.reproduceAnimals();
            gameMap.growPlants();
            stats.reportEndOfTheDay(gameMap);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
