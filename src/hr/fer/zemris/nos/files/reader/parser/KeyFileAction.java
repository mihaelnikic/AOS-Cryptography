package hr.fer.zemris.nos.files.reader.parser;

import hr.fer.zemris.nos.files.structures.KeyMap;

import java.util.HashMap;

/**
 * Created by mihael on 30.04.17..
 */
public interface KeyFileAction {

    void doTask(HashMap<String, String> taskStorage, String token, KeyMap keyMap);
}
