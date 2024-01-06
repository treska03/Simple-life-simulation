package agh.ics.oop;

import javafx.application.Application;

public class World {
    public static void main(String[] args) throws InterruptedException {

        System.out.println("System wystartował");

        Application.launch(SimulationApp.class, args);

        System.out.println("System zakończył działanie");

    }
}
