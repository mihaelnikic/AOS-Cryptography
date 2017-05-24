package hr.fer.zemris.nos.asymmetric;

import hr.fer.zemris.nos.files.structures.KeyMap;
import hr.fer.zemris.nos.files.reader.KeyFileReader;
import hr.fer.zemris.nos.keygen.AbstractKeyGenerator;
import hr.fer.zemris.nos.utils.Pair;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Created by mihael on 05.05.17..
 */
public abstract class AsymmetricKeyGenerator extends AbstractKeyGenerator {

    @Override
    public PrivateKey generatePrivateKey(int keyLength) {
        return generateKeys(keyLength).getX();
    }

    @Override
    public abstract Pair<PrivateKey, PublicKey> generateKeys(int keyLength);

    @Override
    public void storePublicKey(PublicKey publicKey, String publicKeyPath) throws IOException {
        saveMap(publicKeyPath, storePublicKeyWithMap(publicKey));
    }

    @Override
    public PublicKey loadPublicKey(String publicKeyPath) throws IOException {
        return loadPublicKeyFromMap(KeyFileReader.read(publicKeyPath));
    }

    protected abstract PublicKey loadPublicKeyFromMap(KeyMap keyMap);
    protected abstract KeyMap storePublicKeyWithMap(PublicKey publicKey);

    @Override
    public boolean hasPublicKey() {
        return true;
    }
}
