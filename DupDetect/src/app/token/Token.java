package app.token;

public class Token {
    public enum Type {
        GB,
        PROCESSOR,
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
}
