package agh.ics.oop.model.info;

import agh.ics.oop.model.ConstantSetterForTests;
import agh.ics.oop.model.creatures.Animal;
import agh.ics.oop.model.util.GraphVertex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class StatsTest {
    private Stats stats;

    @BeforeEach
    void setUp() {
        // creating stats and default constants
        this.stats = new Stats(0); // default number of ticks
        StatsList.addToStatsList(1, stats);
        ConstantSetterForTests constantSetter = new ConstantSetterForTests();
        constantSetter.setUpConstants(1);
    }
    
    @Test
    public void addStartingAnimaTest(){
        // creating animals
        Animal animal1 = Animal.startingAnimal(1);
        Animal animal2 = Animal.startingAnimal(1);
        Animal animal3 = Animal.startingAnimal(1);

        // adding animals to familyTree
        stats.addStartingAnimal(animal1);
        stats.addStartingAnimal(animal2);
        stats.addStartingAnimal(animal3);

        // checking if animals were correctly added to familyTree
        Map<UUID, GraphVertex> familyTree = stats.getFamilyTreeForTests();
        Assertions.assertTrue(familyTree.containsKey(animal1.getId()));
        Assertions.assertTrue(familyTree.containsKey(animal2.getId()));
        Assertions.assertTrue(familyTree.containsKey(animal3.getId()));
    }

    @Test
    public void addAnimalHavingParents(){
        //this test doesn't check the list of descendants
        //another test is designed for this purpose;

        // creating animals
        Animal parent1 = Animal.startingAnimal(1);
        Animal parent2 = Animal.startingAnimal(1);
        Animal child = Animal.fromParents(parent1, parent2);

        // adding animals to familyTree
        stats.addStartingAnimal(parent1);
        stats.addStartingAnimal(parent2);
        stats.addAnimalHavingParents(child, parent1, parent2);

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
        stats.addStartingAnimal(animal1);
        stats.addStartingAnimal(animal2);
        stats.addStartingAnimal(animal3);
        stats.addAnimalHavingParents(animal4, animal1, animal2);

        /*
         familyTree diagram currently:
         [1] - -             - - [2]    [3]
               |             |
               - - > [4] < - -

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
        stats.addAnimalHavingParents(animal5, animal2, animal3);
        stats.addAnimalHavingParents(animal6, animal2, animal4);
        stats.addAnimalHavingParents(animal7, animal5, animal6);
        stats.addAnimalHavingParents(animal8, animal1, animal3);

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
}
