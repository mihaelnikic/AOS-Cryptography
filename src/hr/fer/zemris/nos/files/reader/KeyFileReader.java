package hr.fer.zemris.nos.files.reader;

import hr.fer.zemris.nos.files.reader.parser.KeyFileParser;
import hr.fer.zemris.nos.files.structures.KeyMap;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by mihael on 30.04.17..
 */
public final class KeyFileReader {

    private static final String NEW_LINE = "\n";

    private KeyFileReader() {}

    public static KeyMap read(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        StringBuilder textBuilder = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            textBuilder.append(line).append(NEW_LINE);
        }

        KeyFileParser parser = new KeyFileParser();
        parser.parse(textBuilder.toString());

        return parser.retrieve();
    }
}
