package hr.fer.zemris.nos.files.reader.parser;

import hr.fer.zemris.nos.files.structures.KeyMap;

import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Created by mihael on 28.04.17..
 */
public enum KeyFileParserState {

    SUCCEEDING(".*",null, null, (tm, m, k) -> {}),
    MAIN("---END OS2 CRYPTO DATA---|.+(?=:)|    (.+\\n)+|:|.+", "---END OS2 CRYPTO DATA---", SUCCEEDING, KeyFileParserState::mainAction),
    PRECEDING("---BEGIN OS2 CRYPTO DATA---|.+", "---BEGIN OS2 CRYPTO DATA---", MAIN, (tm, m, k) -> {});

    private static void mainAction(HashMap<String, String> taskMap, String token, KeyMap keyMap) {
        if(!taskMap.containsKey("currentElement")) {
            taskMap.put("currentElement", token);
        } else if(!taskMap.containsKey("hasColon")) {
            if(!token.equals(":")) {
                throw new KeyFileParserException("Greška prilikom parsiranja, nedostaje znak \":\" između key:value entry-a");
            }
            taskMap.put("hasColon", null);
        } else {
            String currentValue = token.replaceAll("\\s++", "").trim();
            keyMap.put(taskMap.get("currentElement"), currentValue);
            taskMap.remove("currentElement");
            taskMap.remove("hasColon");
        }
    }

    Pattern pattern;
    String nextStateToken;
    KeyFileParserState nextState;
    KeyFileAction action;

    KeyFileParserState(String pattern, String nextStateToken, KeyFileParserState nextState, KeyFileAction action) {
        this.pattern = Pattern.compile(pattern);
        this.nextStateToken = nextStateToken;
        this.nextState = nextState;
        this.action = action;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public String getNextStateToken() {
        return nextStateToken;
    }

    public KeyFileParserState getNextState() {
        return nextState;
    }

    public void performAction(HashMap<String, String> taskMap, String token, KeyMap keyMap) {
        action.doTask(taskMap, token, keyMap);
    }
}
