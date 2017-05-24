package hr.fer.zemris.nos.keygen;

import hr.fer.zemris.nos.files.reader.KeyFileReader;
import hr.fer.zemris.nos.utils.Pair;

import java.io.IOException;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Created by mihael on 05.05.17..
 */
public interface IKeyGenerator {

    PrivateKey generatePrivateKey(int keyLength);
    void storePrivateKey(PrivateKey privateKey, String privateKeyPath) throws IOException;
    PrivateKey loadPrivateKey(String privateKeyPath) throws IOException;
    default Pair<PrivateKey, PublicKey> generateKeys(int keyLength) {
        throw new UnsupportedOperationException();
    }

    default void storePublicKey(PublicKey publicKey, String publicKeyPath) throws IOException {
        throw new UnsupportedOperationException();
    }

    default PublicKey loadPublicKey(String publicKeyPath) throws IOException {
        throw new UnsupportedOperationException();
    }

    int getPublicKeySize(PublicKey publicKey);

    int getPrivateKeySize(PrivateKey privateKey);

    default Key createKeyFromBytes(byte[] k) {
        throw new UnsupportedOperationException();
    }

    boolean hasPublicKey();

}
