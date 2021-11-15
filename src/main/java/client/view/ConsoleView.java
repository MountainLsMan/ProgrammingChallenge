package client.view;

import client.model.Position;
import client.service.calculator.SecurityPriceCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ConsoleView {

    private static final DecimalFormat formatter = new DecimalFormat("#,###.00");

    private final List<Position> portfolio;
    private final SecurityPriceCalculator securityValueCalculator;
    private final Map<String, BigDecimal> marketDataStore;
    private final PrintStream out;

    // view state
    private Map<String, BigDecimal> lastPricesUpdate;
    private int counter;

    @Autowired
    public ConsoleView(List<Position> portfolio, SecurityPriceCalculator securityValueCalculator, Map<String, BigDecimal> marketDataStore, PrintStream out) {
        this.portfolio = portfolio;
        this.securityValueCalculator = securityValueCalculator;
        this.marketDataStore = marketDataStore;
        this.out = out;

        this.lastPricesUpdate = new HashMap<>();
        this.counter = 0;
    }

    public void onMarketDataUpdate(Map<String, BigDecimal> prices) {
        counter++;
        lastPricesUpdate = prices;
        marketDataStore.putAll(lastPricesUpdate);
        render();
    }

    public void render() {
        renderMarketDataUpdate();
        renderPortfolioDetails();
    }

    private void renderMarketDataUpdate() {
        out.printf("## %s Market Data Update%n", counter);

        for (Map.Entry<String, BigDecimal> entry : lastPricesUpdate.entrySet()) {
            String ticker = entry.getKey();
            BigDecimal newPrice = entry.getValue();

            out.printf("%s change to %s%n", ticker, formatter.format(newPrice));
        }

        out.println();
    }

    private void renderPortfolioDetails() {
        out.println("## Portfolio");
        out.printf("%-20s%20s%20s%20s%n", "symbol", "price", "qty", "value");

        BigDecimal total = new BigDecimal(0);
        for (Position position : portfolio) {
            String symbol = position.getSymbol();
            BigDecimal price = securityValueCalculator.calculate(symbol);
            long quantity = position.getSize();
            BigDecimal value = price != null ? price.multiply(BigDecimal.valueOf(quantity)) : null;

            if (value != null) {
                total = total.add(value);
            }

            out.printf("%-20s%20s%20s%20s%n",
                    symbol,
                    price != null ? formatter.format(price) : "N/A",
                    formatter.format(quantity),
                    value != null ? formatter.format(value) : "N/A"
            );
        }

        out.println();
        out.printf("%-40s%40s%n%n%n", "#Total portfolio", formatter.format(total));
    }

}
