package hr.fer.zemris.nos.stamp;

import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Created by mihael on 05.05.17..
 */
public class StampKey implements Key {

    private PrivateKey privateKey;
    private PublicKey publicKey;

    public StampKey(PrivateKey privateKey, PublicKey publicKey) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    @Override
    public String getAlgorithm() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getFormat() {
        throw new UnsupportedOperationException();
    }

    @Override
    public byte[] getEncoded() {
        throw new UnsupportedOperationException();
    }
}
