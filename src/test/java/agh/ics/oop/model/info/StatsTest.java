package agh.ics.oop.model.info;

import agh.ics.oop.model.ConstantSetterForTests;
import agh.ics.oop.model.creatures.Animal;
import agh.ics.oop.model.creatures.Genome;
import agh.ics.oop.model.enums.MapDirection;
import agh.ics.oop.model.map.NormalMap;
import agh.ics.oop.model.map.WorldMap;
import agh.ics.oop.model.util.GraphVertex;
import agh.ics.oop.model.util.Vector2d;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;

public class StatsTest {
    private Stats stats;
    private Constants constants;

    @BeforeEach
    void setUp() {
        // creating stats and default constants
        ConstantSetterForTests constantSetter = new ConstantSetterForTests();
        constantSetter.setNumberOfGenes(6);
        constantSetter.setEnergyRequiredForReproduction(25);
        constantSetter.setEnergyUsedForReproduction(10);
        constantSetter.setNewAnimalEnergy(15);
        constantSetter.setDailyEnergyLoss(4);
        constantSetter.setEnergyFromPlant(21);
        constantSetter.setUpConstants(1);
        this.constants = ConstantsList.getConstants(1);
        this.stats = new Stats(1);
        StatsList.addToStatsList(1, stats);
    }

    @Test
    public void testAddStartingAnimal(){
        // this test only check
        // if animal is correctly assigned to the familyTree;

        // creating animals
        Animal animal1 = Animal.startingAnimal(1);
        Animal animal2 = Animal.startingAnimal(1);
        Animal animal3 = Animal.startingAnimal(1);

        // adding animals
        stats.reportAddingStartingAnimal(animal1);
        stats.reportAddingStartingAnimal(animal2);
        stats.reportAddingStartingAnimal(animal3);

        // checking if animals were correctly added to familyTree
        Map<UUID, GraphVertex> familyTree = stats.getFamilyTreeForTests();
        Assertions.assertTrue(familyTree.containsKey(animal1.getId()));
        Assertions.assertTrue(familyTree.containsKey(animal2.getId()));
        Assertions.assertTrue(familyTree.containsKey(animal3.getId()));
    }

    @Test
    public void testAddAnimalHavingParents(){
        // this test only check
        // if animal is correctly assigned to the familyTree and to its parents;

        // creating animals
        Animal parent1 = Animal.startingAnimal(1);
        Animal parent2 = Animal.startingAnimal(1);
        Animal child = Animal.fromParents(parent1, parent2);

        // adding animals
        stats.reportAddingStartingAnimal(parent1);
        stats.reportAddingStartingAnimal(parent2);
        stats.reportAddingAnimalHavingParents(child, parent1, parent2);

        // checking if child was correctly added to familyTree
        Map<UUID, GraphVertex> familyTree = stats.getFamilyTreeForTests();
        Assertions.assertTrue(familyTree.containsKey(child.getId()));

        // checking if child was correctly assigned to its parents
        List<GraphVertex> childrenList1 = familyTree.get(parent1.getId()).getChildren();
        List<GraphVertex> childrenList2 = familyTree.get(parent2.getId()).getChildren();
        GraphVertex childVertex = familyTree.get(child.getId());
        Assertions.assertTrue(childrenList1.contains(childVertex));
        Assertions.assertTrue(childrenList2.contains(childVertex));
    }

    @Test
    public void TestDescendantsList(){
        // creating animals and adding them to stats
        Animal animal1 = Animal.startingAnimal(1);
        Animal animal2 = Animal.startingAnimal(1);
        Animal animal3 = Animal.startingAnimal(1);
        Animal animal4 = Animal.fromParents(animal1, animal2);
        stats.reportAddingStartingAnimal(animal1);
        stats.reportAddingStartingAnimal(animal2);
        stats.reportAddingStartingAnimal(animal3);
        stats.reportAddingAnimalHavingParents(animal4, animal1, animal2);

        /*
         familyTree diagram currently:
         [1] - -             - - [2]    [3]
               |             |
               - - > [4] < - -

         [1], [2] and [3] are roots;
         [1] - animal1
         [2] - animal2
         etc.
        */

        // marking animal1 and getting its descendants and the family tree
        stats.addMark(animal1);
        HashSet<UUID> descendants1 = stats.getDescendantsForTests();

        // checking if correct animals' Ids are in the HashSet of descendants of animal1
        // and incorrect are not;
        Assertions.assertFalse(descendants1.contains(animal1.getId()));
        Assertions.assertFalse(descendants1.contains(animal2.getId()));
        Assertions.assertFalse(descendants1.contains(animal3.getId()));
        Assertions.assertTrue(descendants1.contains(animal4.getId()));

        // checking if the size of the HashSet of descendants is correct
        Assertions.assertEquals(1, stats.getNumberOfDescendants());

        // creating animals and adding them to stats
        Animal animal5 = Animal.fromParents(animal2, animal3);
        Animal animal6 = Animal.fromParents(animal2, animal4);
        Animal animal7 = Animal.fromParents(animal5, animal6);
        Animal animal8 = Animal.fromParents(animal1, animal3);
        stats.reportAddingAnimalHavingParents(animal5, animal2, animal3);
        stats.reportAddingAnimalHavingParents(animal6, animal2, animal4);
        stats.reportAddingAnimalHavingParents(animal7, animal5, animal6);
        stats.reportAddingAnimalHavingParents(animal8, animal1, animal3);

        /*
         familyTree diagram after adding new animals:
         [1] - -             - - - [2] - -            [3] - -
          |    |             |      |    |             |    |
          |    - - > [4] < - -      |    | - > [5] < - -    |
          |           |             |           |           |
          |           - - > [6] < - -           |           |
          |                  |                  |           |
          |                  - - >  [7]  < - - -            |
          |                                                 |
          - - - - - - - - >  [8]  < - - - - - - - - - - - - -

         [1], [2] and [3] are roots;
         [1] - animal1
         [2] - animal2
         etc.
        */

        // getting descendants of animal1 and the family tree
        HashSet<UUID> descendants2 = stats.getDescendantsForTests();

        // checking if correct animals' Ids are in the HashSet of descendants of animal1
        // and incorrect are not;
        Assertions.assertFalse(descendants2.contains(animal1.getId()));
        Assertions.assertFalse(descendants2.contains(animal2.getId()));
        Assertions.assertFalse(descendants2.contains(animal3.getId()));
        Assertions.assertTrue(descendants2.contains(animal4.getId()));
        Assertions.assertFalse(descendants2.contains(animal5.getId()));
        Assertions.assertTrue(descendants2.contains(animal6.getId()));
        Assertions.assertTrue(descendants2.contains(animal7.getId()));
        Assertions.assertTrue(descendants2.contains(animal8.getId()));

        // checking if the size of the HashSet of descendants is correct
        Assertions.assertEquals(4, stats.getNumberOfDescendants());

        // deleting mark
        stats.deleteMark();

        // checking if the HashSet of descendants was cleared
        HashSet<UUID> descendants3 = stats.getDescendantsForTests();
        Assertions.assertTrue(descendants3.isEmpty());

        // marking animal3 and getting its descendants and the family tree
        stats.addMark(animal3);
        HashSet<UUID> descendants4 = stats.getDescendantsForTests();

        // checking if correct animals' Ids are in the HashSet of descendants of animal3
        // and incorrect are not;
        Assertions.assertFalse(descendants4.contains(animal1.getId()));
        Assertions.assertFalse(descendants4.contains(animal2.getId()));
        Assertions.assertFalse(descendants4.contains(animal3.getId()));
        Assertions.assertFalse(descendants4.contains(animal4.getId()));
        Assertions.assertTrue(descendants4.contains(animal5.getId()));
        Assertions.assertFalse(descendants4.contains(animal6.getId()));
        Assertions.assertTrue(descendants4.contains(animal7.getId()));
        Assertions.assertTrue(descendants4.contains(animal8.getId()));

        // checking if the size of the HashSet of descendants is correct
        Assertions.assertEquals(3, stats.getNumberOfDescendants());
    }

    @Test
    public void testGenesStats(){
        // create animal1 with given moveList1
        Animal animal1 = Animal.startingAnimal(1);
        int[] moveList1 = {3,3,3,5,4,0};
        animal1.getGenome().setMoveListForTests(moveList1);
        stats.reportAddingStartingAnimal(animal1);

        // create animal2 with given moveList2
        Animal animal2 = Animal.startingAnimal(1);
        int[] moveList2 = {2,2,1,7,0,1};
        animal2.getGenome().setMoveListForTests(moveList2);
        stats.reportAddingStartingAnimal(animal2);

        /*
          genotype  |  number of each genotype  |  animals having this genotype
         ___________|___________________________|_______________________________
             0      |             2             |        animal1, animal2
             1      |             2             |            animal2
             2      |             2             |            animal2
             3      |             3             |            animal1
             4      |             1             |            animal1
             5      |             1             |            animal1
             6      |             0             |               -
             7      |             1             |            animal2
        */

        // check if the number of each genotype is correct
        int[] numberOfEachGenotype1 = stats.getNumberOfEachGenotype();
        int[] correctNumberOfEachGenotype1 = {2,2,2,3,1,1,0,1};
        Assertions.assertArrayEquals(numberOfEachGenotype1, correctNumberOfEachGenotype1);

        // check if the animals having the most popular genotype(s) are correctly chosen
        HashSet<Animal> popularGenotypeAnimals1 = stats.getPopularGenotypeAnimals();
        HashSet<Animal> correctPopularGenotypeAnimals1 = new HashSet<>();
        correctPopularGenotypeAnimals1.add(animal1);
        Assertions.assertEquals(popularGenotypeAnimals1, correctPopularGenotypeAnimals1);


        // create animal3 with given moveList3;
        // genes are in fact not inherited from parents for the test purpose;
        Animal animal3 = Animal.fromParents(animal1, animal2);
        int[] moveList3 = {2,2,3,4,7,7};
        animal3.getGenome().setMoveListForTests(moveList3);
        stats.reportAddingAnimalHavingParents(animal3, animal1, animal2);

        /*
          genotype  |  number of each genotype  |  animals having this genotype
         ___________|___________________________|_______________________________
             0      |             2             |        animal1, animal2
             1      |             2             |            animal2
             2      |             4             |        animal2, animal3
             3      |             4             |        animal1, animal3
             4      |             2             |        animal1, animal3
             5      |             1             |            animal1
             6      |             0             |               -
             7      |             3             |        animal2, animal3
        */

        // check if the number of each genotype is correct
        int[] numberOfEachGenotype2 = stats.getNumberOfEachGenotype();
        int[] correctNumberOfEachGenotype2 = {2,2,4,4,2,1,0,3};
        Assertions.assertArrayEquals(numberOfEachGenotype2, correctNumberOfEachGenotype2);

        // check if the animals having the most popular genotype(s) are correctly chosen
        HashSet<Animal> popularGenotypeAnimals2 = stats.getPopularGenotypeAnimals();
        HashSet<Animal> correctPopularGenotypeAnimals2 = new HashSet<>();
        correctPopularGenotypeAnimals2.add(animal1);
        correctPopularGenotypeAnimals2.add(animal2);
        correctPopularGenotypeAnimals2.add(animal3);
        Assertions.assertEquals(popularGenotypeAnimals2, correctPopularGenotypeAnimals2);

        // create animal4 with given moveList4;
        // genes are in fact not inherited from parents for the test purpose;
        Animal animal4 = Animal.fromParents(animal1, animal2);
        int[] moveList4 = {5,5,5,0,7,7};
        animal4.getGenome().setMoveListForTests(moveList4);
        stats.reportAddingAnimalHavingParents(animal4, animal1, animal2);

        /*
          genotype  |  number of each genotype  |  animals having this genotype
         ___________|___________________________|_______________________________
             0      |             3             |   animal1, animal2, animal4
             1      |             2             |            animal2
             2      |             4             |        animal2, animal3
             3      |             4             |        animal1, animal3
             4      |             2             |        animal1, animal3
             5      |             4             |        animal1, animal4
             6      |             0             |               -
             7      |             5             |   animal2, animal3, animal4
        */

        // check if the number of each genotype is correct
        int[] numberOfEachGenotype3 = stats.getNumberOfEachGenotype();
        int[] correctNumberOfEachGenotype3 = {3,2,4,4,2,4,0,5};
        Assertions.assertArrayEquals(numberOfEachGenotype3, correctNumberOfEachGenotype3);

        // check if the animals having the most popular genotype(s) are correctly chosen
        HashSet<Animal> popularGenotypeAnimals3 = stats.getPopularGenotypeAnimals();
        HashSet<Animal> correctPopularGenotypeAnimals3 = new HashSet<>();
        correctPopularGenotypeAnimals3.add(animal2);
        correctPopularGenotypeAnimals3.add(animal3);
        correctPopularGenotypeAnimals3.add(animal4);
        Assertions.assertEquals(popularGenotypeAnimals3, correctPopularGenotypeAnimals3);

        // remove animal3
        stats.reportDeathOfAnimal(animal3);

        /*
          genotype  |  number of each genotype  |  animals having this genotype
         ___________|___________________________|_______________________________
             0      |             3             |   animal1, animal2, animal4
             1      |             2             |            animal2
             2      |             2             |            animal2
             3      |             3             |            animal1
             4      |             1             |            animal1
             5      |             4             |        animal1, animal4
             6      |             0             |               -
             7      |             3             |        animal2, animal4
        */

        // check if the number of each genotype is correct
        int[] numberOfEachGenotype4 = stats.getNumberOfEachGenotype();
        int[] correctNumberOfEachGenotype4 = {3,2,2,3,1,4,0,3};
        Assertions.assertArrayEquals(numberOfEachGenotype4, correctNumberOfEachGenotype4);

        // check if the animals having the most popular genotype(s) are correctly chosen
        HashSet<Animal> popularGenotypeAnimals4 = stats.getPopularGenotypeAnimals();
        HashSet<Animal> correctPopularGenotypeAnimals4 = new HashSet<>();
        correctPopularGenotypeAnimals4.add(animal1);
        correctPopularGenotypeAnimals4.add(animal4);
        Assertions.assertEquals(popularGenotypeAnimals4, correctPopularGenotypeAnimals4);
    }

    @Test
    public void testTimeStats(){
        // create default map
        WorldMap map = new NormalMap(1);

        // day 0;
        // adding animal1 and animal2;
        Animal animal1 = Animal.startingAnimal(1);
        stats.reportAddingStartingAnimal(animal1);
        Animal animal2 = Animal.startingAnimal(1);
        stats.reportAddingStartingAnimal(animal2);
        stats.reportEndOfTheDay(map);

        // day 1;
        // adding animal3;
        Animal animal3 = Animal.fromParents(animal1, animal2);
        stats.reportAddingAnimalHavingParents(animal3, animal1, animal2);
        stats.reportEndOfTheDay(map);

        // no one has died yet, so it should be a default value, which is -1
        Assertions.assertEquals(-1, stats.getAverageDaysOfLiving());

        // day 2;
        // adding animal4;
        // mark animal2;
        Animal animal4 = Animal.fromParents(animal1, animal2);
        stats.reportAddingAnimalHavingParents(animal4, animal1, animal2);
        stats.addMark(animal2);
        stats.reportEndOfTheDay(map);

        // day 3;
        // remove animal1
        stats.reportDeathOfAnimal(animal1);
        stats.reportEndOfTheDay(map);

        // day 4;
        // remove animal4
        stats.reportDeathOfAnimal(animal4);
        stats.reportEndOfTheDay(map);

        /*
            animal  |  day of birth  |  day of death  |  days of living
          __________|________________|________________|_________________
           animal1  |        0       |       3        |        3
           animal2  |        0       |       -        |        4
           animal3  |        1       |       -        |        3
           animal4  |        2       |       4        |        2
        */
        // checking days of living for marked animal (animal2)
        Assertions.assertEquals(4, stats.getDaysOfLiving());
        // checking average days of living for dead animals
        Assertions.assertEquals(2.5, stats.getAverageDaysOfLiving());

        // day 5;
        // adding animal5;
        Animal animal5 = Animal.fromParents(animal1, animal2);
        stats.reportAddingAnimalHavingParents(animal5, animal2, animal3);
        stats.reportEndOfTheDay(map);

        // day 6;
        // mark animal3;
        stats.addMark(animal3);
        stats.reportEndOfTheDay(map);

        // day 7;
        // remove animal2
        // remove animal3
        stats.reportDeathOfAnimal(animal2);
        stats.reportDeathOfAnimal(animal3);
        stats.reportEndOfTheDay(map);

        /*
            animal  |  day of birth  |  day of death  |  days of living
          __________|________________|________________|_________________
           animal1  |        0       |       3        |        3
           animal2  |        0       |       7        |        7
           animal3  |        1       |       7        |        6
           animal4  |        2       |       4        |        2
           animal5  |        5       |       -        |        2
        */

        // checking the day of death for marked animal (animal3)
        Assertions.assertEquals(7, stats.getDayOfDeath());
        // checking average days of living for dead animals
        Assertions.assertEquals(4.5, stats.getAverageDaysOfLiving());
    }

    @Test
    public void testEmptyFields(){
        /*
         create map;
         map's lowerLeft boundary is (0,0)
         and upperRight Boundary is (10,10);
         so there are 121 fields in this map;
        */
        WorldMap map = new NormalMap(1);

        // setting plants on: (10,10), (30,20), (30,40), (60,60) positions
        HashSet<Vector2d> plantPositions = new HashSet<>();
        plantPositions.add(new Vector2d(1, 1));
        plantPositions.add(new Vector2d(3, 2));
        plantPositions.add(new Vector2d(3, 4));
        plantPositions.add(new Vector2d(6, 6));
        map.setPlantPositionsForTests(plantPositions);

        // setting 2 animals on: (30,20) position;
        // setting 1 animal each on: (20, 20), (50, 50) positions;
        HashMap<Vector2d, List<Animal>> animalPositions = new HashMap<>();
        Animal animal1 = Animal.startingAnimal(1);
        animal1.setPosition(new Vector2d(3, 2));
        Animal animal2 = Animal.startingAnimal(1);
        animal2.setPosition(new Vector2d(3, 2));
        animalPositions.put(new Vector2d(3, 2), Arrays.asList(animal1, animal2));
        Animal animal3 = Animal.startingAnimal(1);
        animal3.setPosition(new Vector2d(2, 2));
        animalPositions.put(new Vector2d(2, 2), List.of(animal3));
        Animal animal4 = Animal.startingAnimal(1);
        animal4.setPosition(new Vector2d(5, 5));
        animalPositions.put(new Vector2d(5, 5), List.of(animal4));
        map.setAnimalPositionsForTests(animalPositions);

        // taking statistics as the day ends
        stats.reportEndOfTheDay(map);
        int numberOfEmptyFields = stats.getNumberOfEmptyFields();

        /*
         There are:
         121 fields;
         4 fields containing plants;
         3 fields containing animals;
         1 filed containing both plant and animal(s);
         So the number of empty fields (based on inclusion - exclusion principle)
         should be 121 - 4 - 3 + 1 = 115;
        */

        // checking if the number of empty fields is correct
        Assertions.assertEquals(115, numberOfEmptyFields);
    }

    @Test
    public void testAverageEnergyStatistics() {
        /*
         Constants:
         energy required for reproduction - 25;
         energy used for reproduction - 10;
         new animal energy - 15;
         daily energy loss - 4;
         energy from plant - 21;
        */

        // day 0;
        // initialize map;

        // create map
        WorldMap map = new NormalMap(1);

        // add plants to map
        HashSet<Vector2d> plantPositions = new HashSet<>();
        plantPositions.add(new Vector2d(10, 10));
        plantPositions.add(new Vector2d(8, 10));
        map.setPlantPositionsForTests(plantPositions);

        // add animals to map
        HashMap<Vector2d, List<Animal>> animalPositions = new HashMap<>();

        // add animal1
        Animal animal1 = Animal.startingAnimal(1);
        Genome genome1 = Genome.startingAnimalGenome(1);
        genome1.setMoveListForTests(new int[]{0, 0, 0, 0, 0, 0});
        animal1.setGenesForTests(genome1);
        animal1.setOrientationForTests(MapDirection.WEST);
        animal1.setPosition(new Vector2d(10, 10));
        animalPositions.put(new Vector2d(10, 10),List.of(animal1));

        // add animal2
        Animal animal2 = Animal.startingAnimal(1);
        Genome genome2 = Genome.startingAnimalGenome(1);
        genome2.setMoveListForTests(new int[]{0, 0, 0, 0, 0, 0});
        animal2.setGenesForTests(genome2);
        animal2.setOrientationForTests(MapDirection.EAST);
        animal2.setPosition(new Vector2d(8, 10));
        animalPositions.put(new Vector2d(8, 10),List.of(animal2));

        // add animal3
        Animal animal3 = Animal.startingAnimal(1);
        animal3.setPosition(new Vector2d(3, 3));
        animalPositions.put(new Vector2d(3, 3), List.of(animal3));

        map.setAnimalPositionsForTests(animalPositions);

        /*
            animal  |  current energy
          __________|_________________
           animal1  |        15
           animal2  |        15
           animal3  |        15
        */

        // check animals' current energy
        Assertions.assertEquals(15, animal1.getCurrentEnergy());
        Assertions.assertEquals(15, animal2.getCurrentEnergy());
        Assertions.assertEquals(15, animal3.getCurrentEnergy());

        // check average energy for living animals
        Assertions.assertEquals(15, stats.getAverageEnergy());

        /*
         day 1;
         subtract animals' energy, kill dead animals (if they exist) and feed them;
         other actions are skipped;
         animal1 and animal2 will get energy from plants;
        */
        map.reduceAnimalEnergy();
        map.removeDeadAnimals();
        map.feedAnimals();
        stats.reportEndOfTheDay(map);

        /*
           animal   | energy before | daily lost | from plants | energy now
          __________|_______________|____________|_____________|____________
           animal1  |       15      |     -4     |     +21     |     32
           animal2  |       15      |     -4     |     +21     |     32
           animal3  |       15      |     -4     |      -      |     11
        */

        // check animals' current energy
        Assertions.assertEquals(32, animal1.getCurrentEnergy());
        Assertions.assertEquals(32, animal2.getCurrentEnergy());
        Assertions.assertEquals(11, animal3.getCurrentEnergy());

        // check average energy for living animals
        Assertions.assertEquals(25, stats.getAverageEnergy());

        /*
         day 2;
         subtract animals' energy, kill dead animals (if they exist), move them and reproduce them;
         other actions are skipped;
         animal1 and animal2 will be parents of new animal4;
        */
        map.reduceAnimalEnergy();
        map.removeDeadAnimals();
        map.moveAnimals();
        map.reproduceAnimals();
        stats.reportEndOfTheDay(map);

        List<Animal> animalList = map.getAnimalPositions().get(new Vector2d(9, 10));
        Animal animal4 = animalList.get(animalList.size() - 1);

        /*
           animal   | energy before | daily lost | reproduction | energy now
          __________|_______________|____________|______________|____________
           animal1  |       32      |     -4     |     -10      |      18
           animal2  |       32      |     -4     |     -10      |      18
           animal3  |       11      |     -4     |      -       |      7
           animal4  |       -       |      -     |     +20      |      20
        */

        // check animals' average current energy
        Assertions.assertEquals(18, animal1.getCurrentEnergy());
        Assertions.assertEquals(18, animal2.getCurrentEnergy());
        Assertions.assertEquals(7, animal3.getCurrentEnergy());
        Assertions.assertEquals(20, animal4.getCurrentEnergy());

        // check average energy for living animals
        Assertions.assertEquals(15.75, stats.getAverageEnergy());

        /*
         day 3 and 4;
         subtract animals' energy every day, kill dead animals every day (if they exist);
         other actions are skipped;
         animal4 will die on the 4th day;
        */
        map.reduceAnimalEnergy();
        map.removeDeadAnimals();
        stats.reportEndOfTheDay(map);
        map.reduceAnimalEnergy();
        map.removeDeadAnimals();
        stats.reportEndOfTheDay(map);

        /*
           animal   | energy before | 2 times daily lost | energy now
          __________|_______________|____________________|____________
           animal1  |       18      |         -8         |      10
           animal2  |       18      |         -8         |      10
           animal3  |       7       |         -7         |      -
           animal4  |       20      |         -8         |      12
        */

        // check animals' average current energy
        Assertions.assertEquals(10, animal1.getCurrentEnergy());
        Assertions.assertEquals(10, animal2.getCurrentEnergy());
        Assertions.assertEquals(12, animal4.getCurrentEnergy());

        // check average energy for living animals to the precision of two digits
        Assertions.assertTrue((stats.getAverageEnergy() - 10.66 > 0) &&
                (stats.getAverageEnergy() - 10.67 < 0));

        /*
         day 5, 6 and 7;
         subtract animals' energy every day, kill dead animals every day (if they exist);
         other actions are skipped;
         all animals will die on the 7th day;
        */
        map.reduceAnimalEnergy();
        map.removeDeadAnimals();
        stats.reportEndOfTheDay(map);
        map.reduceAnimalEnergy();
        map.removeDeadAnimals();
        stats.reportEndOfTheDay(map);
        map.reduceAnimalEnergy();
        map.removeDeadAnimals();
        stats.reportEndOfTheDay(map);

        /*
           animal   | energy before | 3 times daily lost | energy now
          __________|_______________|____________________|____________
           animal1  |       10      |        -10         |      -
           animal2  |       10      |        -10         |      -
           animal4  |       12       |       -12         |      -
        */

        // check default value, which is -1, if there are no live animals
        Assertions.assertEquals(-1, stats.getAverageEnergy());
    }

    @Test
    public void averageNumberOfChildren() {
        // create map
        WorldMap map = new NormalMap(1);

        // add animals
        HashMap<Vector2d, List<Animal>> animalPositions = new HashMap<>();

        // animal1 and animal2 are on the same position
        // and have proper energy for more than one reproduction;
        Animal animal1 = Animal.startingAnimal(1);
        animal1.setCurrentEnergyForTests(100);
        animal1.setPosition(new Vector2d(1,1));
        Animal animal2 = Animal.startingAnimal(1);
        animal2.setPosition(new Vector2d(1,1));
        animal2.setCurrentEnergyForTests(100);
        animalPositions.put(animal1.getPosition(), new ArrayList<>(List.of(animal1, animal2)));


        // add animal 3;
        Animal animal3 = Animal.startingAnimal(1);
        animal3.setPosition(new Vector2d(5,5));
        animalPositions.put(animal3.getPosition(), new ArrayList<>(List.of(animal3)));

        map.setAnimalPositionsForTests(animalPositions);

        /*
           animal   |  number of children
          __________|_____________________
           animal1  |          0
           animal2  |          0
           animal3  |          0
        */

        // check average children number for living animals
        Assertions.assertEquals(0, stats.getAverageChildrenNumber());

        // reproduce animals;
        // animal1 and animal2 will be parents;
        map.reproduceAnimals();

        // get animal4 and set its energy to 100, so it can have a child
        List<Animal> animalList = map.getAnimalPositions().get(new Vector2d(1,1));
        Animal animal4 = animalList.get(animalList.size() - 1);
        animal4.setCurrentEnergyForTests(100);

        /*
           animal   |  number of children
          __________|_____________________
           animal1  |          1
           animal2  |          1
           animal3  |          0
           animal4  |          0
        */

        // check if animals have correct number of children
        Assertions.assertEquals(animal1.getChildrenNumber(), 1);
        Assertions.assertEquals(animal2.getChildrenNumber(), 1);
        Assertions.assertEquals(animal3.getChildrenNumber(), 0);
        Assertions.assertEquals(animal4.getChildrenNumber(), 0);

        // check average children number for living animals
        Assertions.assertEquals(0.5, stats.getAverageChildrenNumber());

        // set genomes and orientations for animal1 and animal4,
        // so that they will be on the same position after the next move;
        animal1.getGenome().setMoveListForTests(new int[]{0, 0, 0, 0, 0, 0});
        animal1.setOrientationForTests(MapDirection.NORTH);
        animal2.getGenome().setMoveListForTests(new int[]{0, 0, 0, 0, 0, 0});
        animal2.setOrientationForTests(MapDirection.EAST);
        animal4.getGenome().setMoveListForTests(new int[]{0, 0, 0, 0, 0, 0});
        animal4.setOrientationForTests(MapDirection.NORTH);

        // move and reproduce animals;
        // animal1 and animal4 will be parents;
        map.moveAnimals();
        map.reproduceAnimals();

        // get animal5
        List<Animal> animalList2 = map.getAnimalPositions().get(new Vector2d(1,2));
        Animal animal5 = animalList2.get(animalList2.size() - 1);

        /*
           animal   |  number of children
          __________|_____________________
           animal1  |          2
           animal2  |          1
           animal3  |          0
           animal4  |          1
           animal5  |          0
        */

        // check if animals have correct number of children
        Assertions.assertEquals(animal1.getChildrenNumber(), 2);
        Assertions.assertEquals(animal2.getChildrenNumber(), 1);
        Assertions.assertEquals(animal3.getChildrenNumber(), 0);
        Assertions.assertEquals(animal4.getChildrenNumber(), 1);
        Assertions.assertEquals(animal5.getChildrenNumber(), 0);

        // check average children number for living animals
        Assertions.assertEquals(0.8, stats.getAverageChildrenNumber());

        // remove animal2
        stats.reportDeathOfAnimal(animal2);

        /*
           animal   |  number of children
          __________|_____________________
           animal1  |          2
           animal3  |          0
           animal4  |          1
           animal5  |          0
        */

        // check if animals have correct number of children
        Assertions.assertEquals(animal1.getChildrenNumber(), 2);
        Assertions.assertEquals(animal3.getChildrenNumber(), 0);
        Assertions.assertEquals(animal4.getChildrenNumber(), 1);
        Assertions.assertEquals(animal5.getChildrenNumber(), 0);

        // check average children number for living animals
        Assertions.assertEquals(0.75, stats.getAverageChildrenNumber());

        // remove animal1, animal3, animal4 and animal5
        stats.reportDeathOfAnimal(animal1);
        stats.reportDeathOfAnimal(animal3);
        stats.reportDeathOfAnimal(animal4);
        stats.reportDeathOfAnimal(animal5);

        // check default value, which is -1, if there are no live animals
        Assertions.assertEquals(-1, stats.getAverageChildrenNumber());
    }
}