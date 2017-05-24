package hr.fer.zemris.nos.symmetric;

import hr.fer.zemris.nos.keygen.AbstractKeyGenerator;

import java.security.PublicKey;

/**
 * Created by mihael on 05.05.17..
 */
public abstract class SymmetricKeyGen extends AbstractKeyGenerator {

    @Override
    public int getPublicKeySize(PublicKey publicKey) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasPublicKey() {
        return false;
    }
}
