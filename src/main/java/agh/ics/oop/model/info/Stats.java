package agh.ics.oop.model.info;

public class Stats {
    private final int NUMBER_OF_TICKS; // time of simulation

    public Stats(int NUMBER_OF_TICKS) {
        this.NUMBER_OF_TICKS = NUMBER_OF_TICKS;
    }

    public int getNumberOfTicks() {
        return NUMBER_OF_TICKS;
    }

}
