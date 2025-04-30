package app.token;

import app.io.CSVReader;
import app.model.Notebook;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Tokenizer {

    private static final String CURRENT_DIR = System.getProperty("user.dir");
    private static String filename = CURRENT_DIR + "/data/tokens.txt";

    public static void writeFileTest(List<Notebook> items) {

        try (FileWriter writer = new FileWriter(filename)) {

            for (Notebook item : items) {
                StringBuilder str = new StringBuilder();

                ArrayList<String> tgb = new ArrayList<>();
                ArrayList<String> tpr = new ArrayList<>();
                ArrayList<String> tsh = new ArrayList<>();
                ArrayList<String> tkw = new ArrayList<>();
                ArrayList<String> twd = new ArrayList<>();
                ArrayList<String> tot = new ArrayList<>();
                for (Token token : item.getTokens()) {
                    if (token.getType() == Token.Type.GB) {
                        tgb.add(token.getValue());
                    }
                    else if (token.getType() == Token.Type.PROCESSOR) {
                        tpr.add(token.getValue());
                    }
                    else if (token.getType() == Token.Type.SHORT) {
                        tsh.add(token.getValue());
                    }
                    else if (token.getType() == Token.Type.KEYWORD) {
                        tkw.add(token.getValue());
                    }
                    else if (token.getType() == Token.Type.WORD) {
                        twd.add(token.getValue());
                    }
                    else {
                        tot.add(token.getValue());
                    }
                }

                String tgbStr = tgb.stream().filter(s -> !s.isEmpty()).collect(Collectors.joining(" | "));
                String tprStr = tpr.stream().filter(s -> !s.isEmpty()).collect(Collectors.joining(" | "));
                String tshStr = tsh.stream().filter(s -> !s.isEmpty()).collect(Collectors.joining(" | "));
                String tkwStr = tkw.stream().filter(s -> !s.isEmpty()).collect(Collectors.joining(" | "));
                String twdStr = twd.stream().filter(s -> !s.isEmpty()).collect(Collectors.joining(" | "));
                String totStr = tot.stream().filter(s -> !s.isEmpty()).collect(Collectors.joining(" | "));
                str.append("KEYWORDS: ").append(tkwStr)
                        .append("\nWORDS: ").append(twdStr)
                        .append("\nPROCESSOR: ").append(tprStr)
                        .append("\nGB: ").append(tgbStr)
                        .append("\nSHORT: ").append(tshStr)
                        .append("\nOTHER: ").append(totStr).append("\n\n");
                writer.append(str.toString());
            }
        } catch (IOException e) {
            System.err.println(e);
            System.exit(1);
        }
    }
}
