package agh.ics.oop.model.util;

import agh.ics.oop.model.Vector2d;

import java.util.*;

public class PositionsGenerator implements Iterable<Vector2d> {

    private final int startX;
    private final int startY;
    private final int finishX;
    private final int finishY;
    private List<Vector2d> nPositions;

    public PositionsGenerator(Vector2d lowerLeft, Vector2d upperRight) {
        this.startX = lowerLeft.getX();
        this.startY = lowerLeft.getY();
        this.finishX = upperRight.getX();
        this.finishY = upperRight.getY();
        this.nPositions = generateAllPositions();
    }

    public void ChooseRandomPositions(int numToGenerate){
        Collections.shuffle(nPositions);
        nPositions = generateAllPositions().subList(0, numToGenerate);
    }

    private List<Vector2d> generateAllPositions() {
        List<Vector2d> allPositions = new ArrayList<>();
        for(int x=startX; x<=finishX; x++) {
            for(int y=startY; y<=finishY; y++) {
                allPositions.add(new Vector2d(x, y));
            }
        }
        return allPositions;
    }


    public Iterator<Vector2d> iterator() {
        return nPositions.iterator();
    }
   }
