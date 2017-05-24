package hr.fer.zemris.nos.system;

import hr.fer.zemris.nos.crypto.ICryptoAlgorithm;
import hr.fer.zemris.nos.keygen.IKeyGenerator;
import hr.fer.zemris.nos.signature.ISignature;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.rmi.UnexpectedException;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Created by mihael on 05.05.17..
 */
public interface ICryptoSystem {

    ICryptoAlgorithm getCryptoAlgorithm();
    IKeyGenerator getKeyGenerator();
    ISignature getSignatureChecker();

    String getName();
    void storeFile(String file, String inputFileName, PublicKey publicKey,
                   PrivateKey privateKey, byte[] encrypted, Object ... flags) throws IOException;
    byte[] loadEncryptedData(String file) throws IOException;
    default byte[] loadAddSig(String file) throws IOException {
        return null;
    }
}
