package client.service.csv;

import client.model.Position;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Scanner;

@SpringBootTest
public class PortfolioCsvParserTests {

    @Autowired
    private PortfolioCsvParser parser;

    @Test
    void shouldAbleToParsePortfolio() {
        Scanner scanner = new Scanner(
                "symbol,positionSize\n" +
                        "AAPL,100\n" +
                        "TSLA,-500\n"
        );
        List<Position> actual = parser.parse(scanner);
        Object[] expects = new Position[]{
                new Position("AAPL", 100),
                new Position("TSLA", -500)
        };

        Assertions.assertArrayEquals(expects, actual.toArray());
    }

    @Test
    void shouldThrowExceptionWhenMissingColumn() {
        Scanner scanner = new Scanner(
                "symbol\n" +
                        "AAPL\n"
        );

        IllegalArgumentException exception = Assert.assertThrows(IllegalArgumentException.class, () -> parser.parse(scanner));
        assert exception.getMessage().equals("CSV should only contains 2 columns");
    }

    @Test
    void shouldThrowExceptionWhenSizeIsNotNumber() {
        Scanner scanner = new Scanner(
                "symbol,positionSize\n" +
                        "AAPL,15.5\n"
        );

        IllegalArgumentException exception = Assert.assertThrows(IllegalArgumentException.class, () -> parser.parse(scanner));
        System.out.println(exception.getMessage());
        assert exception.getMessage().equals("Position Size should be in decimal format");
    }

}

