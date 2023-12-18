package agh.ics.oop.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.*;

public class GenesTest {
    @Test
    public void GetMove_test() {
        Genes genes = new Genes();
        int[] moveList = {0,1,7,6,2,3,5,4};
        genes.startMoveNumber = 6;
        genes.moveList = moveList;
        genes.NUMBER_OF_GENES = moveList.length;

        genes.BACK_AND_FORTH = false;
        Assertions.assertEquals(genes.getMove(), 5);
        Assertions.assertEquals(genes.getMove(), 4);
        Assertions.assertEquals(genes.getMove(), 0);

        genes.BACK_AND_FORTH = true;
        genes.moveNumber = 0;
        Assertions.assertEquals(genes.getMove(), 5);
        Assertions.assertEquals(genes.getMove(), 4);
        Assertions.assertEquals(genes.getMove(), 4);
    }

    @Test
    public void fromParents() {
        // creating parent1
        Genes genes1 = new Genes();
        int[] moveList1 = {0,1,2,3,0,1,2,3};
        genes1.moveList = moveList1;
        genes1.NUMBER_OF_GENES = moveList1.length;
        Animal parent1 = new Animal();
        parent1.genes = genes1;
        parent1.currentEnergy = 100;

        // creating parent2
        Genes genes2 = new Genes();
        int[] moveList2 = {4,5,6,7,4,5,6,7};
        genes2.moveList = moveList2;
        genes2.NUMBER_OF_GENES = moveList2.length;
        Animal parent2 = new Animal();
        parent2.genes = genes2;
        parent2.currentEnergy = 200;

        Constances.setNumberOfGens(moveList2.length);
        // if there is no mutations in the list of genes of child can be made only in 2 ways
        // result1 and result2 in this example
        int[] result1 = {0,1,6,7,4,5,6,7};
        int[] result2 = {4,5,6,7,4,5,2,3};

        // creating child1
        Genes genes3 = new Genes();
        genes3.MIN_MUTATIONS = 0;
        genes3.MAX_MUTATIONS = 0;
        genes3.NUMBER_OF_GENES = moveList2.length;
        Animal child1 = new Animal();
        child1.genes = genes3;
        child1.genes.fromParents(parent1, parent2);

        // checking if the list of genes of child is either equal (by values) to result1 or result2
        int[] receivedList = child1.genes.moveList;
        boolean check1 = Arrays.equals(receivedList, result1);
        boolean check2 = Arrays.equals(receivedList, result2);
        Assertions.assertTrue(check1 || check2);

        // creating child2
        Genes genes4 = new Genes();
        genes4.MIN_MUTATIONS = 2;
        genes4.MAX_MUTATIONS = 3;
        genes4.NUMBER_OF_GENES = moveList2.length;
        Animal child2 = new Animal();
        child2.genes = genes4;
        child2.genes.fromParents(parent1, parent2);

        // checking if the numbers of inherited genes from parents is correct
        int numberOfInherited2 = getNumberOfInherited(child2, result1, result2);
        Assertions.assertTrue(numberOfInherited2 >= 5);

        // creating child3
        Genes genes5 = new Genes();
        genes5.MIN_MUTATIONS = 1;
        genes5.MAX_MUTATIONS = 1;
        genes5.NUMBER_OF_GENES = moveList2.length;
        Animal child3 = new Animal();
        child3.genes = genes5;
        child3.genes.fromParents(parent1, parent2);

        // checking if the numbers of inherited genes from parents is correct
        int numberOfInherited3 = getNumberOfInherited(child3, result1, result2);
        Assertions.assertTrue(numberOfInherited3 >= 7);
    }

    private int getNumberOfInherited(Animal child2, int[] result1, int[] result2) {
        // the list of genes of child can differ only by the maximum of MAX_MUTATIONS
        // from the result where, there is no mutations;
        // the result for 2 mutations can be made only in 2 ways
        // result1 and result2 in this example;
        int numberOfInherited = 0;
        int[] receivedList2 = child2.genes.moveList;
        for (int geneIdx = 0; geneIdx < receivedList2.length; geneIdx++) {
            if (receivedList2[geneIdx] == result1[geneIdx] || receivedList2[geneIdx] == result2[geneIdx]) {
                numberOfInherited +=1;
            }
        }
        return numberOfInherited;
    }
}
