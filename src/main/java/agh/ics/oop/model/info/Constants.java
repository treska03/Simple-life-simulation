package agh.ics.oop.model.info;

import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.util.Boundary;

import java.util.HashMap;

public class Constants {
    private final int NUMBER_OF_TICKS; // time of simulation
    private final boolean BACK_AND_FORTH;
    private final int NUMBER_OF_GENES;
    private final int MIN_MUTATIONS;
    private final int MAX_MUTATIONS;
    private final int DAILY_NEW_GRASS_NUMBER;
    private final int ENERGY_FROM_PLANT;
    private final Boundary MAP_BOUNDARY;
    private final Boundary JUNGLE_BOUNDARY;


    public Constants(int NUMBER_OF_TICKS, boolean BACK_AND_FORTH, int NUMBER_OF_GENS, int MIN_MUTATIONS,
                     int MAX_MUTATIONS, int DAILY_NEW_GRASS_NUMBER, int ENERGY_FROM_PLANT, Boundary MAP_BOUNDARY) {
        this.NUMBER_OF_TICKS = NUMBER_OF_TICKS;
        this.BACK_AND_FORTH = BACK_AND_FORTH;
        this.NUMBER_OF_GENES = NUMBER_OF_GENS;
        this.MIN_MUTATIONS = MIN_MUTATIONS;
        this.MAX_MUTATIONS = MAX_MUTATIONS;
        this.DAILY_NEW_GRASS_NUMBER = DAILY_NEW_GRASS_NUMBER;
        this.ENERGY_FROM_PLANT = ENERGY_FROM_PLANT;
        this.MAP_BOUNDARY = MAP_BOUNDARY;
        this.JUNGLE_BOUNDARY = createJungleBoundary(MAP_BOUNDARY);
    }

    private static Boundary createJungleBoundary(Boundary mapBoundary) {
        private static Boundary createJungleBoundary(Boundary mapBoundary) {
        int tenthHeight = (int) ((mapBoundary.upperRight().getY() - mapBoundary.lowerLeft().getY())/10);
        
        Vector2d jungleLowerLeft = mapBoundary.lowerLeft().add(new Vector2d(0, 4*tenthHeight));
        Vector2d jungleUpperRight = mapBoundary.upperRight().subtract(new Vector2d(0, 4*tenthHeight));
        
        return new Boundary(jungleLowerLeft, jungleUpperRight);
    }
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
    public int getDailyNewGrassNumber() {
        return DAILY_NEW_GRASS_NUMBER;
    }

    public int getEnergyFromPlant() {
        return ENERGY_FROM_PLANT;
    }

    public Boundary getMapBoundary() {
        return MAP_BOUNDARY;
    }

    public Boundary getJungleBoundary() {
        return JUNGLE_BOUNDARY;
    }
}
