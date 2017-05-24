package hr.fer.zemris.nos.signature;

import hr.fer.zemris.nos.asymmetric.AsymmetricCryptoSystem;
import hr.fer.zemris.nos.crypto.ICryptoAlgorithm;
import hr.fer.zemris.nos.files.reader.KeyFileReader;
import hr.fer.zemris.nos.files.structures.KeyMap;
import hr.fer.zemris.nos.files.writer.EncryptedFileGen;
import hr.fer.zemris.nos.keygen.IKeyGenerator;
import hr.fer.zemris.nos.system.ICryptoSystem;
import hr.fer.zemris.nos.utils.ConstantUtils;
import hr.fer.zemris.nos.utils.Pair;

import java.io.IOException;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Created by mihael on 10.05.17..
 */
public class SignatureSystem implements ICryptoSystem {

    private IKeyGenerator keyGenerator;
    private AsymmetricCryptoSystem keyCryptoSystem;
    private Signature signature;

    private static final int DEFAULT_ASYMMETRIC_KEY_SIZE = 2048;


    public SignatureSystem(AsymmetricCryptoSystem asymmetricCryptoSystem, int asymmetricKeySize, String digestAlgorithm) {
        this.signature = new Signature(asymmetricCryptoSystem, asymmetricKeySize, digestAlgorithm);
        this.keyCryptoSystem = asymmetricCryptoSystem;
        this.keyGenerator = asymmetricCryptoSystem.getKeyGenerator();
    }

    public SignatureSystem(AsymmetricCryptoSystem asymmetricCryptoSystem, int asymmetricKeySize) {
        this(asymmetricCryptoSystem, asymmetricKeySize, Digest.SHA_256);
    }

    public SignatureSystem(AsymmetricCryptoSystem asymmetricCryptoSystem) {
        this(asymmetricCryptoSystem, DEFAULT_ASYMMETRIC_KEY_SIZE, Digest.SHA_256);
    }


    @Override
    public ICryptoAlgorithm getCryptoAlgorithm() {
        return null;
    }

    @Override
    public IKeyGenerator getKeyGenerator() {
        return keyGenerator;
    }

    @Override
    public ISignature getSignatureChecker() {
        return signature;
    }

    @Override
    public String getName() {
        return Signature.SIGNATURE_ALGORITHM_NAME;
    }

    @Override
    public void storeFile(String file, String inputFileName, PublicKey publicKey, PrivateKey privateKey, byte[] encrypted, Object... flags) throws IOException {
        EncryptedFileGen encryptedFileGen = new EncryptedFileGen();
        String publicKeySize = Integer.toHexString(keyCryptoSystem.getKeyGenerator().getPublicKeySize(publicKey));
        String privateKeySize = Integer.toHexString(keyCryptoSystem.getKeyGenerator().getPrivateKeySize(privateKey));
        encryptedFileGen.storeFile(file, inputFileName, "Signature", new String[] {
                        keyCryptoSystem.getName() }, new String[] { publicKeySize, privateKeySize },
                new Pair<>("Signature", new BigInteger(encrypted).toString(ConstantUtils.HEX_BASE_VALUE)));
    }

    @Override
    public byte[] loadEncryptedData(String file) throws IOException {
        KeyMap keyMap = KeyFileReader.read(file);
        return new BigInteger(keyMap.get("Signature"), ConstantUtils.HEX_BASE_VALUE).toByteArray();
    }
}
