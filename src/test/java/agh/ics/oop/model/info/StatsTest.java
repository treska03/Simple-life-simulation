package agh.ics.oop.model.info;

import agh.ics.oop.model.ConstantSetterForTests;
import agh.ics.oop.model.creatures.Animal;
import agh.ics.oop.model.util.GraphVertex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

public class StatsTest {
    private Stats stats;

    @BeforeEach
    void setUp() {
        // creating stats and default constants
        this.stats = new Stats(0); // default number of ticks
        StatsList.addToStatsList(1, stats);
        ConstantSetterForTests constantSetter = new ConstantSetterForTests();
        constantSetter.setNUMBER_OF_GENS(6);
        constantSetter.setUpConstants(1);
    }

    @Test
    public void addStartingAnimaTest(){
        // this test doesn't check genes related statistics;
        // another test is designed for this purpose;

        // creating animals
        Animal animal1 = Animal.startingAnimal(1);
        Animal animal2 = Animal.startingAnimal(1);
        Animal animal3 = Animal.startingAnimal(1);

        // adding animals to familyTree
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
    public void addAnimalHavingParents(){
        /*
         this test doesn't check the list of descendants
         and genes related statistics;
         another tests are designed for this purpose;
        */

        // creating animals
        Animal parent1 = Animal.startingAnimal(1);
        Animal parent2 = Animal.startingAnimal(1);
        Animal child = Animal.fromParents(parent1, parent2);

        // adding animals to familyTree
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
    public void descendantsListTest(){
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
        Map<UUID, GraphVertex> familyTree1 = stats.getFamilyTreeForTests();

        // checking if correct animals' Ids are in the HashSet of descendants of animal1
        // and incorrect are not;
        Assertions.assertFalse(descendants1.contains(animal1.getId()));
        Assertions.assertFalse(descendants1.contains(animal2.getId()));
        Assertions.assertFalse(descendants1.contains(animal3.getId()));
        Assertions.assertTrue(descendants1.contains(animal4.getId()));

        // checking if the size of the HashSet of descendants is correct
        Assertions.assertEquals(1, descendants1.size());

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
        Map<UUID, GraphVertex> familyTree2 = stats.getFamilyTreeForTests();

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
        Assertions.assertEquals(4, descendants2.size());

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
        Assertions.assertEquals(3, descendants4.size());
    }

    @Test
    public void genesStats_test(){
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
         __________|___________________________|______________________________
             0     |             2             |        animal1, animal2
             1     |             2             |             animal2
             2     |             2             |             animal2
             3     |             3             |             animal1
             4     |             1             |             animal1
             5     |             1             |             animal1
             6     |             0             |                -
             7     |             1             |             animal2
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


        // create animal3 with given moveList3
        Animal animal3 = Animal.startingAnimal(1);
        int[] moveList3 = {2,2,3,4,7,7};
        animal3.getGenome().setMoveListForTests(moveList3);
        stats.reportAddingStartingAnimal(animal3);

        /*
         genotype  |  number of each genotype  |  animals having this genotype
         __________|___________________________|______________________________
             0     |             2             |        animal1, animal2
             1     |             2             |             animal2
             2     |             4             |        animal2, animal3
             3     |             4             |        animal1, animal3
             4     |             2             |        animal1, animal3
             5     |             1             |             animal1
             6     |             0             |                -
             7     |             3             |        animal2, animal3
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

        // create animal4 with given moveList4
        Animal animal4 = Animal.startingAnimal(1);
        int[] moveList4 = {5,5,5,0,7,7};
        animal4.getGenome().setMoveListForTests(moveList4);
        stats.reportAddingStartingAnimal(animal4);

        /*
         genotype  |  number of each genotype  |  animals having this genotype
         __________|___________________________|______________________________
             0     |             3             |   animal1, animal2, animal4
             1     |             2             |             animal2
             2     |             4             |        animal2, animal3
             3     |             4             |        animal1, animal3
             4     |             2             |        animal1, animal3
             5     |             4             |        animal1, animal4
             6     |             0             |                -
             7     |             5             |   animal2, animal3, animal4
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
         __________|___________________________|______________________________
             0     |             3             |   animal1, animal2, animal4
             1     |             2             |             animal2
             2     |             2             |             animal2
             3     |             3             |             animal1
             4     |             1             |             animal1
             5     |             4             |        animal1, animal4
             6     |             0             |                -
             7     |             3             |        animal2, animal4
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
}
