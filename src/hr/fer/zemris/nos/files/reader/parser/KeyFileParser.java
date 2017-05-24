package hr.fer.zemris.nos.files.reader.parser;

import hr.fer.zemris.nos.files.structures.KeyMap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.regex.Matcher;

/**
 * Created by mihael on 28.04.17..
 */
public class KeyFileParser {

    private KeyMap keyMap = new KeyMap();

    public void parse(String text) throws IOException {
        keyMap.clear();

        KeyFileParserState state = KeyFileParserState.PRECEDING;
        Matcher m = state.getPattern().matcher(text);
        HashMap<String, String> taskMap = new HashMap<>();

        while (m.find()) {
            String token = m.group();
            if(token.equals(state.getNextStateToken())) {
                state = state.getNextState();
                m.usePattern(state.getPattern());
            } else {
                state.performAction(taskMap, token, keyMap);
            }
        }
    }


    public KeyMap retrieve() {
        return keyMap;
    }

    public static void main(String[] args) throws IOException {
        KeyFileParser parser = new KeyFileParser();
        parser.parse(String.join("\n", Files.readAllLines(Paths.get("/home/mihael/IdeaProjects/NOS/NOS_LAB_3_4/Kljucevi/DES_example.txt"))));
        parser.retrieve().forEach((k, v) -> System.out.println(k + "=" + v));
    }
}
