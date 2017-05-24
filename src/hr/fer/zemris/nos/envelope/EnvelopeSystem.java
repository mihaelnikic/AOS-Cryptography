package hr.fer.zemris.nos.envelope;

import hr.fer.zemris.nos.asymmetric.AsymmetricCryptoSystem;
import hr.fer.zemris.nos.crypto.ICryptoAlgorithm;
import hr.fer.zemris.nos.files.reader.KeyFileReader;
import hr.fer.zemris.nos.files.structures.KeyMap;
import hr.fer.zemris.nos.files.writer.EncryptedFileGen;
import hr.fer.zemris.nos.keygen.IKeyGenerator;
import hr.fer.zemris.nos.signature.ISignature;
import hr.fer.zemris.nos.symmetric.SymmetricCryptoSystem;
import hr.fer.zemris.nos.system.ICryptoSystem;
import hr.fer.zemris.nos.utils.ArrayUtils;
import hr.fer.zemris.nos.utils.ConstantUtils;
import hr.fer.zemris.nos.utils.Pair;

import java.io.IOException;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

/**
 * Created by mihael on 09.05.17..
 */
public class EnvelopeSystem implements ICryptoSystem {

    private IKeyGenerator keyGenerator;
    private SymmetricCryptoSystem textCryptoSystem;
    private AsymmetricCryptoSystem keyCryptoSystem;
    private Envelope envelope;

    private static final int DEFAULT_ASYMMETRIC_KEY_SIZE = 2048;

    public EnvelopeSystem(SymmetricCryptoSystem textCryptoSystem, AsymmetricCryptoSystem keyCryptoSystem, int asymmetricKeySize) {
        this.envelope = new Envelope(textCryptoSystem, keyCryptoSystem, asymmetricKeySize);
        this.textCryptoSystem = textCryptoSystem;
        this.keyCryptoSystem = keyCryptoSystem;
        this.keyGenerator = keyCryptoSystem.getKeyGenerator();
    }

    public EnvelopeSystem(SymmetricCryptoSystem textCryptoSystem, AsymmetricCryptoSystem keyCryptoSystem) {
        this(textCryptoSystem, keyCryptoSystem, DEFAULT_ASYMMETRIC_KEY_SIZE);
    }


    @Override
    public ICryptoAlgorithm getCryptoAlgorithm() {
        return envelope;
    }

    @Override
    public IKeyGenerator getKeyGenerator() {
        return keyGenerator;
    }

    @Override
    public ISignature getSignatureChecker() {
        return null;
    }

    @Override
    public String getName() {
        return Envelope.ENVELOPE_ALGORITHM_NAME;
    }

    @Override
    public void storeFile(String file, String inputFileName, PublicKey publicKey, PrivateKey privateKey
            , byte[] encrypted, Object ... ignored) throws IOException {
        EncryptedFileGen encryptedFileGen = new EncryptedFileGen();
        String publicKeySize = Integer.toHexString(keyCryptoSystem.getKeyGenerator().getPublicKeySize(publicKey));
        String privateKeySize = Integer.toHexString(keyCryptoSystem.getKeyGenerator().getPrivateKeySize(privateKey));
        Pair<byte[], byte[]> message = Envelope.mergeByteArrays(encrypted, envelope.getEncryptedKeyBlockSize());
        encryptedFileGen.storeFile(file, inputFileName, "Envelope", new String[]{textCryptoSystem.getName(),
                        keyCryptoSystem.getName()}, new String[]{publicKeySize, privateKeySize},
                new Pair<>("Envelope data", Base64.getEncoder().encodeToString(message.getY())),
                new Pair<>("Envelope crypt key", new BigInteger(message.getX()).toString(ConstantUtils.HEX_BASE_VALUE)));
    }

    @Override
    public byte[] loadEncryptedData(String file) throws IOException {
        KeyMap keyMap = KeyFileReader.read(file);
        byte[] key = new BigInteger(keyMap.get("Envelope crypt key"), ConstantUtils.HEX_BASE_VALUE).toByteArray();
        byte[] data = Base64.getDecoder().decode(keyMap.get("Envelope data"));
        return ArrayUtils.mergeByteArrays(new byte[][]{ key, data });
    }
}
