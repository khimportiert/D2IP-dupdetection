package app.model;

import app.token.Token;

import java.util.TreeSet;

public interface ModelEntity {
    int getId();
    String toCSV();
    String[] getFields();
    int compareTo(ModelEntity b);
    String getTitle();

    TreeSet<Token> getTokens();
}
