package agh.ics.oop.model.algorithms;

import agh.ics.oop.model.algorithms.FisherYatesShuffle;
import agh.ics.oop.model.util.Vector2d;
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
        FisherYatesShuffle fisherYatesShuffle = new FisherYatesShuffle();
        List<Integer> returnedList = fisherYatesShuffle.getValues(5,list);

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
        FisherYatesShuffle fisherYatesShuffle = new FisherYatesShuffle();
        List<Vector2d> returnedList = fisherYatesShuffle.getValues(5,list);

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
