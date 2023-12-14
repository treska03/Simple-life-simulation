package agh.ics.oop.model;

import agh.ics.oop.model.util.PositionAlreadyOccupiedException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GrassFieldTest {
    private GrassField map;

    @BeforeEach
    public void setUp() {
        map = new GrassField(100);
    }

    @Test
    public void shouldThrowIfDimensionsAreInvalid() {
        Assertions.assertDoesNotThrow(() -> new GrassField(0));
        Assertions.assertDoesNotThrow(() -> new GrassField(4));
        Assertions.assertDoesNotThrow(() -> new GrassField(100));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new GrassField(-1));
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

        Assertions.assertDoesNotThrow(() -> map.move(animal1, MoveDirection.FORWARD));
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
        Assertions.assertDoesNotThrow(() -> map.move(knownAnimal, MoveDirection.FORWARD));
        Assertions.assertTrue(knownAnimal.isAt(new Vector2d(1, 1)));

        Assertions.assertDoesNotThrow(() -> map.move(foreignAnimal, MoveDirection.FORWARD));
        Assertions.assertFalse(foreignAnimal.isAt(new Vector2d(2, 1)));
    }
}