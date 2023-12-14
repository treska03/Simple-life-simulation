package agh.ics.oop;
import agh.ics.oop.model.*;
import javafx.application.Application;

import java.util.ArrayList;
import java.util.List;

public class World {
    public static void main(String[] args) throws InterruptedException {

        System.out.println("System wystartował");

        Application.launch(SimulationApp.class, args);
//
//
//        List<MoveDirection> directions = OptionParser.Parse(args);
//        List<Vector2d> positions = List.of(new Vector2d(2,2), new Vector2d(3,4));
//
//        ConsoleMapDisplay mapDisplay = new ConsoleMapDisplay();
//        List<Simulation> simulations = new ArrayList<>();
//
//        for(int i=0; i<10000; i++) {
//            RectangularMap gameMap = new RectangularMap(10, 10);
//            gameMap.addObserver(mapDisplay);
//            Simulation simulation = new Simulation(gameMap, positions, directions);
//            simulations.add(simulation);
//        }
//
//        SimulationEngine engine = new SimulationEngine(simulations);
//
//
//        engine.runAsync();


        System.out.println("System zakończył działanie");

    }
}
