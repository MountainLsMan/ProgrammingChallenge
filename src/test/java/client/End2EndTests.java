package client;

import client.tester.ConsoleViewTester;
import client.view.ConsoleView;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@Sql("classpath:/db/e2e.sql")
@Import(End2EndTestsConfiguration.class)
public class End2EndTests {

    @Autowired
    ConsoleView view;

    @Autowired
    ByteArrayOutputStream outputStream;

    @Test
    public void shouldRenderPortfolioWithUpdates() {
        Map<String, BigDecimal> update1 = new HashMap<>();
        update1.put("AAPL", BigDecimal.valueOf(110.0));
        update1.put("TESLA", BigDecimal.valueOf(450.0));
        view.onMarketDataUpdate(update1);
        view.render();

        Map<String, BigDecimal> update2 = new HashMap<>();
        update2.put("AAPL", BigDecimal.valueOf(109.0));
        view.onMarketDataUpdate(update2);
        view.render();

        Map<String, BigDecimal> update3 = new HashMap<>();
        update3.put("TESLA", BigDecimal.valueOf(451.0));
        view.onMarketDataUpdate(update3);
        view.render();

        ConsoleViewTester tester = new ConsoleViewTester(outputStream);
        tester.assertContentExists(
                "## 1 Market Data Update",
                "AAPL change to 110.00",
                "TESLA change to 450.00",
                "",
                "## Portfolio",
                "symbol                             price                 qty               value",
                "AAPL                              110.00            1,000.00          110,000.00",
                "AAPL-OCT-2020-110-C                 5.20          -20,000.00         -103,940.72",
                "AAPL-OCT-2020-110-P                 4.10           20,000.00           82,050.35",
                "TESLA                             450.00             -500.00         -225,000.00",
                "TESLA-NOV-2020-400-C               53.98           10,000.00          539,806.17",
                "TESLA-DEC-2020-400-P                 .02          -10,000.00             -185.29",
                "",
                "#Total portfolio                                                      402,730.51"
        );

        tester.assertContentExists(
                "## 2 Market Data Update",
                "AAPL change to 109.00",
                "",
                "## Portfolio",
                "symbol                             price                 qty               value",
                "AAPL                              109.00            1,000.00          109,000.00",
                "AAPL-OCT-2020-110-C                 4.66          -20,000.00          -93,110.06",
                "AAPL-OCT-2020-110-P                 4.56           20,000.00           91,219.70",
                "TESLA                             450.00             -500.00         -225,000.00",
                "TESLA-NOV-2020-400-C               53.98           10,000.00          539,806.17",
                "TESLA-DEC-2020-400-P                 .02          -10,000.00             -185.29",
                "",
                "#Total portfolio                                                      421,730.51"
        );

        tester.assertContentExists(
                "## 3 Market Data Update",
                "TESLA change to 451.00",
                "",
                "## Portfolio",
                "symbol                             price                 qty               value",
                "AAPL                              109.00            1,000.00          109,000.00",
                "AAPL-OCT-2020-110-C                 4.66          -20,000.00          -93,110.06",
                "AAPL-OCT-2020-110-P                 4.56           20,000.00           91,219.70",
                "TESLA                             451.00             -500.00         -225,500.00",
                "TESLA-NOV-2020-400-C               54.98           10,000.00          549,804.92",
                "TESLA-DEC-2020-400-P                 .02          -10,000.00             -159.95",
                "",
                "#Total portfolio                                                      431,254.61"
        );
    }

}


