package agh.ics.oop.model.info;

import java.util.HashMap;

public class StatsList {

    private static final HashMap<Integer, Stats> statsList = new HashMap<>();

    public static void addToStatsList(int simulationId, Stats stats) {
        statsList.put(simulationId, stats);
    }

    public static Stats getStats(int simulationId) {
        return statsList.get(simulationId);
    }
}
