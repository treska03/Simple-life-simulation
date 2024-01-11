package agh.ics.oop;

import agh.ics.oop.model.info.Constants;
import agh.ics.oop.model.info.ConstantsList;
import agh.ics.oop.model.info.Stats;
import agh.ics.oop.model.info.StatsList;
import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.Vector2d;
import javafx.application.Application;

public class World {
    public static void main(String[] args) throws InterruptedException {

        System.out.println("System wystartował");

        //Application.launch(SimulationApp.class, args);

        for (int i = 1; i<= 1; i++){ // end condition only temporarily

            setUpConstants(i);
            Stats stats = new Stats(i);
            StatsList.addToStatsList(i, stats);
            Simulation simulation = new Simulation(1);
            simulation.run();
        }

        System.out.println("System zakończył działanie");

    }

    private static void setUpConstants(int simulationId) {
        boolean BACK_AND_FORTH = false;
        int NUMBER_OF_GENS = 10;
        int MIN_MUTATIONS = 0;
        int MAX_MUTATIONS = 3;
        int MIN_ENERGY_FOR_REPRODUCTION = 30;
        int ENERGY_USED_FOR_REPRODUCTION = 25;
        int STARTING_ANIMALS_NUMBER = 15;
        int NEW_ANIMAL_ENERGY = 15;
        int DAILY_NEW_GRASS_NUMBER = 25;
        int DAILY_ENERGY_LOSS = 1;
        int ENERGY_FROM_PLANT = 20;
        Boundary MAP_BOUNDARY = new Boundary(new Vector2d(0, 0), new Vector2d(10, 10));

        Constants mockConsts = new Constants(BACK_AND_FORTH, NUMBER_OF_GENS, MIN_MUTATIONS,
                MAX_MUTATIONS, MIN_ENERGY_FOR_REPRODUCTION, ENERGY_USED_FOR_REPRODUCTION,
                STARTING_ANIMALS_NUMBER, NEW_ANIMAL_ENERGY, DAILY_NEW_GRASS_NUMBER,
                DAILY_ENERGY_LOSS, ENERGY_FROM_PLANT, MAP_BOUNDARY);

        ConstantsList.addToConstantsList(simulationId, mockConsts);
    }
}
