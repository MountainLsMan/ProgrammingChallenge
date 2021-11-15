package server;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class FakeMarketDataProvider {

    private final Random random = new Random();
    private final List<FakeStockSetting> stockSettings = new ArrayList<>();

    public void addStock(String symbol, double min, double max) {
        FakeStockSetting setting = new FakeStockSetting();

        setting.symbol = symbol;
        setting.min = min;
        setting.max = max;

        stockSettings.add(setting);
    }

    public Map<String, BigDecimal> next() {
        Map<String, BigDecimal> result = new HashMap<>();

        for (FakeStockSetting setting : stockSettings) {
            OptionalDouble first = random.doubles(setting.min, setting.max).findFirst();

            if (first.isPresent()) {
                BigDecimal value = BigDecimal.valueOf(first.getAsDouble()).setScale(2, RoundingMode.HALF_UP);
                result.put(setting.symbol, value);
            }
        }

        return result;
    }

    static class FakeStockSetting {

        private String symbol;
        private double min;
        private double max;

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public double getMin() {
            return min;
        }

        public void setMin(double min) {
            this.min = min;
        }

        public double getMax() {
            return max;
        }

        public void setMax(double max) {
            this.max = max;
        }
    }

}
