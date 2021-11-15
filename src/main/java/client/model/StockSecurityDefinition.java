package client.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Stock")
public class StockSecurityDefinition extends SecurityDefinition {

}
