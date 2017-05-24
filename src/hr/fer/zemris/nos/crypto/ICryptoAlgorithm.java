package hr.fer.zemris.nos.crypto;

import hr.fer.zemris.nos.files.reader.KeyFileReader;

import java.io.IOException;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Created by mihael on 04.05.17..
 */
public interface ICryptoAlgorithm {

    byte[] encrypt(byte[] base64Encoded, Key encryptionKey);
    byte[] decrypt(byte[] encryptedData, Key decryptionKey);

 //   PrivateKey getPrivateKey();
 //   void setPrivateKey(PrivateKey key);
}
