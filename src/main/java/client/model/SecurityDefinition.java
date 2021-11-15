package client.model;

import client.model.enums.SecurityType;

import javax.persistence.*;
import java.util.Comparator;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "TYPE", discriminatorType = DiscriminatorType.STRING)
public abstract class SecurityDefinition implements Comparable<SecurityDefinition> {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(unique = true)
    private String ticker;

    @Column(insertable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private SecurityType type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public SecurityType getType() {
        return type;
    }

    public void setType(SecurityType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SecurityDefinition that = (SecurityDefinition) o;
        return Objects.equals(id, that.id) && Objects.equals(ticker, that.ticker);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ticker);
    }

    @Override
    public int compareTo(SecurityDefinition o) {
        return Comparator.comparing((SecurityDefinition def) -> def.ticker).compare(this, o);
    }

}