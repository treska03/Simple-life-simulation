package agh.ics.oop.model;

import agh.ics.oop.model.util.RandomPositionGenerator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.lang.Math.sqrt;

public class GrassField extends AbstractWorldMap{

    private final Map<Vector2d, Grass> grassPositions = new HashMap<>();
    private final int grassNumber;
    private final Vector2d lowerLeftGrass;
    private final Vector2d upperRightGrass;

    public GrassField(int n) {
        super();
        if(n<0) {throw new IllegalArgumentException();}
        this.grassNumber = n;
        this.lowerLeftGrass = new Vector2d(0, 0);
        this.upperRightGrass = new Vector2d((int) sqrt(n*10), (int) sqrt(n*10));
        initGrass();
    }

    private void initGrass() {
        RandomPositionGenerator randomPositionGenerator = new RandomPositionGenerator(lowerLeftGrass, upperRightGrass, grassNumber);
        for (Vector2d grassPosition : randomPositionGenerator) {
            grassPositions.put(grassPosition, new Grass(grassPosition));
        }
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return super.isOccupied(position) || grassPositions.get(position) != null;
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        return super.objectAt(position) != null ? super.objectAt(position) : grassPositions.get(position);
    }

    @Override
    public List<WorldElement> getElements() {
        return Stream.concat(
                super.getElements().stream(),
                (grassPositions.values()).stream())
                .toList();
    }

    @Override
    public Boundary getCurrentBounds() {
        List<Vector2d> positionsList = getElements().stream()
                .map(WorldElement::getPosition)
                .toList();
        return Boundary.fromList(positionsList);
    }
}
