package client;

import client.model.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

@TestConfiguration
public class End2EndTestsConfiguration {

    @Bean
    @Primary
    public List<Position> getTestPortfolio() {
        List<Position> portfolio = new ArrayList<>();

        portfolio.add(new Position("AAPL", 1000));
        portfolio.add(new Position("AAPL-OCT-2020-110-C", -20000));
        portfolio.add(new Position("AAPL-OCT-2020-110-P", 20000));
        portfolio.add(new Position("TESLA", -500));
        portfolio.add(new Position("TESLA-NOV-2020-400-C", 10000));
        portfolio.add(new Position("TESLA-DEC-2020-400-P", -10000));

        return portfolio;
    }

    @Bean
    ByteArrayOutputStream getBufferStream() {
        return new ByteArrayOutputStream();
    }

    @Bean
    @Primary
    public PrintStream getTestPrintStream(@Autowired ByteArrayOutputStream outputStream) {
        return new PrintStream(outputStream);
    }

}
