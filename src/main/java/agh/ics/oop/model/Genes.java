package agh.ics.oop.model;

import agh.ics.oop.model.util.FisherYatesShuffle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Genes extends FisherYatesShuffle{
    int NUMBER_OF_GENES = Constances.getNumberOfGens();
    int[] moveList = new int[NUMBER_OF_GENES];
    int startMoveNumber;
    int moveNumber = 0; //number of already made moves
    boolean BACK_AND_FORTH = Constances.isBackAndForth();
    int MIN_MUTATIONS = Constances.getMinMutations();
    int MAX_MUTATIONS = Constances.getMaxMutations();


    public int getMove() {
        int currGeneIdx;

        if (!BACK_AND_FORTH){
            currGeneIdx = (startMoveNumber + moveNumber) % NUMBER_OF_GENES;
        }
        else {
            int order = ((startMoveNumber + moveNumber) / NUMBER_OF_GENES) % 2; // order = 0 from left to right
            int fromEdge = (startMoveNumber + moveNumber) % NUMBER_OF_GENES;
            if (order == 0){
                currGeneIdx = fromEdge;
            }
            else {
                currGeneIdx = NUMBER_OF_GENES - 1 - fromEdge;
            }
        }
        moveNumber += 1;
        return moveList[currGeneIdx];
    }

    public void fromParents(Animal animal1, Animal animal2) {
        // checking, which animal is dominant and which animal is minor
        Animal dominant;
        Animal minor;
        if (animal1.currentEnergy > animal2.currentEnergy){
            dominant = animal1;
            minor = animal2;
        }
        else {
            dominant = animal2;
            minor = animal1;
        }

        // divide the number of genes to inherit
        // division not equal to integer number of genes is for the benefit of dominant
        double proportion = (double) NUMBER_OF_GENES / (animal1.currentEnergy + animal2.currentEnergy);
        int numberOfGensFromMinor = (int)(minor.currentEnergy * proportion);
        int numberOfGensFromDominant = NUMBER_OF_GENES - numberOfGensFromMinor;

        // choosing side of genes for dominant
        Random random = new Random();
        int side = random.nextInt(2); // side = 0 left side for dominant

        // copied genes from parents to child
        if (side == 0){
            for (int i = 0; i < numberOfGensFromDominant; i++){
                moveList[i] = dominant.genes.moveList[i];
            }
            for (int i = numberOfGensFromDominant; i < NUMBER_OF_GENES; i++){
                moveList[i] = minor.genes.moveList[i];
            }
        }
        else {
            for (int i = 0; i < numberOfGensFromMinor; i++){
                moveList[i] = minor.genes.moveList[i];
            }
            for (int i = numberOfGensFromMinor; i < NUMBER_OF_GENES; i++){
                moveList[i] = dominant.genes.moveList[i];
            }
        }

        // mutating genes
        mutateGene(moveList);

        // choosing randomly starting gene
        setStartMoveNumber();
    }

    public void startingAnimal() {
        // choosing randomly genes
        Random random = new Random();
        for (int i = 0; i < NUMBER_OF_GENES; i++){
            moveList[i] = random.nextInt(8);
        }

        // choosing randomly starting gene
        setStartMoveNumber();
    }

    private void mutateGene(int[] moveList) {
        Random random = new Random();

        // choosing randomly number of mutations
        int numberOfMutatingGens = random.nextInt(MAX_MUTATIONS - MIN_MUTATIONS + 1) + MIN_MUTATIONS;

        // choosing randomly indexes to modify
        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_GENES; i++){
            indexes.add(i);
        }
        FisherYatesShuffle fisherYatesShuffle = new FisherYatesShuffle();
        List<Integer> indexesToMutate = fisherYatesShuffle.getValues(numberOfMutatingGens, indexes);

        // choosing random gens in mutations
        for (int idx : indexesToMutate) {
            moveList[idx] = random.nextInt(8);
        }
    }

    private void setStartMoveNumber() {
        Random random = new Random();
        this.startMoveNumber = random.nextInt(8);
    }
}
