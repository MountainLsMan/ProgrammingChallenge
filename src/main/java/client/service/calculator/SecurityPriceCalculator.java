package client.service.calculator;

import client.model.OptionSecurityDefinition;
import client.model.SecurityDefinition;
import client.model.enums.OptionType;
import client.model.enums.SecurityType;
import client.repository.SecurityDefinitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class SecurityPriceCalculator {

    private static final double INTEREST_RATE = 0.02;

    private final SecurityDefinitionRepository repository;
    private final Map<String, BigDecimal> marketDataStore;

    @Autowired
    public SecurityPriceCalculator(SecurityDefinitionRepository repository, Map<String, BigDecimal> marketDataStore) {
        this.repository = repository;
        this.marketDataStore = marketDataStore;
    }

    public BigDecimal calculate(String symbol) {
        SecurityDefinition definition = repository.findByTicker(symbol);

        if (definition == null) {
            throw new IllegalStateException(String.format("No security definition found for symbol %s", symbol));
        }

        if (definition.getType() == SecurityType.Stock) {
            return marketDataStore.get(symbol);
        } else if (definition.getType() == SecurityType.Option) {
            OptionSecurityDefinition optionDef = (OptionSecurityDefinition) definition;

            if (!marketDataStore.containsKey(optionDef.getUnderlying())) return null;
            BigDecimal spotPrice = marketDataStore.get(optionDef.getUnderlying());
            double strikePrice = optionDef.getStrikePrice();
            double volatility = optionDef.getVolatility();
            double timeToExpiration = optionDef.getTimeToExpiration();

            if (optionDef.getOptionType() == OptionType.Call) {
                double optionPrice = BlackScholes.calculateCallOptionPrice(spotPrice.doubleValue(), strikePrice, INTEREST_RATE, volatility, timeToExpiration);
                return BigDecimal.valueOf(optionPrice);
            } else if (optionDef.getOptionType() == OptionType.Put) {
                double optionPrice = BlackScholes.calculatePutOptionPrice(spotPrice.doubleValue(), strikePrice, INTEREST_RATE, volatility, timeToExpiration);
                return BigDecimal.valueOf(optionPrice);
            } else {
                throw new UnsupportedOperationException(String.format("Unsupported option type: %s", optionDef.getOptionType()));
            }
        } else {
            throw new UnsupportedOperationException(String.format("Unsupported security type: %s", definition.getType()));
        }
    }

}
