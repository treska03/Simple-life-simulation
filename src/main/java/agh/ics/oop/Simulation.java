package agh.ics.oop;

import agh.ics.oop.model.ConsoleMapDisplay;
import agh.ics.oop.model.info.Constants;
import agh.ics.oop.model.info.ConstantsList;
import agh.ics.oop.model.info.Stats;
import agh.ics.oop.model.info.StatsList;
import agh.ics.oop.model.map.NormalMap;
import agh.ics.oop.model.util.MapVisualizer;
import agh.ics.oop.model.util.Vector2d;
import agh.ics.oop.model.map.WorldMap;
import java.util.List;

public class Simulation implements Runnable{
    //TODO Add List containing all animals that were ever present (dead too)
    private final int simulationId;
    private final Constants constants;
    private final Stats stats;
    private final WorldMap gameMap;
    private final MapVisualizer mapVisualizer;

    public Simulation(int simulationId) {
        this.simulationId = simulationId;
        this.constants = ConstantsList.getConstants(simulationId);
        this.stats = StatsList.getStats(simulationId);
        this.gameMap = new NormalMap(simulationId);
        gameMap.addObserver(new ConsoleMapDisplay());
        stats.reportEndOfTheDay(gameMap); // end of day 0
        this.mapVisualizer = new MapVisualizer(gameMap);
    }

    @Override
    public void run() {

        for (int day = 1; day <= 50; day++) { // end condition only temporarily

            gameMap.reduceAnimalEnergy();
            gameMap.removeDeadAnimals();
            gameMap.moveAnimals();
            gameMap.feedAnimals();
            gameMap.reproduceAnimals();
            gameMap.growPlants();
            stats.reportEndOfTheDay(gameMap);
            gameMap.atMapChanged("Day " + day + " passed!");

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public WorldMap getGameMap() {
        return gameMap;
    }

}
