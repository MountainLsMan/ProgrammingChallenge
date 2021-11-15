package client.model;

import client.model.enums.OptionType;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@DiscriminatorValue("Option")
public class OptionSecurityDefinition extends SecurityDefinition {

    @Enumerated(EnumType.STRING)
    private OptionType optionType;

    private String underlying;
    private double strikePrice;
    private double volatility;
    private double timeToExpiration;

    public OptionType getOptionType() {
        return optionType;
    }

    public void setOptionType(OptionType optionType) {
        this.optionType = optionType;
    }

    public String getUnderlying() {
        return underlying;
    }

    public void setUnderlying(String underlying) {
        this.underlying = underlying;
    }

    public double getStrikePrice() {
        return strikePrice;
    }

    public void setStrikePrice(double strikePrice) {
        this.strikePrice = strikePrice;
    }

    public double getVolatility() {
        return volatility;
    }

    public void setVolatility(double volatility) {
        this.volatility = volatility;
    }

    public double getTimeToExpiration() {
        return timeToExpiration;
    }

    public void setTimeToExpiration(double timeToExpiration) {
        this.timeToExpiration = timeToExpiration;
    }
}
