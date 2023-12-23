package agh.ics.oop.model;

public class Constants {
    private static int NUMBER_OF_TICKS; // time of simulation
    private static boolean BACK_AND_FORTH;
    private static int NUMBER_OF_GENS;
    private static int MIN_MUTATIONS;
    private static int MAX_MUTATIONS;
    private static Vector2d LOWER_LEFT;
    private static Vector2d UPPER_RIGHT;
    private static int DAILY_NEW_GRASS_NUMBER;


    public static int getNumberOfTicks() {
        return NUMBER_OF_TICKS;
    }

    public static boolean isBackAndForth() {
        return BACK_AND_FORTH;
    }

    public static int getNumberOfGens() {
        return NUMBER_OF_GENS;
    }

    public static int getMinMutations() {
        return MIN_MUTATIONS;
    }

    public static int getMaxMutations() {
        return MAX_MUTATIONS;
    }

    public static Vector2d getLowerLeft() {
        return LOWER_LEFT;
    }

    public static Vector2d getUpperRight() {
        return UPPER_RIGHT;
    }

    public static int getDailyNewGrassNumber() {
        return DAILY_NEW_GRASS_NUMBER;
    }

    public static void setNumberOfTicks(int numberOfMoves) {
        NUMBER_OF_TICKS = numberOfMoves;
    }

    public static void setBackAndForth(boolean backAndForth) {
        BACK_AND_FORTH = backAndForth;
    }

    public static void setNumberOfGens(int numberOfGens) {
        NUMBER_OF_GENS = numberOfGens;
    }

    public static void setMinMutations(int minMutations) {
        MIN_MUTATIONS = minMutations;
    }

    public static void setMaxMutations(int maxMutations) {
        MAX_MUTATIONS = maxMutations;
    }

    public static void setLowerLeft(int x, int y) {
        LOWER_LEFT = new Vector2d(x,y);
    }

    public static void setUpperRight(int x, int y) {
        UPPER_RIGHT = new Vector2d(x,y);
    }

    public static void setDailyNewGrassNumber(int dailyNewGrassNumber) {
        DAILY_NEW_GRASS_NUMBER = dailyNewGrassNumber;
    }
}
