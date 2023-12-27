package agh.ics.oop.model.map;

import agh.ics.oop.model.DummyDataForTests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WorldMapTest {

    private final int simulationId = 0;
    private WorldMap worldMap;

    @BeforeEach
    void setUp() {
        DummyDataForTests.setUpConstants(simulationId);
        this.worldMap = new NormalMap(simulationId);
    }

    @Test
    void removeDeadAnimals() {
        worldMap.removeDeadAnimals();

    }

    @Test
    void moveAnimals() {
        worldMap.moveAnimals();
    }

    @Test
    void feedAnimals() {
        worldMap.feedAnimals();
    }

    @Test
    void reproduceAnimals() {
        worldMap.reproduceAnimals();
    }

    @Test
    void growPlants() {
        worldMap.growPlants();
    }
}