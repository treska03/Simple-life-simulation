package agh.ics.oop.model.algorithms;

import agh.ics.oop.model.algorithms.DFS;
import agh.ics.oop.model.util.GraphVertex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.UUID;

public class DFSTest {

    @Test
    public void testGetDescendantsList(){
        /*
         Graph diagram:
             [1]       [2] - -         - - - [3]    [4] - -
              |         |    |         |      |      |    |
              |         |    - > [5] < -      |      |    |
              v         v         |           v      |    |
         - - [6]  < -  [7]  < - - -      - - [8] < - -    |
         |    |         |                |    |           |
         |    v         v                |    |           |
         |   [9]  < -  [10]  < - - - - - -    |           |
         |    |         |                     |           |
         |    v         v                     v           |
         - > [11]      [12]  < - - - - - - - [13] < - - - -

         [1], [2], [3] and [4] are roots;
         [1] - first
         [2] - second
         [3] - third
         etc.
        */

        // creating graph vertices
        GraphVertex first = new GraphVertex(UUID.randomUUID());
        GraphVertex second = new GraphVertex(UUID.randomUUID());
        GraphVertex third = new GraphVertex(UUID.randomUUID());
        GraphVertex fourth = new GraphVertex(UUID.randomUUID());
        GraphVertex fifth = new GraphVertex(UUID.randomUUID());
        GraphVertex sixth = new GraphVertex(UUID.randomUUID());
        GraphVertex seventh = new GraphVertex(UUID.randomUUID());
        GraphVertex eighth = new GraphVertex(UUID.randomUUID());
        GraphVertex ninth = new GraphVertex(UUID.randomUUID());
        GraphVertex tenth = new GraphVertex(UUID.randomUUID());
        GraphVertex eleventh = new GraphVertex(UUID.randomUUID());
        GraphVertex twelfth = new GraphVertex(UUID.randomUUID());
        GraphVertex thirteenth = new GraphVertex(UUID.randomUUID());

        // creating graph directed edges
        first.addChild(sixth);
        second.addChild(fifth);
        second.addChild(seventh);
        third.addChild(fifth);
        third.addChild(eighth);
        fourth.addChild(eighth);
        fourth.addChild(thirteenth);
        fifth.addChild(seventh);
        sixth.addChild(ninth);
        sixth.addChild(eleventh);
        seventh.addChild(sixth);
        seventh.addChild(tenth);
        eighth.addChild(tenth);
        eighth.addChild(thirteenth);
        ninth.addChild(eleventh);
        tenth.addChild(ninth);
        tenth.addChild(twelfth);
        thirteenth.addChild(twelfth);

        // creating DFS object
        DFS dfs2 = new DFS();

        // creating list of descendants for second
        List<GraphVertex> descendantsList2 = dfs2.getDescendantsList(second);

        // checking if correct vertices are in list of descendants of second
        // and incorrect are not;
        Assertions.assertFalse(descendantsList2.contains(first));
        Assertions.assertFalse(descendantsList2.contains(second));
        Assertions.assertFalse(descendantsList2.contains(third));
        Assertions.assertFalse(descendantsList2.contains(fourth));
        Assertions.assertTrue(descendantsList2.contains(fifth));
        Assertions.assertTrue(descendantsList2.contains(sixth));
        Assertions.assertTrue(descendantsList2.contains(seventh));
        Assertions.assertFalse(descendantsList2.contains(eighth));
        Assertions.assertTrue(descendantsList2.contains(ninth));
        Assertions.assertTrue(descendantsList2.contains(tenth));
        Assertions.assertTrue(descendantsList2.contains(eleventh));
        Assertions.assertTrue(descendantsList2.contains(twelfth));
        Assertions.assertFalse(descendantsList2.contains(thirteenth));

        // checking if the size of the list of descendants is correct
        Assertions.assertEquals(7, descendantsList2.size());

        // creating list of descendants for twelfth
        DFS dfs12 = new DFS();
        List<GraphVertex> descendantsList12 = dfs12.getDescendantsList(twelfth);

        // checking if the list of descendants is empty
        Assertions.assertTrue(descendantsList12.isEmpty());
    }
}
