package agh.ics.oop;

import agh.ics.oop.model.MoveDirection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static agh.ics.oop.OptionParser.Parse;

public class OptionParserTest {

    @Test
    public void Parse_test() {
        String[] firstString = {"f", "l", "r", "r", "l", "b"};
        String[] secondString = {"f", "xddd", " ", "dupa"};
        String[] thirdString = {"l", "r", "2", "", "null", "r"};

        List<MoveDirection> res1 = new ArrayList<> (List.of(MoveDirection.FORWARD, MoveDirection.LEFT, MoveDirection.RIGHT,
                MoveDirection.RIGHT, MoveDirection.LEFT, MoveDirection.BACKWARD));

        Assertions.assertEquals(res1, Parse(firstString));
        Assertions.assertThrows(IllegalArgumentException.class, () -> Parse(secondString));
        Assertions.assertThrows(IllegalArgumentException.class, () -> Parse(thirdString));
    }
}
