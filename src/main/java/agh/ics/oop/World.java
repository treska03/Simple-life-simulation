package agh.ics.oop;

import agh.ics.oop.model.info.Stats;
import agh.ics.oop.model.info.StatsList;
import javafx.application.Application;

public class World {
    public static void main(String[] args) throws InterruptedException {

        System.out.println("System wystartował");

        //Application.launch(SimulationApp.class, args);

        for (int i = 1; i<= 1; i++){ // end condition only temporarily

            Stats stats = new Stats(i);
            StatsList.addToStatsList(i, stats);
            Simulation simulation = new Simulation(1);
            simulation.run();
        }

        System.out.println("System zakończył działanie");

    }
}
