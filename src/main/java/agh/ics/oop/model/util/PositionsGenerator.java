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

    public List<Vector2d> generateAllPositions() {
        return generateAllPositions(startX, startY, finishX, finishY);
    }
    public static List<Vector2d> generateAllPositions(Boundary bounds) {
        int startX = bounds.lowerLeft().getX();
        int startY = bounds.lowerLeft().getY();
        int finishX = bounds.upperRight().getX();
        int finishY = bounds.upperRight().getY();
        return generateAllPositions(startX, startY, finishX, finishY);
    }

    public static List<Vector2d> generateAllPositions(int startX, int startY, int finishX, int finishY) {
        List<Vector2d> allPositions = new ArrayList<>();
        for(int x=startX; x<=finishX; x++) {
            for(int y=startY; y<=finishY; y++) {
                allPositions.add(new Vector2d(x, y));
            }
        }
        return allPositions;
    }

    public static Vector2d generateRandomPosition(Boundary boundary) {
        Vector2d size2d = boundary.upperRight().subtract(boundary.lowerLeft());
        int widthDelta = RandomNumberGenerator.getRandomInRange(size2d.getX());
        int heightDelta = RandomNumberGenerator.getRandomInRange(size2d.getY());

        return boundary.lowerLeft().add(new Vector2d(widthDelta, heightDelta));
    }

    public static List<Vector2d> generateStepsPositionList(Boundary mapBoundary, Boundary jgBoundary) {
        List<Vector2d> positionList = generateAllPositions(mapBoundary);
        positionList.removeIf(jgBoundary::insideBoundary);
        return positionList;
    }


    public Iterator<Vector2d> iterator() {
        return nPositions.iterator();
    }
   }
