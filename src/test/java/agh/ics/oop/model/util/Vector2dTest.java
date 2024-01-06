package agh.ics.oop.model.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class Vector2dTest {

    @Test
    public void Equals_test() {
        Vector2d vector1 = new Vector2d(3, 0);
        Vector2d vector2 = new Vector2d(3, 0);
        Vector2d vector3 = new Vector2d(3, 3);

        Assertions.assertTrue(vector1.equals(vector1));
        Assertions.assertTrue(vector1.equals(vector2));
        Assertions.assertFalse(vector1.equals(vector3));
        Assertions.assertFalse(vector1.equals(20));
    }

    @Test
    public void ToString_test() {
        Vector2d vector1 = new Vector2d(3, 0);
        Vector2d vector2 = new Vector2d(0, 3);
        Vector2d vector3 = new Vector2d(3, 3);

        Assertions.assertEquals(vector1.toString(), "(3,0)");
        Assertions.assertEquals(vector2.toString(), "(0,3)");
        Assertions.assertEquals(vector3.toString(), "(3,3)");
    }

    @Test
    public void Precedes_test() {
        Vector2d vector1 = new Vector2d(1, 1);
        Vector2d vector2 = new Vector2d(2, 2);
        Vector2d vector3 = new Vector2d(2, 3);

        Assertions.assertTrue(vector1.precedes(vector1));
        Assertions.assertTrue(vector1.precedes(vector2));
        Assertions.assertTrue(vector1.precedes(vector3));
        Assertions.assertFalse(vector2.precedes(vector1));
        Assertions.assertTrue(vector2.precedes(vector2));
        Assertions.assertTrue(vector2.precedes(vector3));
        Assertions.assertFalse(vector3.precedes(vector1));
        Assertions.assertFalse(vector3.precedes(vector2));
        Assertions.assertTrue(vector3.precedes(vector3));
    }

    @Test
    public void Follows_test() {
        Vector2d vector1 = new Vector2d(1, 1);
        Vector2d vector2 = new Vector2d(2, 2);
        Vector2d vector3 = new Vector2d(2, 3);

        Assertions.assertTrue(vector1.follows(vector1));
        Assertions.assertFalse(vector1.follows(vector2));
        Assertions.assertFalse(vector1.follows(vector3));
        Assertions.assertTrue(vector2.follows(vector1));
        Assertions.assertTrue(vector2.follows(vector2));
        Assertions.assertFalse(vector2.follows(vector3));
        Assertions.assertTrue(vector3.follows(vector1));
        Assertions.assertTrue(vector3.follows(vector2));
        Assertions.assertTrue(vector3.follows(vector3));
    }

    @Test
    public void UpperRight_test() {
        Vector2d vector1 = new Vector2d(1, 1);
        Vector2d vector2 = new Vector2d(2, 2);
        Vector2d vector3 = new Vector2d(2, 3);

        Vector2d result1 = new Vector2d(2, 2);
        Vector2d result2 = new Vector2d(2, 3);

        Assertions.assertEquals(vector1.upperRight(vector1), vector1);
        Assertions.assertEquals(vector1.upperRight(vector2), result1);
        Assertions.assertEquals(vector1.upperRight(vector3), result2);
        Assertions.assertEquals(vector2.upperRight(vector1), result1);
        Assertions.assertEquals(vector2.upperRight(vector2), vector2);
        Assertions.assertEquals(vector3.upperRight(vector1), result2);
        Assertions.assertEquals(vector3.upperRight(vector3), vector3);
    }

    @Test
    public void LowerLeft_test() {
        Vector2d vector1 = new Vector2d(1, 1);
        Vector2d vector2 = new Vector2d(2, 2);
        Vector2d vector3 = new Vector2d(2, 3);

        Assertions.assertEquals(vector1.lowerLeft(vector1), vector1);
        Assertions.assertEquals(vector1.lowerLeft(vector2), vector1);
        Assertions.assertEquals(vector1.lowerLeft(vector3), vector1);
        Assertions.assertEquals(vector2.lowerLeft(vector1), vector1);
        Assertions.assertEquals(vector2.lowerLeft(vector2), vector2);
        Assertions.assertEquals(vector2.lowerLeft(vector3), vector2);
        Assertions.assertEquals(vector3.lowerLeft(vector1), vector1);
        Assertions.assertEquals(vector3.lowerLeft(vector2), vector2);
        Assertions.assertEquals(vector3.lowerLeft(vector3), vector3);
    }

    @Test
    public void Add_test() {
        Vector2d vector1 = new Vector2d(1, 1);
        Vector2d vector2 = new Vector2d(2, 2);
        Vector2d vector3 = new Vector2d(2, 3);

        Vector2d res1 = new Vector2d(2, 2);
        Vector2d res2 = new Vector2d(3, 3);
        Vector2d res3 = new Vector2d(3, 4);
        Vector2d res4 = new Vector2d(3, 3);
        Vector2d res5 = new Vector2d(4, 4);
        Vector2d res6 = new Vector2d(4, 5);
        Vector2d res7 = new Vector2d(3, 4);
        Vector2d res8 = new Vector2d(4, 5);
        Vector2d res9 = new Vector2d(4, 6);


        Assertions.assertEquals(vector1.add(vector1), res1);
        Assertions.assertEquals(vector1.add(vector2), res2);
        Assertions.assertEquals(vector1.add(vector3), res3);
        Assertions.assertEquals(vector2.add(vector1), res4);
        Assertions.assertEquals(vector2.add(vector2), res5);
        Assertions.assertEquals(vector2.add(vector3), res6);
        Assertions.assertEquals(vector3.add(vector1), res7);
        Assertions.assertEquals(vector3.add(vector2), res8);
        Assertions.assertEquals(vector3.add(vector3), res9);
    }

    @Test
    public void Subtract_test() {
        Vector2d vector1 = new Vector2d(1, 1);
        Vector2d vector2 = new Vector2d(2, 2);
        Vector2d vector3 = new Vector2d(2, 3);

        Vector2d res1 = new Vector2d(0, 0);
        Vector2d res2 = new Vector2d(-1, -1);
        Vector2d res3 = new Vector2d(-1, -2);
        Vector2d res4 = new Vector2d(1, 1);
        Vector2d res5 = new Vector2d(0, 0);
        Vector2d res6 = new Vector2d(0, -1);
        Vector2d res7 = new Vector2d(1, 2);
        Vector2d res8 = new Vector2d(0, 1);
        Vector2d res9 = new Vector2d(0, 0);

        Assertions.assertEquals(vector1.subtract(vector1), res1);
        Assertions.assertEquals(vector1.subtract(vector2), res2);
        Assertions.assertEquals(vector1.subtract(vector3), res3);
        Assertions.assertEquals(vector2.subtract(vector1), res4);
        Assertions.assertEquals(vector2.subtract(vector2), res5);
        Assertions.assertEquals(vector2.subtract(vector3), res6);
        Assertions.assertEquals(vector3.subtract(vector1), res7);
        Assertions.assertEquals(vector3.subtract(vector2), res8);
        Assertions.assertEquals(vector3.subtract(vector3), res9);
    }

    @Test
    public void Opposite_test() {
        Vector2d vector1 = new Vector2d(1, 1);
        Vector2d vector2 = new Vector2d(2, 2);
        Vector2d vector3 = new Vector2d(2, 3);

        Vector2d res1 = new Vector2d(-1, -1);
        Vector2d res2 = new Vector2d(-2, -2);
        Vector2d res3 = new Vector2d(-2, -3);

        Assertions.assertEquals(vector1.opposite(), res1);
        Assertions.assertEquals(vector2.opposite(), res2);
        Assertions.assertEquals(vector3.opposite(), res3);
    }
}