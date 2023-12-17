package agh.ics.oop;

import agh.ics.oop.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SimulationEngineTest {

    @Test
    public void runSync() {
        Vector2d start1 = new Vector2d(0,0);
        Vector2d start2 = new Vector2d(2,2);
        Vector2d start3 = new Vector2d(2,3);
        Vector2d start4 = new Vector2d(4,0);
        List<Vector2d> startList = List.of(start1, start2, start3, start4);

        String[] moveList1 = {"f", "f", "r", "l", "b", "b", "b", "f", "f"};
        String[] moveList2 = {"f"};
        String[] moveList3 = {"f", "f", "b", "r", "r", "f", "b", "b", "l", "f", "f", "r", "r", "r", "b", "b", "b"};
        String[] moveList4 = {};
        String[] moveList5 = {"f", "r", "r", "b", "b", "b"};
        List<Vector2d> finishPosition1 = List.of(new Vector2d(0, 1), new Vector2d(2, 1), new Vector2d(1, 3), new Vector2d(3, 0));
        List<Vector2d> finishPosition2 = List.of(new Vector2d(0, 1), new Vector2d(2, 2), new Vector2d(2, 3), new Vector2d(4, 0));
        List<Vector2d> finishPosition3 = List.of(new Vector2d(0, 1), new Vector2d(2, 2), new Vector2d(2, 3), new Vector2d(3, 1));
        List<Vector2d> finishPosition4 = startList;
        List<Vector2d> finishPosition5 = List.of(new Vector2d(0, 0), new Vector2d(1, 2), new Vector2d(2, 3), new Vector2d(4, 0));

        RectangularMap worldMap1 = new RectangularMap(4, 4);
        RectangularMap worldMap2 = new RectangularMap(4, 4);
        RectangularMap worldMap3 = new RectangularMap(4, 4);
        RectangularMap worldMap4 = new RectangularMap(4, 4);
        RectangularMap worldMap5 = new RectangularMap(4, 4);

        ConsoleMapDisplay display = new ConsoleMapDisplay();
        worldMap1.addObserver(display);
        worldMap2.addObserver(display);
        worldMap3.addObserver(display);
        worldMap4.addObserver(display);
        worldMap5.addObserver(display);

        Simulation simulation1 = new Simulation(worldMap1, startList, OptionParser.Parse(moveList1));
        Simulation simulation2 = new Simulation(worldMap2, startList, OptionParser.Parse(moveList2));
        Simulation simulation3 = new Simulation(worldMap3, startList, OptionParser.Parse(moveList3));
        Simulation simulation4 = new Simulation(worldMap4, startList, OptionParser.Parse(moveList4));
        Simulation simulation5 = new Simulation(worldMap5, startList, OptionParser.Parse(moveList5));

        List<MapDirection> endDirection1 = List.of(MapDirection.NORTH, MapDirection.NORTH, MapDirection.EAST, MapDirection.WEST);
        List<MapDirection> endDirection2 = List.of(MapDirection.NORTH, MapDirection.NORTH, MapDirection.NORTH, MapDirection.NORTH);
        List<MapDirection> endDirection3 = List.of(MapDirection.EAST, MapDirection.EAST, MapDirection.NORTH, MapDirection.SOUTH);
        List<MapDirection> endDirection4 = List.of(MapDirection.NORTH, MapDirection.NORTH, MapDirection.NORTH, MapDirection.NORTH);
        List<MapDirection> endDirection5 = List.of(MapDirection.NORTH, MapDirection.EAST, MapDirection.EAST, MapDirection.NORTH);



        List<Simulation> simulations = List.of(simulation1, simulation2, simulation3, simulation4, simulation5);
        SimulationEngine engine = new SimulationEngine(simulations);
        engine.runSync();


        // testing if animals finish in correct spots
        for(int i=0; i<4; i++) {
            Assertions.assertEquals(finishPosition1.get(i), simulation1.getAnimalList().get(i).getPosition());
            Assertions.assertEquals(finishPosition2.get(i), simulation2.getAnimalList().get(i).getPosition());
            Assertions.assertEquals(finishPosition3.get(i), simulation3.getAnimalList().get(i).getPosition());
            Assertions.assertEquals(finishPosition4.get(i), simulation4.getAnimalList().get(i).getPosition());
            Assertions.assertEquals(finishPosition5.get(i), simulation5.getAnimalList().get(i).getPosition());
        }

        // testing if animal doesn't go outside boundary
        Boundary bounds = new Boundary(new Vector2d(0, 0), new Vector2d(4, 4));
        Assertions.assertTrue(simulation1.getAnimalList().stream().allMatch(animal -> bounds.insideBoundary(animal.getPosition())));
        Assertions.assertTrue(simulation2.getAnimalList().stream().allMatch(animal -> bounds.insideBoundary(animal.getPosition())));
        Assertions.assertTrue(simulation3.getAnimalList().stream().allMatch(animal -> bounds.insideBoundary(animal.getPosition())));
        Assertions.assertTrue(simulation4.getAnimalList().stream().allMatch(animal -> bounds.insideBoundary(animal.getPosition())));
        Assertions.assertTrue(simulation5.getAnimalList().stream().allMatch(animal -> bounds.insideBoundary(animal.getPosition())));


        // testing if animals finish with correct orientations
        for(int i=0; i<4; i++) {
            Assertions.assertEquals(endDirection1.get(i), simulation1.getAnimalList().get(i).getOrientation());
            Assertions.assertEquals(endDirection2.get(i), simulation2.getAnimalList().get(i).getOrientation());
            Assertions.assertEquals(endDirection3.get(i), simulation3.getAnimalList().get(i).getOrientation());
            Assertions.assertEquals(endDirection4.get(i), simulation4.getAnimalList().get(i).getOrientation());
            Assertions.assertEquals(endDirection5.get(i), simulation5.getAnimalList().get(i).getOrientation());
        }
    }

}