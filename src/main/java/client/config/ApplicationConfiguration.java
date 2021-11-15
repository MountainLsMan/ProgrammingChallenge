package client.config;

import client.model.Position;
import client.service.csv.PortfolioCsvParser;
import client.service.messging.MessageQueue;
import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.queue.ExcerptAppender;
import net.openhft.chronicle.queue.ExcerptTailer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.*;

@Configuration
public class ApplicationConfiguration {

    @Autowired
    PortfolioCsvParser parser;

    @Bean
    public List<Position> getPortfolio(@Value("${client.portfolio.filePath}") File csvFile) throws FileNotFoundException {
        Scanner scanner = new Scanner(csvFile);
        return parser.parse(scanner);
    }

    @Bean
    public Map<String, BigDecimal> getMarketDataStore() {
        return new HashMap<>();
    }

    @Bean
    public PrintStream getPrintStream() {
        return System.out;
    }

}
