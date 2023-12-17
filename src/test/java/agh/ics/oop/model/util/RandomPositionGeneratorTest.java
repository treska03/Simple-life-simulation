package agh.ics.oop.model.util;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RandomPositionGeneratorTest {

    @Test
    void testRNG() {
        Vector2d lowerLeft = new Vector2d(0, 0);
        Vector2d upperRight = new Vector2d(10, 10);
        int grassCount = 10;
        RandomPositionGenerator randomPositionGenerator = new RandomPositionGenerator(lowerLeft, upperRight, grassCount);
        List<Vector2d> dummyList = new ArrayList<>();
        for(Vector2d grassPosition : randomPositionGenerator) {
            dummyList.add(grassPosition);
        }
        assertEquals(10, dummyList.size());
    }
}