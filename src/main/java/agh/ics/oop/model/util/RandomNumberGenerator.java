package agh.ics.oop.model.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class RandomNumberGenerator {


    public int getRandomInRange(int start, int finish) {
        return (int)Math.floor(Math.random() * (finish - start + 1) + start);
    }
    public int getRandomInRange(int finish) {
        return getRandomInRange(0, finish);
    }

    public List<Integer> getNRandomInts(int n, int start, int finish) {
        if(n < finish-start) {
            throw new IllegalArgumentException("Range not big enough to fit %d digits".formatted(n));
        }
        List<Integer> intList = new ArrayList<>(IntStream.range(start, finish).boxed().toList());
        Collections.shuffle(intList);
        return intList.subList(0, n-1);
    }

}
