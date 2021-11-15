package client.service.calculator;

import client.model.OptionSecurityDefinition;
import client.model.StockSecurityDefinition;
import client.model.enums.OptionType;
import client.model.enums.SecurityType;
import client.repository.SecurityDefinitionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class SecurityValueCalculatorTests {

    SecurityPriceCalculator calculator;

    Map<String, BigDecimal> marketDataStore;

    @BeforeEach
    public void setUp() {
        // Mock repository
        SecurityDefinitionRepository repository = Mockito.mock(SecurityDefinitionRepository.class);

        StockSecurityDefinition aapl = new StockSecurityDefinition();
        aapl.setTicker("AAPL");
        aapl.setType(SecurityType.Stock);

        StockSecurityDefinition tesla = new StockSecurityDefinition();
        tesla.setTicker("TESLA");
        tesla.setType(SecurityType.Stock);

        OptionSecurityDefinition aaplPut = new OptionSecurityDefinition();
        aaplPut.setTicker("AAPL-PUT");
        aaplPut.setType(SecurityType.Option);
        aaplPut.setOptionType(OptionType.Put);
        aaplPut.setUnderlying("AAPL");
        aaplPut.setStrikePrice(225);
        aaplPut.setVolatility(0.15);
        aaplPut.setTimeToExpiration(2);

        Mockito.when(repository.findByTicker(aapl.getTicker())).thenReturn(aapl);
        Mockito.when(repository.findByTicker(aaplPut.getTicker())).thenReturn(aaplPut);
        Mockito.when(repository.findByTicker(tesla.getTicker())).thenReturn(tesla);

        // Market Data Store
        marketDataStore = new HashMap<>();

        calculator = new SecurityPriceCalculator(repository, marketDataStore);
    }

    @Test
    public void shouldAbleToCalculateStockSecurityValue() {
        marketDataStore.put("AAPL", BigDecimal.valueOf(240));
        marketDataStore.put("TESLA", BigDecimal.valueOf(1200));

        BigDecimal aaplAmount = calculator.calculate("AAPL");
        Assertions.assertEquals(aaplAmount, BigDecimal.valueOf(240));

        BigDecimal teslaAmount = calculator.calculate("TESLA");
        Assertions.assertEquals(teslaAmount, BigDecimal.valueOf(1200));
    }

    @Test
    public void shouldAbleToCalculateOptionSecurityValue() {
        marketDataStore.put("AAPL", BigDecimal.valueOf(240));
        BigDecimal actual = calculator.calculate("AAPL-PUT");
        double expectedPrice = BlackScholes.calculatePutOptionPrice(240, 225, 0.02, 0.15, 2);
        Assertions.assertEquals(BigDecimal.valueOf(expectedPrice), actual);

        // recalculate with lower price
        marketDataStore.put("AAPL", BigDecimal.valueOf(235));
        BigDecimal newActual = calculator.calculate("AAPL-PUT");
        double newExpectedPrice = BlackScholes.calculatePutOptionPrice(235, 225, 0.02, 0.15, 2);
        Assertions.assertEquals(BigDecimal.valueOf(newExpectedPrice), newActual);
    }

    @Test
    public void shouldReturnNullWhenNoMarketData() {
        marketDataStore.remove("AAPL");
        BigDecimal amount = calculator.calculate("AAPL");
        Assertions.assertNull(amount);
    }

}
