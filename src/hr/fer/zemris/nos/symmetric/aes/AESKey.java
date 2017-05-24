package hr.fer.zemris.nos.symmetric.aes;

import hr.fer.zemris.nos.utils.ArrayUtils;

import java.security.Key;
import java.security.PrivateKey;

/**
 * Created by mihael on 03.05.17..
 */
public class AESKey extends AESBlock implements PrivateKey {

    private AESKeyDescription aesKeyDescription;

    public AESKey(int[] intInput, AESKeyDescription keyDescription) {
        super(intInput, keyDescription.getKeyBlockSize());
        this.aesKeyDescription = keyDescription;
    }

    public AESKeyDescription getAesKeyDescription() {
        return aesKeyDescription;
    }

    @Override
    public String getAlgorithm() {
        return aesKeyDescription.name();
    }

    @Override
    public String getFormat() {
        return this.toString();
    }

    @Override
    public byte[] getEncoded() {
        return this.getByteValue();
    }
}
