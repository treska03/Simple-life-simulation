package agh.ics.oop;
import agh.ics.oop.model.*;
import javafx.application.Application;

import java.util.ArrayList;
import java.util.List;

public class World {
    public static void main(String[] args) throws InterruptedException {

        System.out.println("System wystartował");

        Application.launch(SimulationApp.class, args);

        System.out.println("System zakończył działanie");

    }
}
