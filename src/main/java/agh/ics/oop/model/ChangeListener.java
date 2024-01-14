package agh.ics.oop.model;

import agh.ics.oop.model.info.Stats;
import agh.ics.oop.model.map.WorldMap;

public interface ChangeListener {
    void mapChanged(WorldMap worldMap, String message, Stats stats);

}
