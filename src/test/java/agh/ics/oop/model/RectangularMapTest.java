package agh.ics.oop.model;

import agh.ics.oop.model.util.PositionAlreadyOccupiedException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RectangularMapTest {
    private RectangularMap map;

    @BeforeEach
    public void setUp() {
        map = new RectangularMap(4, 4);
    }

    @Test
    public void shouldThrowIfDimensionsAreInvalid() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new RectangularMap(0, 4));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new RectangularMap(4, 0));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new RectangularMap(-1, 4));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new RectangularMap(4, -1));
    }

    @Test
    public void shouldValidateMove() {
        Assertions.assertDoesNotThrow(() -> map.place(new Animal(new Vector2d(2, 2))));

        Assertions.assertTrue(map.canMoveTo(new Vector2d(1, 3)));
        Assertions.assertFalse(map.canMoveTo(new Vector2d(4, 5)));
        Assertions.assertFalse(map.canMoveTo(new Vector2d(-1, 3)));
        Assertions.assertFalse(map.canMoveTo(new Vector2d(9, 9)));
        Assertions.assertFalse(map.canMoveTo(new Vector2d(2, 2)));
    }

    @Test
    public void shouldValidateAnimalPlacement() {
        Animal animal = new Animal();

        Assertions.assertDoesNotThrow(() -> map.place(animal));

        Assertions.assertThrows(PositionAlreadyOccupiedException.class, () -> map.place(new Animal(new Vector2d(2, 2))));
        Assertions.assertThrows(PositionAlreadyOccupiedException.class, () -> map.place(new Animal(new Vector2d(2, 10))));
        Assertions.assertThrows(PositionAlreadyOccupiedException.class, () -> map.place(new Animal(new Vector2d(-1, 3))));
        Assertions.assertThrows(PositionAlreadyOccupiedException.class, () -> map.place(animal));
    }

    @Test
    public void shouldClearPlaceAfterAnimalMoves() {
        Animal animal1 = new Animal(new Vector2d(2, 2));
        Animal animal2 = new Animal(new Vector2d(2, 2));
        Assertions.assertDoesNotThrow(() -> map.place(animal1));

        Assertions.assertDoesNotThrow(() -> map.move(animal1, MoveDirection.FORWARD));
        Assertions.assertNull(map.objectAt(new Vector2d(2, 2)));

        Assertions.assertDoesNotThrow(() -> map.place(animal2));
        Assertions.assertEquals(animal1, map.objectAt(new Vector2d(2, 3)));
        Assertions.assertEquals(animal2, map.objectAt(new Vector2d(2, 2)));
    }

    @Test
    public void shouldMoveOnlyKnownAnimals() {
        Animal knownAnimal = new Animal(new Vector2d(1, 0));
        Animal foreignAnimal = new Animal(new Vector2d(2, 0));

        Assertions.assertDoesNotThrow(() -> map.place(knownAnimal));
        Assertions.assertDoesNotThrow(() -> map.move(knownAnimal, MoveDirection.FORWARD));
        Assertions.assertTrue(knownAnimal.isAt(new Vector2d(1, 1)));

        Assertions.assertDoesNotThrow(() -> map.move(foreignAnimal, MoveDirection.FORWARD));
        Assertions.assertFalse(foreignAnimal.isAt(new Vector2d(2, 1)));
    }
}