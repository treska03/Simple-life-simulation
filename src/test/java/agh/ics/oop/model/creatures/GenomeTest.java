package agh.ics.oop.model.creatures;

import agh.ics.oop.model.ConstantSetterForTests;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.*;

public class GenomeTest {
    @Test
    public void getCurrentMove_test() {

        // creating default list of genes
        int[] moveList = {0,1,7,6,2,3,5,4};

        // test for BACK_AND_FORTH = false
        // set constants
        ConstantSetterForTests constantSetter1 = new ConstantSetterForTests();
        constantSetter1.setNUMBER_OF_GENS(8);
        constantSetter1.setBACK_AND_FORTH(false);
        constantSetter1.setUpConstants(1);

        // creating genome
        Genome genome1 = Genome.startingAnimalGenome(1);
        genome1.setStartMoveNumberForTests(6);
        genome1.setMoveListForTests(moveList);

        // tests
        Assertions.assertEquals(genome1.getCurrentMove(), 5);
        Assertions.assertEquals(genome1.getCurrentMove(), 4);
        Assertions.assertEquals(genome1.getCurrentMove(), 0);

        // test for BACK_AND_FORTH = true
        // set constants
        ConstantSetterForTests constantSetter2 = new ConstantSetterForTests();
        constantSetter2.setNUMBER_OF_GENS(8);
        constantSetter2.setBACK_AND_FORTH(true);
        constantSetter2.setUpConstants(2);

        // creating genome
        Genome genome2 = Genome.startingAnimalGenome(2);
        genome2.setStartMoveNumberForTests(6);
        genome2.setMoveListForTests(moveList);

        // tests
        Assertions.assertEquals(genome2.getCurrentMove(), 5);
        Assertions.assertEquals(genome2.getCurrentMove(), 4);
        Assertions.assertEquals(genome2.getCurrentMove(), 4);
    }

    @Test
    public void fromParents_test() {
        /*
         if there is no mutations in the list of genes can be made only in 2 ways
         result1 and result2 in this example
         those are the combinations of list of genes from parents;
        */
        int[] result1 = {0, 1, 6, 7, 4, 5, 6, 7};
        int[] result2 = {4, 5, 6, 7, 4, 5, 2, 3};

        // declaring variables that will be later used
        Animal parent1;
        Animal parent2;

        // creating genes3 and set constants
        ConstantSetterForTests constantSetter1 = new ConstantSetterForTests();
        constantSetter1.setNUMBER_OF_GENS(8);
        constantSetter1.setMIN_MUTATIONS(0);
        constantSetter1.setMAX_MUTATIONS(0);
        constantSetter1.setUpConstants(1);
        parent1 = setParent(1, 1);
        parent2 = setParent(1, 2);
        Genome genome1 = Genome.fromParents(parent1, parent2);

        // checking if the list of genes is either equal (by values) to result1 or result2
        int[] receivedList = genome1.getMoveList();
        boolean check1 = Arrays.equals(receivedList, result1);
        boolean check2 = Arrays.equals(receivedList, result2);
        Assertions.assertTrue(check1 || check2);

        // creating genes4 and set constants
        ConstantSetterForTests constantSetter2 = new ConstantSetterForTests();
        constantSetter2.setNUMBER_OF_GENS(8);
        constantSetter2.setMIN_MUTATIONS(2);
        constantSetter2.setMAX_MUTATIONS(3);
        constantSetter2.setUpConstants(2);
        parent1 = setParent(2,1);
        parent2 = setParent(2,2);
        Genome genome2 = Genome.fromParents(parent1, parent2);

        // checking if the numbers of inherited genes from parents is correct
        int numberOfInherited2 = getNumberOfInherited(genome2, result1, result2);
        Assertions.assertTrue(numberOfInherited2 >= 5);

        // creating genes5 and set constants
        ConstantSetterForTests constantSetter3 = new ConstantSetterForTests();
        constantSetter3.setNUMBER_OF_GENS(8);
        constantSetter3.setMIN_MUTATIONS(1);
        constantSetter3.setMAX_MUTATIONS(1);
        constantSetter3.setUpConstants(3);
        parent1 = setParent(3,1);
        parent2 = setParent(3,2);
        Genome genome3 = Genome.fromParents(parent1, parent2);

        // checking if the numbers of inherited genes from parents is correct
        int numberOfInherited3 = getNumberOfInherited(genome3, result1, result2);
        Assertions.assertTrue(numberOfInherited3 >= 7);
    }

    private Animal setParent (int simulationId, int numberOfParent){
        // creating parent
        Animal parent = Animal.startingAnimal(simulationId);
        Genome genes = Genome.startingAnimalGenome(simulationId);
        int [] moveList;
        if (numberOfParent == 1) {
            moveList = new int[] {0, 1, 2, 3, 0, 1, 2, 3};
            parent.setCurrentEnergyForTests(100);
        }
        else {
            moveList = new int[] {4, 5, 6, 7, 4, 5, 6, 7};
            parent.setCurrentEnergyForTests(200);
        }
        genes.setMoveListForTests(moveList);
        parent.setGenesForTests(genes);
        return parent;
    }

    private int getNumberOfInherited(Genome genes, int[] result1, int[] result2) {
        /*
         the list of genes can differ only by the maximum of MAX_MUTATIONS
         from the result where, there is no mutations;
         the result for 2 mutations can be made only in 2 ways
         result1 and result2 in this example;
        */
        int numberOfInherited = 0;
        int[] receivedList2 = genes.getMoveList();
        for (int geneIdx = 0; geneIdx < receivedList2.length; geneIdx++) {
            if (receivedList2[geneIdx] == result1[geneIdx] || receivedList2[geneIdx] == result2[geneIdx]) {
                numberOfInherited +=1;
            }
        }
        return numberOfInherited;
    }
}
