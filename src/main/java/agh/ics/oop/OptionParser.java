package agh.ics.oop;
import agh.ics.oop.model.enums.MoveDirection;

import java.util.ArrayList;
import java.util.List;

public class OptionParser {
    public static List<MoveDirection> Parse(String[] args) {
        List<MoveDirection> result = new ArrayList<>();
        for (String arg : args) {
            switch (arg) {
                case "f" -> result.add(MoveDirection.FORWARD);
                case "b" -> result.add(MoveDirection.BACKWARD);
                case "l" -> result.add(MoveDirection.LEFT);
                case "r" -> result.add(MoveDirection.RIGHT);
                default -> throw new IllegalArgumentException(arg + " is not legal move specification");
            }
        }
        return result;
    }
}
