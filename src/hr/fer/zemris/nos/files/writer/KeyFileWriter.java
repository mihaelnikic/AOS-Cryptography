package hr.fer.zemris.nos.files.writer;

import hr.fer.zemris.nos.files.structures.KeyMap;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * Created by mihael on 30.04.17..
 */
public final class KeyFileWriter {

    private KeyFileWriter() {}

    private static final String HEADER = "---BEGIN OS2 CRYPTO DATA---";
    private static final String FOOTER = "---END OS2 CRYPTO DATA---";

    private static final String FOUR_WHITESPACES = "    ";
    private static final String COLON = ":";
    private static final String NEW_LINE = "\n";

    public static void write(String file, KeyMap keyMap) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(HEADER + NEW_LINE + NEW_LINE);
        for (Map.Entry<String, String> entry: keyMap.entrySet()) {
            writer.write(entry.getKey() + COLON + NEW_LINE);
            writer.write(FOUR_WHITESPACES + entry.getValue() + NEW_LINE + NEW_LINE);
        }
        writer.write(FOOTER);
        writer.close();
    }

}
