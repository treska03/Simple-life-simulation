package agh.ics.oop.model.util;

import agh.ics.oop.model.ConstantSetterForTests;
import agh.ics.oop.model.creatures.Animal;
import agh.ics.oop.model.info.Stats;
import agh.ics.oop.model.info.StatsList;
import agh.ics.oop.model.map.NormalMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

public class MaximalAnimalsFinderTest {
    private Stats stats;

    @BeforeEach
    void setUp() {
        // creating stats and default constants
        ConstantSetterForTests constantSetter = new ConstantSetterForTests();
        constantSetter.setUpConstants(1);
        this.stats = new Stats(1);
        StatsList.addToStatsList(1, stats);
    }

    @Test
    public void testIsGreater(){
        // create starting animals: animal1, animal2 and animal3
        // and set their energy;
        Animal animal1 = Animal.startingAnimal(1);
        animal1.setCurrentEnergyForTests(100);
        Animal animal2 = Animal.startingAnimal(1);
        animal2.setCurrentEnergyForTests(100);
        Animal animal3 = Animal.startingAnimal(1);
        animal3.setCurrentEnergyForTests(100);

        // animal1, animal2 and animal3 were born in day1,
        // the next animals will be born in day 2;
        stats.reportEndOfTheDay(new NormalMap(1));

        // create animal4, whose parents are animal1 and animal3
        // and set energy for animal4 and renew it for its parents;
        Animal animal4 = animal1.reproduce(animal3);
        animal4.setCurrentEnergyForTests(100);
        animal1.setCurrentEnergyForTests(100);
        animal3.setCurrentEnergyForTests(100);

        // create animal5, whose parents are animal1 and animal3
        // and set energy for animal5 and renew it for its parents;
        Animal animal5 = animal2.reproduce(animal3);
        animal5.setCurrentEnergyForTests(80);
        animal2.setCurrentEnergyForTests(100);
        animal3.setCurrentEnergyForTests(100);

        /*
           animal  |  energy  |  date of birth  |  number of children
         __________|__________|_________________|_____________________
          animal1  |    100   |        1        |          1
          animal2  |    100   |        1        |          1
          animal3  |    100   |        1        |          2
          animal4  |    100   |        2        |          0
          animal5  |    80    |        2        |          0
        */

        MaximalAnimalsFinder maximalAnimalsFinder = new MaximalAnimalsFinder();

        // check if animal1 is greater or not
        Assertions.assertTrue(maximalAnimalsFinder.checkIsGreaterForTests(animal1, animal5));
        Assertions.assertTrue(maximalAnimalsFinder.checkIsGreaterForTests(animal1, animal4));
        Assertions.assertFalse(maximalAnimalsFinder.checkIsGreaterForTests(animal1, animal3));
        Assertions.assertFalse(maximalAnimalsFinder.checkIsGreaterForTests(animal1, animal2));

        // check if animal1 is smaller or not
        Assertions.assertFalse(maximalAnimalsFinder.checkIsGreaterForTests(animal5, animal1));
        Assertions.assertFalse(maximalAnimalsFinder.checkIsGreaterForTests(animal4, animal1));
        Assertions.assertTrue(maximalAnimalsFinder.checkIsGreaterForTests(animal3, animal1));
        Assertions.assertFalse(maximalAnimalsFinder.checkIsGreaterForTests(animal2, animal1));
    }

    @Test
    public void testGetOneMax(){
        // create animals and set their energy;
        // animal2 will have the most energy;
        Animal animal1 = Animal.startingAnimal(1);
        animal1.setCurrentEnergyForTests(100);
        Animal animal2 = Animal.startingAnimal(1);
        animal2.setCurrentEnergyForTests(150);
        Animal animal3 = Animal.startingAnimal(1);
        animal3.setCurrentEnergyForTests(50);

        // add animals to the list (order doesn't matter)
        List<Animal> listOfAnimals = new ArrayList<>();
        listOfAnimals.add(animal1);
        listOfAnimals.add(animal2);
        listOfAnimals.add(animal3);

        // find maximal animal
        MaximalAnimalsFinder maximalAnimalsFinder = new MaximalAnimalsFinder();
        Animal returnedAnimal = maximalAnimalsFinder.getOneMax(listOfAnimals);

        // check if maximal animal is animal2 by theirs id
        Assertions.assertEquals(animal2.getId(), returnedAnimal.getId());
    }

    @Test
    public void testGetTwoMax(){
        // create animals and set their energy;
        // animal3 and animal4 will have the most energy;
        Animal animal1 = Animal.startingAnimal(1);
        animal1.setCurrentEnergyForTests(50);
        Animal animal2 = Animal.startingAnimal(1);
        animal2.setCurrentEnergyForTests(100);
        Animal animal3 = Animal.startingAnimal(1);
        animal3.setCurrentEnergyForTests(150);
        Animal animal4 = Animal.startingAnimal(1);
        animal4.setCurrentEnergyForTests(200);

        // add animals to the list (order doesn't matter)
        List<Animal> listOfAnimals = new ArrayList<>();
        listOfAnimals.add(animal1);
        listOfAnimals.add(animal2);
        listOfAnimals.add(animal3);
        listOfAnimals.add(animal4);

        MaximalAnimalsFinder maximalAnimalsFinder = new MaximalAnimalsFinder();

        // find maximal animals in the list
        List<Animal> returnedAnimals = maximalAnimalsFinder.getTwoMax(listOfAnimals);

        // check if maximal animals in the list are animal3 and animal4 by theirs id;
        // we know that the greatest animal will be on the index 0 in the returned list
        Assertions.assertEquals(animal4.getId(), returnedAnimals.get(0).getId());
        Assertions.assertEquals(animal3.getId(), returnedAnimals.get(1).getId());
    }
}