package agh.ics.oop.model.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.UUID;

public class GraphVertexTest {
    @Test
    public void children_test(){
        // creating graph vertices
        GraphVertex parent = new GraphVertex(UUID.randomUUID());
        GraphVertex child1 = new GraphVertex(UUID.randomUUID());
        GraphVertex child2 = new GraphVertex(UUID.randomUUID());
        GraphVertex child3 = new GraphVertex(UUID.randomUUID());
        GraphVertex anotherVertex = new GraphVertex(UUID.randomUUID());

        // adding children to parent
        parent.addChild(child1);
        parent.addChild(child2);
        parent.addChild(child3);
        List<GraphVertex> childrenList = parent.getChildren();

        // checking if children are assigned to parent
        // and others are not;
        Assertions.assertTrue(childrenList.contains(child1));
        Assertions.assertTrue(childrenList.contains(child2));
        Assertions.assertTrue(childrenList.contains(child3));
        Assertions.assertFalse(childrenList.contains(anotherVertex));
        Assertions.assertFalse(childrenList.contains(parent));

        // checking the number of children of parent
        Assertions.assertEquals(3, childrenList.size());
    }
}
