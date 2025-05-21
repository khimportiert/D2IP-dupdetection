package app.token;

import app.io.DictionaryTree;
import app.misc.Speech;
import app.model.ModelEntity;
import app.model.Notebook;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Tokenizer {

    private static final String CURRENT_DIR = System.getProperty("user.dir");
    private static String filename = CURRENT_DIR + "/data/tokens.txt";

    public static HashMap<Token.Type, HashSet<Token>> getGroupedTokens(ModelEntity notebook) {
        HashMap<Token.Type, HashSet<Token>> hashMap = new HashMap<>();

        for (Token.Type tokentype : Token.Type.values()) {
            hashMap.put(tokentype, new HashSet<>());
        }

        for (Token token : notebook.getTokens()) {
            hashMap.get(token.getType()).add(token);
        }

        return hashMap;
    }


    public static TreeSet<Token> tokenizeStorageDevice(ModelEntity notebook) {
        TreeSet<Token> tokens = new TreeSet<>();

        String str = notebook.getTitle().toLowerCase()
                .replaceAll("[\"()?]", " ")
                .replaceAll("\\s+", " ")
                .trim();

        Pattern processorPattern = Pattern.compile("\\bi[3579][- ]\\d{3,5}[a-z]{1,3}\\b");
        str = extractTokens(processorPattern, str, Token.Type.SPEC, tokens);

        Pattern iPattern = Pattern.compile("i[3579]\\s");
        str = extractTokens(iPattern, str, Token.Type.SPEC, tokens);

        Pattern GbOrHzPattern = Pattern.compile("\\b\\d{1,4}(\\.\\d{1})?\\s?(kb|mb|gb|tb|mhz|ghz)\\b");
        str = extractTokens(GbOrHzPattern, str, Token.Type.SPEC, tokens);

        Pattern sellerPattern = Pattern.compile("(\\b((www\\.)?[a-z0-9\\-]+\\.[a-z]{2,4})\\b|\\bebay\\b)");
        str = extractTokens(sellerPattern, str, Token.Type.SELLER, tokens);

        for (String s : str.split(" ")) {
            if (s.equals("|"))
                continue;
            else if (s.length() < 4) {
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

        return tokens;
    }

    private static String extractTokens(Pattern pattern, String input, Token.Type type, Set<Token> tokens) {
        Matcher matcher = pattern.matcher(input);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            String match = matcher.group();
            tokens.add(new Token(match, type));
            matcher.appendReplacement(sb, "");
        }
        matcher.appendTail(sb);
        return sb.toString().trim();
    }

    public static void writeFileTest(List<ModelEntity> items) {

        try (FileWriter writer = new FileWriter(filename)) {

            boolean even = false;

            for (ModelEntity item : items) {
                HashMap<Token.Type, HashSet<Token>> groupedTokens = getGroupedTokens(item);

                StringBuilder str = new StringBuilder();

                List<String> tpr = groupedTokens.get(Token.Type.SPEC).stream().map(Token::getValue).toList();
                List<String> tsh = groupedTokens.get(Token.Type.SHORT).stream().map(Token::getValue).toList();
                List<String> tkw = groupedTokens.get(Token.Type.KEYWORD).stream().map(Token::getValue).toList();
                List<String> twd = groupedTokens.get(Token.Type.WORD).stream().map(Token::getValue).toList();
                List<String> tot = groupedTokens.get(Token.Type.OTHER).stream().map(Token::getValue).toList();
                List<String> tsl = groupedTokens.get(Token.Type.SELLER).stream().map(Token::getValue).toList();

                String tprStr = tpr.stream().filter(s -> !s.isEmpty()).collect(Collectors.joining(" | "));
                String tslStr = tsl.stream().filter(s -> !s.isEmpty()).collect(Collectors.joining(" | "));
                String tshStr = tsh.stream().filter(s -> !s.isEmpty()).collect(Collectors.joining(" | "));
                String tkwStr = tkw.stream().filter(s -> !s.isEmpty()).collect(Collectors.joining(" | "));
                String twdStr = twd.stream().filter(s -> !s.isEmpty()).collect(Collectors.joining(" | "));
                String totStr = tot.stream().filter(s -> !s.isEmpty()).collect(Collectors.joining(" | "));

                str
                        .append("KEYWORDS: ").append(tkwStr)
                        .append("\nWORDS: ").append(twdStr)
                        .append("\nSELLER: ").append(tslStr)
                        .append("\nSPECS: ").append(tprStr)
                        .append("\nSHORT: ").append(tshStr)
                        .append("\nOTHER: ").append(totStr).append("\n\n");

                writer.append(item.getId()+": ").append(item.getTitle()).append("\n");
                writer.append(str.toString());

                if (even)
                    writer.append("\n");

                even = !even;
            }
        } catch (IOException e) {
            System.err.println(e);
            System.exit(1);
        }
    }
}
