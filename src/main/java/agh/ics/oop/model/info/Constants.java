package agh.ics.oop.model.info;

import agh.ics.oop.model.Vector2d;

import java.util.HashMap;

public class Constants {
    private final int NUMBER_OF_TICKS; // time of simulation
    private final boolean BACK_AND_FORTH;
    private final int NUMBER_OF_GENES;
    private final int MIN_MUTATIONS;
    private final int MAX_MUTATIONS;
    private final Vector2d LOWER_LEFT;
    private final Vector2d UPPER_RIGHT;
    private final int DAILY_NEW_GRASS_NUMBER;


    public Constants(int NUMBER_OF_TICKS, boolean BACK_AND_FORTH, int NUMBER_OF_GENS, int MIN_MUTATIONS, int MAX_MUTATIONS, Vector2d LOWER_LEFT, Vector2d UPPER_RIGHT, int DAILY_NEW_GRASS_NUMBER) {
        this.NUMBER_OF_TICKS = NUMBER_OF_TICKS;
        this.BACK_AND_FORTH = BACK_AND_FORTH;
        this.NUMBER_OF_GENES = NUMBER_OF_GENS;
        this.MIN_MUTATIONS = MIN_MUTATIONS;
        this.MAX_MUTATIONS = MAX_MUTATIONS;
        this.LOWER_LEFT = LOWER_LEFT;
        this.UPPER_RIGHT = UPPER_RIGHT;
        this.DAILY_NEW_GRASS_NUMBER = DAILY_NEW_GRASS_NUMBER;
    }



    public int getNumberOfTicks() {
        return NUMBER_OF_TICKS;
    }

    public boolean isBackAndForth() {
        return BACK_AND_FORTH;
    }

    public int getNumberOfGenes() {
        return NUMBER_OF_GENES;
    }

    public int getMinMutations() {
        return MIN_MUTATIONS;
    }

    public int getMaxMutations() {
        return MAX_MUTATIONS;
    }

    public Vector2d getLowerLeft() {
        return LOWER_LEFT;
    }

    public Vector2d getUpperRight() {
        return UPPER_RIGHT;
    }

    public int getDailyNewGrassNumber() {
        return DAILY_NEW_GRASS_NUMBER;
    }

}
