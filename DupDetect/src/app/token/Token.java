package app.token;

import java.util.Objects;

public class Token implements Comparable<Token> {
    public enum Type {
        SELLER,
        SPEC,
        SHORT,
        WORD,
        KEYWORD,
        OTHER
    }

    private String value;
    private Type type;

    public Token(String value, Type type) {
        this.value = value;
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public Type getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return Objects.equals(value, token.value) && type == token.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, type);
    }

    @Override
    public int compareTo(Token other) {
        return this.value.compareTo(other.value);
    }
}
