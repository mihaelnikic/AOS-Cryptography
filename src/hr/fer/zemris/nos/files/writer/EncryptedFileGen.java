package hr.fer.zemris.nos.files.writer;


import hr.fer.zemris.nos.files.reader.KeyFileReader;
import hr.fer.zemris.nos.files.structures.KeyMap;
import hr.fer.zemris.nos.utils.Pair;

import java.io.IOException;

/**
 * Created by mihael on 08.05.17..
 */
public class EncryptedFileGen {

    // Name of associative fields in key file
    private static final String METHOD = "Method";
    private static final String DESCRIPTION = "Description";
    private static final String FILE_NAME = "File name";
    private static final String KEY_LENGTH = "Key length";
    private static final String PRIVATE_KEY = "Private Key";
    private static final String PUBLIC_KEY = "Public Key";


    public static void storeFile(String file, String inputFileName, String desc, String[] methods, String[] keyLengths, Pair<String, String> ... data) throws IOException {
        KeyMap keyMap = new KeyMap();
        storeFileWithMap(keyMap, inputFileName, desc, methods, keyLengths, data);
        KeyFileWriter.write(file, keyMap);
    }

    public static String[] loadFile(String file) throws IOException {
        KeyMap keyMap = KeyFileReader.read(file);
        return loadFileWithMap(keyMap);
    }


    private static void storeFileWithMap(KeyMap map, String fileName, String desc, String[] methods, String[] keyLengths, Pair<String, String>... data) {
        map.put(DESCRIPTION, desc);
        map.put(METHOD, String.join("\n    ", methods));
        map.put(FILE_NAME, fileName);
        map.put(KEY_LENGTH, String.join("\n    ", keyLengths));
        for (Pair<String, String> keyValue : data) {
            map.put(keyValue.getX(), keyValue.getY());
        }
    }

    private static String[] loadFileWithMap(KeyMap map) {
        return new String[] { map.get("Data")};
    }
}
