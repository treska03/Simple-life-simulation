package agh.ics.oop.model;

import agh.ics.oop.model.info.Constants;
import agh.ics.oop.model.info.ConstantsList;
import agh.ics.oop.model.util.FisherYatesShuffle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Genes{
    private final int simulationId;
    private final Constants constants;
    private final int NUMBER_OF_GENES;
    private final boolean BACK_AND_FORTH;
    private final int MIN_MUTATIONS;
    private final int MAX_MUTATIONS;
    private int[] moveList;
    private int startMoveNumber;
    private int moveNumber = 0; //number of already made moves

    public Genes(int simulationId) {
        this.simulationId = simulationId;
        this.constants = ConstantsList.getConstants(simulationId);
        this.NUMBER_OF_GENES = constants.getNumberOfGenes();
        this.BACK_AND_FORTH = constants.isBackAndForth();
        this.MIN_MUTATIONS = constants.getMinMutations();
        this.MAX_MUTATIONS = constants.getMaxMutations();
        this.moveList = new int[NUMBER_OF_GENES];
    }

    public int getCurrentMove() {
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

    public static Genes fromParents(Animal animal1, Animal animal2) {
//        Main constructor, for gene.
//        Used after initial creation of first animals in simulation
//
//        Args: 2 Animals
//
//        Returns: Gene

        Genes newGene = new Genes(animal1.getGenes().getSimulationId());

        Animal dominant;
        Animal minor;
        if (animal1.getCurrentEnergy() > animal2.getCurrentEnergy()){
            dominant = animal1;
            minor = animal2;
        }
        else {
            dominant = animal2;
            minor = animal1;
        }

        // divide the number of genes to inherit
        // division not equal to integer number of genes is for the benefit of dominant
        double proportion = (double) newGene.NUMBER_OF_GENES / (animal1.getCurrentEnergy() + animal2.getCurrentEnergy());
        int numberOfGensFromMinor = (int)(minor.getCurrentEnergy() * proportion);
        int numberOfGensFromDominant = newGene.NUMBER_OF_GENES - numberOfGensFromMinor;

        // choosing side of genes for dominant
        Random random = new Random();
        int side = random.nextInt(2); // side = 0 left side for dominant

        // copied genes from parents to child
        if (side == 0){
            for (int i = 0; i < numberOfGensFromDominant; i++){
                newGene.moveList[i] = dominant.getGenes().moveList[i];
            }
            for (int i = numberOfGensFromDominant; i < newGene.NUMBER_OF_GENES; i++){
                newGene.moveList[i] = minor.getGenes().moveList[i];
            }
        }
        else {
            for (int i = 0; i < numberOfGensFromMinor; i++){
                newGene.moveList[i] = minor.getGenes().moveList[i];
            }
            for (int i = numberOfGensFromMinor; i < newGene.NUMBER_OF_GENES; i++){
                newGene.moveList[i] = dominant.getGenes().moveList[i];
            }
        }

        // mutating genes
        newGene.mutateGene();

        // choosing randomly starting gene
        newGene.setStartMoveNumber();

        return newGene;
    }


//    What is this used for???
    public void startingAnimal() {
        // choosing randomly genes
        Random random = new Random();
        for (int i = 0; i < NUMBER_OF_GENES; i++){
            moveList[i] = random.nextInt(8);
        }

        // choosing randomly starting gene
        setStartMoveNumber();
    }

    private void mutateGene() {
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

    public int getSimulationId() {
        return simulationId;
    }

    public int[] getMoveList() {
        return moveList;
    }

    public void setMoveList(int[] moveList) { // only for tests
        this.moveList = moveList;
    }

    public void setStartMoveNumber(int startMoveNumber) { // only for tests
        this.startMoveNumber = startMoveNumber;
    }
}
