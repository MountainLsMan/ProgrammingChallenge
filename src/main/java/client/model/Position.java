package client.model;

import java.util.Objects;

public class Position {

    private String symbol;
    private long size;

    public Position() {
    }

    public Position(String symbol, long size) {
        this.symbol = symbol;
        this.size = size;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return size == position.size && Objects.equals(symbol, position.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, size);
    }

}