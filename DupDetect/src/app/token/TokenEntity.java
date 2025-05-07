package app.token;

import app.model.ModelEntity;
import app.model.Notebook;
import app.model.StorageDevice;

import java.util.*;

public class TokenEntity implements Comparable<TokenEntity> {
    private int id;
    private Set<Token> tokens = new HashSet<>();

    public TokenEntity(ModelEntity notebook) {
        this.id = notebook.getId();

        notebook.tokenize();
        HashMap<Token.Type, HashSet<Token>> tokenMap = Tokenizer.getGroupedTokens(notebook);
        tokens.addAll(tokenMap.get(Token.Type.KEYWORD));
//        tokens.addAll(tokenMap.get(Token.Type.WORD));
//        tokens.addAll(tokenMap.get(Token.Type.SPEC));
//        tokens.addAll(tokenMap.get(Token.Type.SHORT));
//        tokens.addAll(tokenMap.get(Token.Type.SELLER));
        tokens.addAll(tokenMap.get(Token.Type.OTHER));
    }

    public int getId() {
        return id;
    }

    public Set<Token> getTokens() {
        return tokens;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof TokenEntity other)
            return this.compareTo(other) == 0;
        return false;
    }

    @Override
    public int compareTo(TokenEntity other) {

        int sizeCompare = Integer.compare(this.tokens.size(), other.tokens.size());
        if (sizeCompare != 0)
            return sizeCompare;

        List<Token> thisList = new ArrayList<>(this.tokens);
        List<Token> otherList = new ArrayList<>(other.tokens);

        Collections.sort(thisList);
        Collections.sort(otherList);

        for (int i = 0; i < thisList.size(); i++) {
            int cmp = thisList.get(i).getValue().compareTo(otherList.get(i).getValue());
            if (cmp != 0)
                return cmp;
        }

        return 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Token token : tokens) {
            sb.append(token.getValue()).append(" ");
        }
        return sb.toString();
    }
}
