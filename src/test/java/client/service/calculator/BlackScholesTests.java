package client.service.calculator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

public class BlackScholesTests {

    private static Stream<Arguments> sampleCallOptionsParameters() {
        return Stream.of(
                arguments(300.0, 250.0, 0.02, 0.15, 1, 56.61),
                arguments(300.0, 250.0, 0.02, 0.15, 2, 64.11),
                arguments(300.0, 250.0, 0.02, 0.15, 3, 71.05),
                arguments(23.75, 15.0, 0.01, 0.35, 1, 9.17),
                arguments(30.14, 15.0, 0.01, 0.332, 2, 15.7)
        );
    }

    private static Stream<Arguments> samplePutOptionsParameters() {
        return Stream.of(
                arguments(300.0, 250.0, 0.02, 0.15, 1, 1.66),
                arguments(300.0, 250.0, 0.02, 0.15, 2, 4.31),
                arguments(300.0, 250.0, 0.02, 0.15, 3, 6.49),
                arguments(23.75, 15.0, 0.01, 0.35, 1, 0.27),
                arguments(30.14, 15.0, 0.01, 0.332, 2, 0.27)
        );
    }


    @ParameterizedTest
    @MethodSource("sampleCallOptionsParameters")
    public void shouldAbleToCalculateCallOptionPrice(double spotPrice, double strikePrice, double interestRate, double volatility, double timeToExpiration, double expectPrice) {
        double price = BlackScholes.calculateCallOptionPrice(spotPrice, strikePrice, interestRate, volatility, timeToExpiration);
        double actualPrice = BigDecimal.valueOf(price).setScale(2, RoundingMode.HALF_UP).doubleValue();
        Assertions.assertEquals(expectPrice, actualPrice);
    }

    @ParameterizedTest
    @MethodSource("samplePutOptionsParameters")
    public void shouldAbleToCalculatePutOptionPrice(double spotPrice, double strikePrice, double interestRate, double volatility, double timeToExpiration, double expectPrice) {
        double price = BlackScholes.calculatePutOptionPrice(spotPrice, strikePrice, interestRate, volatility, timeToExpiration);
        double actualPrice = BigDecimal.valueOf(price).setScale(2, RoundingMode.HALF_UP).doubleValue();
        Assertions.assertEquals(expectPrice, actualPrice);
    }

}
