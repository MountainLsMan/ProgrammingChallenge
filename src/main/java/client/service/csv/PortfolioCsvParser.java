package client.service.csv;

import client.model.Position;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Service
public class PortfolioCsvParser {

    private static final String COMMA_DELIMITER = ",";

    @Value("${client.portfolio.hasHeader:true}")
    private boolean hasHeader;

    public List<Position> parse(Scanner scanner) {
        List<Position> records = new ArrayList<>();

        if (hasHeader && scanner.hasNextLine()) {
            scanner.nextLine();
        }

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] values = line.split(COMMA_DELIMITER);

            if (values.length != 2) {
                throw new IllegalArgumentException("CSV should only contains 2 columns");
            }

            try {
                String symbol = values[0];
                long size = Long.parseLong(values[1]);

                records.add(new Position(symbol, size));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Position Size should be in decimal format");
            }
        }

        return records;
    }

}
