package app.token;

public class Token {
    public enum Type {
        GB,
        WORD,
        OTHER
    }

    private String token;
    private Type type;

    public Token(String token, Type type) {
        this.token = token;
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public Type getType() {
        return type;
    }
}
