package hr.fer.zemris.nos.signature;


import java.io.IOException;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Created by mihael on 10.05.17..
 */
public interface ISignature {

    byte[] createDigest(byte[] input, Key privateKey);
    boolean checkDigest(byte[] original, byte[] encryptedSignature, Key publicKey);
}
