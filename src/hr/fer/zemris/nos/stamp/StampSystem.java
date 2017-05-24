package hr.fer.zemris.nos.stamp;

import hr.fer.zemris.nos.crypto.ICryptoAlgorithm;
import hr.fer.zemris.nos.envelope.EnvelopeSystem;
import hr.fer.zemris.nos.files.reader.KeyFileReader;
import hr.fer.zemris.nos.files.structures.KeyMap;
import hr.fer.zemris.nos.keygen.IKeyGenerator;
import hr.fer.zemris.nos.signature.ISignature;
import hr.fer.zemris.nos.signature.SignatureSystem;
import hr.fer.zemris.nos.system.ICryptoSystem;
import hr.fer.zemris.nos.utils.ArrayUtils;
import hr.fer.zemris.nos.utils.ConstantUtils;

import java.io.IOException;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

/**
 * Created by mihael on 09.05.17..
 */
public class StampSystem implements ICryptoSystem {

    private static String STAMP_ALGORITHM_NAME = "Stamp";

    private IKeyGenerator keyGenerator;
    private Stamp stamp;
    private EnvelopeSystem envelopeSystem;
    private SignatureSystem signatureSystem;

    public StampSystem(EnvelopeSystem envelope, SignatureSystem signature) {
        this.stamp = new Stamp(signature, envelope);
        this.envelopeSystem = envelope;
        this.signatureSystem = signature;
        this.keyGenerator = envelope.getKeyGenerator();
    }


    @Override
    public ICryptoAlgorithm getCryptoAlgorithm() {
        return stamp;
    }

    @Override
    public IKeyGenerator getKeyGenerator() {
        return keyGenerator;
    }

    @Override
    public ISignature getSignatureChecker() {
        return stamp;
    }

    @Override
    public String getName() {
        return STAMP_ALGORITHM_NAME;
    }

    @Override
    public void storeFile(String envelopeFile, String inputFileName, PublicKey anaPublicKey, PrivateKey anaPrivateKey
            , byte[] encrypted, Object... flags) throws IOException {
        String signatureFile = (String) flags[0];
        byte[] signatureEncrypted = (byte[]) flags[1];
        PublicKey brankoPublic = (PublicKey) flags[2];
        PrivateKey brankoPrivate = (PrivateKey) flags[3];
        envelopeSystem.storeFile(envelopeFile, inputFileName, brankoPublic, brankoPrivate, encrypted);
        signatureSystem.storeFile(signatureFile, inputFileName, anaPublicKey, anaPrivateKey, signatureEncrypted);
    }

    @Override
    public byte[] loadEncryptedData(String file) throws IOException {
        KeyMap keyMap = KeyFileReader.read(file);
        byte[] key = new BigInteger(keyMap.get("Envelope crypt key"), ConstantUtils.HEX_BASE_VALUE).toByteArray();
        byte[] data = Base64.getDecoder().decode(keyMap.get("Envelope data"));
        return ArrayUtils.mergeByteArrays(new byte[][]{ key, data });
    }

    @Override
    public byte[] loadAddSig(String file) throws IOException {
        KeyMap keyMap = KeyFileReader.read(file);
        return new BigInteger(keyMap.get("Signature"), ConstantUtils.HEX_BASE_VALUE).toByteArray();
    }
}
