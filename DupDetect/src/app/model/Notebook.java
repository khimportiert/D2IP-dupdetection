package app.model;

import app.io.DictionaryTree;
import app.misc.Speech;
import app.token.Token;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a notebook entity that can be loaded from a CSV file.
 * <p>
 * This class must define at least all fields that appear in the CSV header.
 * These required fields must:
 * <ul>
 *     <li>Exactly match the CSV column names (case-sensitive)</li>
 *     <li>Be of type {@code String}</li>
 * </ul>
 * Additional fields beyond those in the CSV are allowed.
 */
public class Notebook implements ModelEntity, Comparable<ModelEntity> {
    private String id;
    private String title;
    private ArrayList<Token> tokens = new ArrayList<>();

    public Notebook() {}

    public Notebook(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public ArrayList<Token> getTokens() {
        return tokens;
    }

    public void setTokens(ArrayList<Token> tokens) {
        this.tokens = tokens;
    }

    public int getId() {
        return Integer.parseInt(id);
    }

    public String getTitle() {
        return title;
    }

    public void tokenize() {
        String str = title.toLowerCase().replaceAll("[\"()?\\s+]", " ").trim();
        Pattern pattern = Pattern.compile("i[3579][- ]\\d{4,5}[a-z]*");
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            String processor = matcher.group(0);
            tokens.add(new Token(processor, Token.Type.PROCESSOR));
            str = str.replaceAll(pattern.toString(), "");
        }
        for (String s : str.split(" ")) {
            if (s.equals("|"))
                continue;

            if (s.endsWith("gb")) {
                tokens.add(new Token(s, Token.Type.GB));
            }
            else if (s.length() < 3) {
                tokens.add(new Token(s, Token.Type.SHORT));
            }
            else if (Speech.looksLikeSpeech(s)) {
                if (DictionaryTree.tree.contains(s))
                    tokens.add(new Token(s, Token.Type.WORD));
                else
                    tokens.add(new Token(s, Token.Type.KEYWORD));
            }
            else {
                tokens.add(new Token(s, Token.Type.OTHER));
            }
        }
    }

    @Override
    public String toString() {
        return "Notebook{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                '}';
    }

    public String toCSV() {
        return id + "," + title;
    }

    public String[] getFields() {
        return new String[] { "id", "title" };
    }

    @Override
    public int compareTo(ModelEntity other) {
        return Integer.compare(this.getId(), other.getId());
    }
}
