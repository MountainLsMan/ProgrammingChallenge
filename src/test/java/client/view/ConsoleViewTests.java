package client.view;

import client.model.Position;
import client.service.calculator.SecurityPriceCalculator;
import client.tester.ConsoleViewTester;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.*;

@SpringBootTest
public class ConsoleViewTests {

    ByteArrayOutputStream outputStream;
    SecurityPriceCalculator calculator;

    ConsoleView view;

    @BeforeEach
    public void setUp() {
        // Portfolio
        List<Position> positions = new ArrayList<>();
        positions.add(new Position("AAPL", 100));
        positions.add(new Position("TESLA", 50));
        positions.add(new Position("AAPL-PUT", 25));

        // Mock calculator
        calculator = Mockito.mock(SecurityPriceCalculator.class);
        Mockito.when(calculator.calculate("AAPL")).thenReturn(BigDecimal.valueOf(250));
        Mockito.when(calculator.calculate("TESLA")).thenReturn(null);
        Mockito.when(calculator.calculate("AAPL-PUT")).thenReturn(BigDecimal.valueOf(5.85));
//        Mockito.when(calculator.calculate(anyString())).thenReturn(BigDecimal.valueOf(0));

        // Market Data Store
        Map<String, BigDecimal> store = new HashMap<>();

        // Canvas
        outputStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outputStream);

        view = new ConsoleView(positions, calculator, store, out);
    }

    @Test
    void shouldRenderMarketDataUpdate() {
        Map<String, BigDecimal> update1 = new LinkedHashMap<>();
        update1.put("AAPL", BigDecimal.valueOf(10));

        view.onMarketDataUpdate(update1);
        view.render();

        Map<String, BigDecimal> update2 = new LinkedHashMap<>();
        update2.put("AAPL", BigDecimal.valueOf(15));
        update2.put("TESLA", BigDecimal.valueOf(20));
        update2.put("MSFT", BigDecimal.valueOf(15.5));

        view.onMarketDataUpdate(update2);
        view.render();

        Map<String, BigDecimal> update3 = new LinkedHashMap<>();
        update3.put("TESLA", BigDecimal.valueOf(21.2));
        update3.put("MSFT", BigDecimal.valueOf(12.5));

        view.onMarketDataUpdate(update3);
        view.render();

        ConsoleViewTester tester = new ConsoleViewTester(outputStream);
        tester.assertContentExists(
                "## 1 Market Data Update",
                "AAPL change to 10.00"
        );

        tester.assertContentExists(
                "## 2 Market Data Update",
                "AAPL change to 15.00",
                "TESLA change to 20.00",
                "MSFT change to 15.50"
        );

        tester.assertContentExists(
                "## 3 Market Data Update",
                "TESLA change to 21.20",
                "MSFT change to 12.50"
        );
    }

    @Test
    void shouldRenderPortfolioDetails() {
        view.render();

        ConsoleViewTester tester = new ConsoleViewTester(outputStream);
        tester.assertContentExists(
                "## Portfolio",
                "symbol                             price                 qty               value",
                "AAPL                              250.00              100.00           25,000.00",
                "TESLA                                N/A               50.00                 N/A",
                "AAPL-PUT                            5.85               25.00              146.25",
                "",
                "#Total portfolio                                                       25,146.25"
        );
    }

}

