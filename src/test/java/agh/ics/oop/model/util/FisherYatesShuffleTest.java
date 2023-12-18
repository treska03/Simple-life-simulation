package agh.ics.oop.model.util;

import agh.ics.oop.model.Vector2d;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class FisherYatesShuffleTest {
    @Test
    public void getIntegerValues_test() {
        List<Integer> list = new ArrayList<>();
        list.add(3);
        list.add(2);
        list.add(1);
        list.add(5);
        list.add(4);
        List<Integer> returnedList = FisherYatesShuffle.getIntegerValues(5,list);

        // check if any values repeat
        boolean correct = true;
        for (int i = 0; i < 5; i++){
            for (int j = 0; j < 5; j++){
                if (returnedList.get(i) == returnedList.get(j) && i != j){
                    correct = false;
                }
            }
        }

        Assertions.assertTrue(correct);
    }

    @Test
    public void getVector2DValues_test() {
        List<Vector2d> list = new ArrayList<>();
        list.add(new Vector2d(3,3));
        list.add(new Vector2d(2,2));
        list.add(new Vector2d(1,1));
        list.add(new Vector2d(5,5));
        list.add(new Vector2d(4,4));
        List<Vector2d> returnedList = FisherYatesShuffle.getVector2dValues(5,list);

        // check if any values repeat
        boolean correct = true;
        for (int i = 0; i < 5; i++){
            for (int j = 0; j < 5; j++){
                if (returnedList.get(i).equals(returnedList.get(j)) && i != j){
                    correct = false;
                }
            }
        }

        Assertions.assertTrue(correct);
    }
}
