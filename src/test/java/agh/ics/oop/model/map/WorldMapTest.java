package agh.ics.oop.model.map;

import agh.ics.oop.model.DummyDataForTests;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.creatures.Animal;
import agh.ics.oop.model.info.ConstantsList;
import agh.ics.oop.model.util.Boundary;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class WorldMapTest {

    private final int simulationId = 1;
    private WorldMap worldMap;

    @BeforeEach
    void setUp() {
//        boolean BACK_AND_FORTH = false;
//        int NUMBER_OF_GENS = 10;
//        int MIN_MUTATIONS = 0;
//        int MAX_MUTATIONS = 3;
//        int MIN_ENERGY_FOR_REPRODUCTION = 30;
//        int ENERGY_USED_FOR_REPRODUCTION = 25;
//        int NEW_ANIMAL_ENERGY = 15;
//        int DAILY_NEW_GRASS_NUMBER = 25;
//        int ENERGY_FROM_PLANT = 20;
//        Boundary MAP_BOUNDARY = new Boundary(new Vector2d(0, 0), new Vector2d(10, 10));
        DummyDataForTests.setUpConstants(simulationId);
        this.worldMap = new NormalMap(simulationId);
    }

    @Test
    void removeDeadAnimals() {
        worldMap.removeDeadAnimals();

        int i = 0;
        //
        for(List<Animal> animalList : worldMap.animalPositions.values()) {
            for(Animal animal : animalList) {
                if(i == 0) {
                    animal.setCurrentEnergy(0);
                }
                i = (i + 1) % 3;
            }
        }

        //Count animals whether it removes animals with more than 0 energy
        int animalCount = 0;
        for(List<Animal> animalList : worldMap.animalPositions.values()) {
            for(Animal animal : animalList) {
                animalCount++;
            }
        }

        worldMap.removeDeadAnimals();

        int animalCountAfterClear = 0;
        for(List<Animal> animalList : worldMap.animalPositions.values()) {
            for(Animal animal : animalList) {
                animalCountAfterClear++;
            }
        }

        Assertions.assertEquals(15, animalCount);
        Assertions.assertEquals(10, animalCountAfterClear);


    }

    @Test
    void moveAnimals() {
        // Sometimes it randomly doesn't work

        Optional<List<Animal>> firstList = worldMap.animalPositions.values().stream().findFirst();

        worldMap.moveAnimals();

        Optional<List<Animal>> secondList = worldMap.animalPositions.values().stream().findFirst();

        worldMap.moveAnimals();

        Optional<List<Animal>> thirdList = worldMap.animalPositions.values().stream().findFirst();

        // Check if anything changed at all
        Assertions.assertNotEquals(firstList, secondList);
        Assertions.assertNotEquals(firstList, thirdList);
    }

    @Test
    void feedAnimals() {
        Animal animal1 = Animal.startingAnimal(simulationId);
        Animal animal2 = Animal.startingAnimal(simulationId);
        Animal animal3 = Animal.startingAnimal(simulationId);
        Animal animal4 = Animal.startingAnimal(simulationId);

        animal1.setCurrentEnergy(50);
        animal2.setCurrentEnergy(50);
        animal3.setCurrentEnergy(50);
        animal4.setCurrentEnergy(40);

        animal1.setPosition(new Vector2d(0, 0));
        animal2.setPosition(new Vector2d(1, 0));
        animal3.setPosition(new Vector2d(2, 0));
        animal4.setPosition(new Vector2d(2, 0));

        worldMap.addToAnimalMap(worldMap.animalPositions, animal1);
        worldMap.addToAnimalMap(worldMap.animalPositions, animal2);
        worldMap.addToAnimalMap(worldMap.animalPositions, animal3);
        worldMap.addToAnimalMap(worldMap.animalPositions, animal4);

        worldMap.plantPositions.add(new Vector2d(0, 0));
        worldMap.plantPositions.add(new Vector2d(1, 0));
        worldMap.plantPositions.add(new Vector2d(2, 0));

        worldMap.feedAnimals();
        Assertions.assertEquals(70, animal1.getCurrentEnergy());
        Assertions.assertEquals(70, animal2.getCurrentEnergy());
        Assertions.assertEquals(70, animal3.getCurrentEnergy());
        Assertions.assertEquals(40, animal4.getCurrentEnergy());
    }

    @Test
    void reproduceAnimals() {
        worldMap.reproduceAnimals();
    }

    @Test
    void growPlants() {
        Assertions.assertEquals(0, worldMap.plantPositions.size());
        worldMap.growPlants();
        Assertions.assertEquals(25, worldMap.plantPositions.size());
        worldMap.growPlants();
        Assertions.assertEquals(50, worldMap.plantPositions.size());
        worldMap.growPlants();
        Assertions.assertEquals(75, worldMap.plantPositions.size());
        worldMap.growPlants();
        Assertions.assertEquals(100, worldMap.plantPositions.size());
        worldMap.growPlants();
        Assertions.assertEquals(121, worldMap.plantPositions.size());
        worldMap.growPlants();
        Assertions.assertEquals(121, worldMap.plantPositions.size());
    }
}