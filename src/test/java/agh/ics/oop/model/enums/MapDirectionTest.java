package agh.ics.oop.model.enums;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import agh.ics.oop.model.util.Vector2d;

public class MapDirectionTest {
    @Test
    public void Rotate_test() {
        Assertions.assertEquals(MapDirection.NORTH.rotate(0), MapDirection.NORTH);
        Assertions.assertEquals(MapDirection.NORTH.rotate(1), MapDirection.NORTH_EAST);
        Assertions.assertEquals(MapDirection.NORTH_EAST.rotate(2), MapDirection.SOUTH_EAST);
        Assertions.assertEquals(MapDirection.SOUTH_EAST.rotate(3), MapDirection.WEST);
        Assertions.assertEquals(MapDirection.WEST.rotate(4), MapDirection.EAST);
        Assertions.assertEquals(MapDirection.EAST.rotate(5), MapDirection.NORTH_WEST);
        Assertions.assertEquals(MapDirection.NORTH_WEST.rotate(6), MapDirection.SOUTH_WEST);
        Assertions.assertEquals(MapDirection.SOUTH_WEST.rotate(7), MapDirection.SOUTH);
    }

    @Test
    public void ToString_test() {
        Assertions.assertEquals(MapDirection.NORTH.toString(), "N");
        Assertions.assertEquals(MapDirection.NORTH_EAST.toString(), "NE");
        Assertions.assertEquals(MapDirection.EAST.toString(), "E");
        Assertions.assertEquals(MapDirection.SOUTH_EAST.toString(), "SE");
        Assertions.assertEquals(MapDirection.SOUTH.toString(), "S");
        Assertions.assertEquals(MapDirection.SOUTH_WEST.toString(), "SW");
        Assertions.assertEquals(MapDirection.WEST.toString(), "W");
        Assertions.assertEquals(MapDirection.NORTH_WEST.toString(), "NW");
    }

    @Test
    public void ToUnitVector() {
        Assertions.assertEquals(MapDirection.NORTH.toUnitVector(), new Vector2d(0,1));
        Assertions.assertEquals(MapDirection.NORTH_EAST.toUnitVector(), new Vector2d(1,1));
        Assertions.assertEquals(MapDirection.EAST.toUnitVector(), new Vector2d(1,0));
        Assertions.assertEquals(MapDirection.SOUTH_EAST.toUnitVector(), new Vector2d(1,-1));
        Assertions.assertEquals(MapDirection.SOUTH.toUnitVector(), new Vector2d(0,-1));
        Assertions.assertEquals(MapDirection.SOUTH_WEST.toUnitVector(), new Vector2d(-1,-1));
        Assertions.assertEquals(MapDirection.WEST.toUnitVector(), new Vector2d(-1,0));
        Assertions.assertEquals(MapDirection.NORTH_WEST.toUnitVector(), new Vector2d(-1,1));
    }

}