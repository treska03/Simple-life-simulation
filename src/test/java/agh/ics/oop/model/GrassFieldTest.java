package agh.ics.oop.model;

import agh.ics.oop.model.util.PositionAlreadyOccupiedException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GrassFieldTest {
    private GrassField map;

    @BeforeEach
    public void setUp() {
        Constances.setLowerLeft(0,0);
        Constances.setUpperRight(9,9);
        Constances.setDailyNewGrassNumber(5);
        map = new GrassField();
    }

    @Test
    public void initGrass_test() {
        // cover whole map with grass
        Constances.setDailyNewGrassNumber(40);
        GrassField map2 = new GrassField();
        map2.initGrass(); // only 20 empty fields left after that
        map2.initGrass();  // it has to add 20 fields, not 40

        // check if grass cover whole map
        for (int x = 0 ; x < 10; x++) {
            for (int y = 0; y <10; y++) {
                Assertions.assertTrue(map2.grassPositions.containsKey(new Vector2d(x,y)));
            }
        }

        // there shouldn't be any empty fields
        Assertions.assertTrue(map2.barren.isEmpty());
    }

    @Test
    public void eaten_test() {
        // cover whole map with grass
        Constances.setDailyNewGrassNumber(40);
        GrassField map2 = new GrassField();
        map2.initGrass(); // only 20 empty fields left after that
        map2.initGrass();  // it has to add 20 fields, not 40

        // assume that eaten position is (1,1)
        Vector2d eatenPosition = new Vector2d(1,1);
        map2.eaten(eatenPosition);

        // there shouldn't be grass on eaten position
        Assertions.assertFalse(map2.grassPositions.containsKey(eatenPosition));
        Assertions.assertEquals(map2.barren.get(0), eatenPosition);
    }

    @Test
    public void shouldValidateMove(){
        Assertions.assertDoesNotThrow(() -> map.place(new Animal(new Vector2d(2, 2))));
        Assertions.assertDoesNotThrow(() -> map.place(new Animal(new Vector2d(1, 2))));
        Assertions.assertDoesNotThrow(() -> map.place(new Animal(new Vector2d(0, 2))));

        Assertions.assertTrue(map.canMoveTo(new Vector2d(1, 3)));
        Assertions.assertFalse(map.canMoveTo(new Vector2d(1, 2)));
        Assertions.assertFalse(map.canMoveTo(new Vector2d(0, 2)));
        Assertions.assertTrue(map.canMoveTo(new Vector2d(9, 9)));
        Assertions.assertTrue(map.canMoveTo(new Vector2d(Integer.MAX_VALUE, Integer.MIN_VALUE)));
    }

    @Test
    public void shouldValidateAnimalPlacement() {
        Animal animal = new Animal();

        Assertions.assertDoesNotThrow(() -> map.place(new Animal(new Vector2d(0, 2))));
        Assertions.assertDoesNotThrow(() -> map.place(new Animal(new Vector2d(2, 10))));

        Assertions.assertDoesNotThrow(() -> map.place(animal));
        Assertions.assertDoesNotThrow(() -> map.place(new Animal(new Vector2d(-1, 3))));
        Assertions.assertDoesNotThrow(() -> map.place(new Animal(new Vector2d(-1, 399))));
        Assertions.assertThrows(PositionAlreadyOccupiedException.class, () -> map.place(animal));
        Assertions.assertThrows(PositionAlreadyOccupiedException.class, () -> map.place(new Animal(new Vector2d(0, 2))));
        Assertions.assertThrows(PositionAlreadyOccupiedException.class, () -> map.place(new Animal(new Vector2d(2, 10))));

    }

    @Test
    public void shouldClearPlaceAfterAnimalMoves() {
        Animal animal1 = new Animal(new Vector2d(2, 2));
        Animal animal2 = new Animal(new Vector2d(2, 2));
        Assertions.assertDoesNotThrow(() -> map.place(animal1));

        Assertions.assertDoesNotThrow(() -> map.move(animal1));
        Assertions.assertFalse(map.objectAt(new Vector2d(2, 2)) instanceof Animal);

        Assertions.assertDoesNotThrow(() -> map.place(animal2));
        Assertions.assertEquals(animal1, map.objectAt(new Vector2d(2, 3)));
        Assertions.assertEquals(animal2, map.objectAt(new Vector2d(2, 2)));
    }

    @Test
    public void shouldMoveOnlyKnownAnimals() {
        Animal knownAnimal = new Animal(new Vector2d(1, 0));
        Animal foreignAnimal = new Animal(new Vector2d(2, 0));

        Assertions.assertDoesNotThrow(() -> map.place(knownAnimal));
        Assertions.assertDoesNotThrow(() -> map.move(knownAnimal));
        Assertions.assertTrue(knownAnimal.isAt(new Vector2d(1, 1)));

        Assertions.assertDoesNotThrow(() -> map.move(foreignAnimal));
        Assertions.assertFalse(foreignAnimal.isAt(new Vector2d(2, 1)));
    }
}