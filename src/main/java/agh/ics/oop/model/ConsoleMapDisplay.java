package agh.ics.oop.model;

import agh.ics.oop.model.info.Stats;
import agh.ics.oop.model.map.WorldMap;

import java.util.ArrayList;
import java.util.List;

public class ConsoleMapDisplay implements ChangeListener {

    List<String> changeLog = new ArrayList<>();

    @Override
    public void mapChanged(WorldMap worldMap, String message, Stats stats) {
        synchronized (this) {
            changeLog.add(message);
            System.out.println();
            System.out.printf("Map number: #%d%n", worldMap.getSimulationId());
            System.out.println(message);
            System.out.println(worldMap);
            System.out.printf("Total number of changes: %d%n", changeLog.size());
//        System.out.printf("Map number: #%d%n%s%n%sTotal number of changes: %d%n", worldMap.getId(), message, worldMap, changeLog.size());
        }

    }

}
