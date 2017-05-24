package hr.fer.zemris.nos.signature;

import hr.fer.zemris.nos.asymmetric.AsymmetricCryptoSystem;
import hr.fer.zemris.nos.files.writer.EncryptedFileGen;
import hr.fer.zemris.nos.utils.ConstantUtils;
import hr.fer.zemris.nos.utils.Pair;

import java.io.IOException;
import java.math.BigInteger;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Created by mihael on 10.05.17..
 */
public class Signature implements ISignature {

    static final String SIGNATURE_ALGORITHM_NAME = "Signature";

    private AsymmetricCryptoSystem keyCryptoSystem;
    private int encryptedKeyBlockSize;
    private String digestAlgorithm;

    public Signature(AsymmetricCryptoSystem keyCryptoSystem, int asymmetricKeySize, String digestAlgorithm) {
        this.keyCryptoSystem = keyCryptoSystem;
        this.encryptedKeyBlockSize = asymmetricKeySize / ConstantUtils.ONE_BYTE_TO_BITS;
        this.digestAlgorithm = digestAlgorithm;
    }

    public Signature(AsymmetricCryptoSystem keyCryptoSystem, int asymmetricKeySize) {
        this(keyCryptoSystem, asymmetricKeySize, Digest.SHA_256);
    }

    @Override
    public byte[] createDigest(byte[] input, Key privateKey) {
        byte[] digest = Digest.createDigest(digestAlgorithm, input);
        return keyCryptoSystem.getCryptoAlgorithm().encrypt(digest, privateKey);
    }

    @Override
    public boolean checkDigest(byte[] original, byte[] encryptedSignature, Key publicKey) {
        byte[] decrypted =  keyCryptoSystem.getCryptoAlgorithm().decrypt(encryptedSignature, publicKey);
        return Digest.checkDigest(Digest.createDigest(digestAlgorithm, original), decrypted);
    }

}
