package hr.fer.zemris.nos.keygen;

import hr.fer.zemris.nos.crypto.CryptoException;
import hr.fer.zemris.nos.files.structures.KeyMap;
import hr.fer.zemris.nos.files.reader.KeyFileReader;
import hr.fer.zemris.nos.files.writer.KeyFileWriter;

import java.io.IOException;
import java.security.PrivateKey;

/**
 * Created by mihael on 05.05.17..
 */
public abstract class AbstractKeyGenerator implements IKeyGenerator {

    @Override
    public PrivateKey loadPrivateKey(String privateKeyPath) throws IOException {
        return loadPrivateKeyFromMap(KeyFileReader.read(privateKeyPath));
    }

    protected static void saveMap(String keyPath, KeyMap map) {
        try {
            KeyFileWriter.write(keyPath, map);
        } catch (IOException e) {
            throw new CryptoException(e);
        }
    }

    @Override
    public void storePrivateKey(PrivateKey privateKey, String privateKeyPath) throws IOException {
        saveMap(privateKeyPath, storePrivateKeyWithMap(privateKey));
    }

    protected abstract PrivateKey loadPrivateKeyFromMap(KeyMap keyMap);
    protected abstract KeyMap storePrivateKeyWithMap(PrivateKey privateKey);
}
