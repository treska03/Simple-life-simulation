package agh.ics.oop.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.*;

public class GenesTest {
    @Test
    public void GetMove_test() {
        Constants.setNumberOfGens(8);
        int[] moveList = {0,1,7,6,2,3,5,4};

        Constants.setBackAndForth(false);
        Genes genes1 = new Genes();
        genes1.setStartMoveNumber(6);
        genes1.setMoveList(moveList);

        Assertions.assertEquals(genes1.getMove(), 5);
        Assertions.assertEquals(genes1.getMove(), 4);
        Assertions.assertEquals(genes1.getMove(), 0);

        Constants.setBackAndForth(true);
        Genes genes2 = new Genes();
        genes2.setStartMoveNumber(6);
        genes2.setMoveList(moveList);
        Assertions.assertEquals(genes2.getMove(), 5);
        Assertions.assertEquals(genes2.getMove(), 4);
        Assertions.assertEquals(genes2.getMove(), 4);
    }

    @Test
    public void fromParents() {
        Constants.setNumberOfGens(8);

        // creating parent1
        Genes genes1 = new Genes();
        int[] moveList1 = {0,1,2,3,0,1,2,3};
        genes1.setMoveList(moveList1);
        Animal parent1 = new Animal();
        parent1.setGenes(genes1);
        parent1.setCurrentEnergy(100);

        // creating parent2
        Genes genes2 = new Genes();
        int[] moveList2 = {4,5,6,7,4,5,6,7};
        genes2.setMoveList(moveList2);
        Animal parent2 = new Animal();
        parent2.setGenes(genes2);
        parent2.setCurrentEnergy(200);

        Constants.setNumberOfGens(moveList2.length);
        // if there is no mutations in the list of genes of child can be made only in 2 ways
        // result1 and result2 in this example
        int[] result1 = {0,1,6,7,4,5,6,7};
        int[] result2 = {4,5,6,7,4,5,2,3};

        // creating child1
        Constants.setMinMutations(0);
        Constants.setMaxMutations(0);
        Genes genes3 = new Genes();
        Animal child1 = new Animal();
        child1.setGenes(genes3);
        child1.getGenes().fromParents(parent1, parent2);

        // checking if the list of genes of child is either equal (by values) to result1 or result2
        int[] receivedList = child1.getGenes().getMoveList();
        boolean check1 = Arrays.equals(receivedList, result1);
        boolean check2 = Arrays.equals(receivedList, result2);
        Assertions.assertTrue(check1 || check2);

        // creating child2
        Constants.setMinMutations(2);
        Constants.setMaxMutations(3);
        Genes genes4 = new Genes();
        Animal child2 = new Animal();
        child2.setGenes(genes4);
        child2.getGenes().fromParents(parent1, parent2);

        // checking if the numbers of inherited genes from parents is correct
        int numberOfInherited2 = getNumberOfInherited(child2, result1, result2);
        Assertions.assertTrue(numberOfInherited2 >= 5);

        // creating child3
        Constants.setMinMutations(1);
        Constants.setMaxMutations(1);
        Genes genes5 = new Genes();
        Animal child3 = new Animal();
        child3.setGenes(genes5);
        child3.getGenes().fromParents(parent1, parent2);

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
        int[] receivedList2 = child2.getGenes().getMoveList();
        for (int geneIdx = 0; geneIdx < receivedList2.length; geneIdx++) {
            if (receivedList2[geneIdx] == result1[geneIdx] || receivedList2[geneIdx] == result2[geneIdx]) {
                numberOfInherited +=1;
            }
        }
        return numberOfInherited;
    }
}
