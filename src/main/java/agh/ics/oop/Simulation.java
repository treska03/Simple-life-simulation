package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.util.PositionAlreadyOccupiedException;

import java.util.ArrayList;
import java.util.List;

public class Simulation implements Runnable{

    private final WorldMap<WorldElement, Vector2d> gameMap;
    private final List<MoveDirection> moveList;
    private final List<Animal> animalList = new ArrayList<>();

    public Simulation(WorldMap<WorldElement, Vector2d> map, List<Vector2d> startPositions, List<MoveDirection> moveList) {
        this.gameMap = map;
        this.moveList = moveList;
        initAnimals(gameMap, startPositions);
    }


    private void initAnimals(WorldMap<WorldElement, Vector2d> map, List<Vector2d> startPositions) {
        for(Vector2d position : startPositions) {
            Animal newAnimal = new Animal(position);
            try {
                map.place(newAnimal);
                animalList.add(newAnimal);
            }
            catch(PositionAlreadyOccupiedException ignored) {}
        }
    }

    @Override
    public void run() {
        int i = 0;

        for(MoveDirection movement : moveList) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Animal currentAnimal = animalList.get(i);
            try {gameMap.move(currentAnimal, movement);}
            catch (PositionAlreadyOccupiedException ignore) {};
            i = (i+1) % (animalList.size());
        }
    }

    public List<Animal> getAnimalList() {
        return animalList;
    }
}
