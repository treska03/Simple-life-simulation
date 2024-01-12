package agh.ics.oop;

import agh.ics.oop.model.ConsoleMapDisplay;
import agh.ics.oop.model.info.Constants;
import agh.ics.oop.model.info.ConstantsList;
import agh.ics.oop.model.info.Stats;
import agh.ics.oop.model.info.StatsList;
import agh.ics.oop.model.map.NormalMap;
import agh.ics.oop.model.map.PortalMap;
import agh.ics.oop.model.map.WorldMap;

public class Simulation implements Runnable{
    //TODO Add List containing all animals that were ever present (dead too)
    private final int simulationId;
    private final Constants constants;
    private final Stats stats;
    private final WorldMap gameMap;
    private boolean finished = false;
    private boolean paused = true;

    public Simulation(int simulationId) {
        this.simulationId = simulationId;
        this.constants = ConstantsList.getConstants(simulationId);
        this.stats = StatsList.getStats(simulationId);

        if(constants.isPortalToHell()) {
            this.gameMap = new PortalMap(simulationId);
        }
        else {
            this.gameMap = new NormalMap(simulationId);

        }
        gameMap.addObserver(new ConsoleMapDisplay());
        stats.reportEndOfTheDay(gameMap); // end of day 0
    }

    @Override
    public void run() {
        while(!finished) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if(!paused) {
                singleDayLoop();
            }
        }
    }

    private void singleDayLoop() {
        gameMap.reduceAnimalEnergy();
        gameMap.removeDeadAnimals();
        gameMap.moveAnimals();
        gameMap.feedAnimals();
        gameMap.reproduceAnimals();
        gameMap.growPlants();
        stats.reportEndOfTheDay(gameMap);
        gameMap.atMapChanged("Day " + stats.getDay() + " passed!");

        if(stats.getNumberOfLiveAnimals() == 0) {
            finished = true;
        }
    }

    public void resumeGame() {
        paused = false;
    }

    public void pauseGame() {
        paused = true;
    }

    public void forceEnd() {
        finished = true;
    }

    public WorldMap getGameMap() {
        return gameMap;
    }

}
