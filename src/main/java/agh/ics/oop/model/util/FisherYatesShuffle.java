package agh.ics.oop.model.util;

import agh.ics.oop.model.Vector2d;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FisherYatesShuffle {
    // Fisher-Yates shuffle is an algorithm that chose random unique values from the given list;
    // Time complexity O(k), where k is the number of elements we want to choose

    public static List<Integer> getIntegerValues(int numberOfReturnedValues, List<Integer> array){
        Random random = new Random();
        List<Integer> ReturnedValues = new ArrayList<>();
        for (int i = array.size() - 1; i >= array.size() - numberOfReturnedValues; i--) {
            int randomIndex = random.nextInt(i + 1);
            int randomValue = array.get(randomIndex);
            array.set(randomIndex, array.get(i));
            array.set(i, randomValue);
            ReturnedValues.add(randomValue);
        }
        return ReturnedValues;
    }

    public static List<Vector2d> getVector2dValues(int numberOfReturnedValues, List<Vector2d> array){
        Random random = new Random();
        List<Vector2d> ReturnedValues = new ArrayList<>();
        for (int i = array.size() - 1; i >= array.size() - numberOfReturnedValues; i--) {
            int randomIndex = random.nextInt(i + 1);
            Vector2d randomValue = array.get(randomIndex);
            array.set(randomIndex, array.get(i));
            array.set(i, randomValue);
            ReturnedValues.add(randomValue);
        }
        return ReturnedValues;
    }
}
