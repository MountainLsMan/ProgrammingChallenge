package client.repository;

import client.model.OptionSecurityDefinition;
import client.model.SecurityDefinition;
import client.model.StockSecurityDefinition;
import client.model.enums.OptionType;
import client.model.enums.SecurityType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Collections;
import java.util.List;

@SpringBootTest
@Sql("classpath:/db/sample.sql")
public class SecurityDefinitionRepositoryTests {

    @Autowired
    private SecurityDefinitionRepository repo;

    @Test
    public void shouldAbleToRetrieveDataFromRepository() {
        List<SecurityDefinition> list = repo.findAll();
        Assertions.assertEquals(2, list.size());

        Collections.sort(list);
        StockSecurityDefinition definition1 = (StockSecurityDefinition) list.get(0);
        Assertions.assertEquals("XYZ", definition1.getTicker());
        Assertions.assertEquals(SecurityType.Stock, definition1.getType());

        OptionSecurityDefinition definition2 = (OptionSecurityDefinition) list.get(1);
        Assertions.assertEquals("XYZ-PUT", definition2.getTicker());
        Assertions.assertEquals(SecurityType.Option, definition2.getType());
        Assertions.assertEquals(OptionType.Put, definition2.getOptionType());
        Assertions.assertEquals("XYZ", definition2.getUnderlying());
        Assertions.assertEquals(50.0, definition2.getStrikePrice());
        Assertions.assertEquals(0.15, definition2.getVolatility());
        Assertions.assertEquals(2.0, definition2.getTimeToExpiration());
    }

    @Test
    public void shouldAbleToRetrieveSecurityDefinitionByTicker() {
        SecurityDefinition definition = repo.findByTicker("XYZ");
        Assertions.assertEquals("XYZ", definition.getTicker());

        SecurityDefinition definition2 = repo.findByTicker("XYZ-PUT");
        Assertions.assertEquals("XYZ-PUT", definition2.getTicker());
    }

}
