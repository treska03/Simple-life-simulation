package agh.ics.oop.model;

import agh.ics.oop.model.util.FisherYatesShuffle;
import agh.ics.oop.model.util.RandomPositionGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.lang.Math.min;
import static java.lang.Math.sqrt;

public class GrassField extends AbstractWorldMap{

    public Map<Vector2d, Grass> grassPositions = new HashMap<>();
    public List<Vector2d> barren;
    private final int dailyNewGrassNumber = Constances.DAILY_NEW_GRASS_NUMBER;
    private final Vector2d lowerLeftGrass = Constances.LOWER_LEFT;
    private final Vector2d upperRightGrass = Constances.UPPER_RIGHT;

    public GrassField() {
        super();
        this.barren = this.getAllPositions(); // positions with no grass
        //initGrass2(); // to delete?
        initGrass();
    }

    private void initGrass2() { // to delete?
        RandomPositionGenerator randomPositionGenerator = new RandomPositionGenerator(lowerLeftGrass, upperRightGrass, dailyNewGrassNumber);
        for (Vector2d grassPosition : randomPositionGenerator) {
            grassPositions.put(grassPosition, new Grass(grassPosition));
        }
    }

    public void initGrass() {
        // add new Grass on randomly chosen unique positions to grassPositions
        // and remove those positions from barren;
        // positions are chosen using Fisher-Yates shuffle;
        // it is impossible to add more grass than the empty fields left
        int numberOfAddedGrass = min(dailyNewGrassNumber, barren.size());
        List<Vector2d> newPositions = FisherYatesShuffle.getVector2dValues(numberOfAddedGrass, barren);
        for (Vector2d grassPosition : newPositions) {
            grassPositions.put(grassPosition, new Grass(grassPosition));
        }
        for (int i = 0; i < numberOfAddedGrass; i++) {
            // Fisher-Yates shuffle place chosen positions at the end of the list
            barren.remove(barren.remove(barren.size() - 1));
        }
    }

    public void eaten(Vector2d position){
        grassPositions.remove(position);
        barren.add(position);
    }

    private List<Vector2d> getAllPositions () {
        // make a list from all positions in the map
        List<Vector2d> allPositions = new ArrayList<>();
        for (int x = lowerLeftGrass.getX(); x <= upperRightGrass.getX(); x++) {
            for (int y = lowerLeftGrass.getY(); y <= upperRightGrass.getY(); y++) {
                allPositions.add(new Vector2d(x, y));
            }
        }
        return allPositions;
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
