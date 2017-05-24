package hr.fer.zemris.nos.asymmetric.rsa;

import hr.fer.zemris.nos.asymmetric.AsymmetricCryptoAlgorithm;
import hr.fer.zemris.nos.asymmetric.AsymmetricCryptoSystem;
import hr.fer.zemris.nos.asymmetric.AsymmetricKeyGenerator;
import hr.fer.zemris.nos.crypto.CryptoException;
import hr.fer.zemris.nos.crypto.ICryptoAlgorithm;
import hr.fer.zemris.nos.files.writer.EncryptedFileGen;
import hr.fer.zemris.nos.keygen.IKeyGenerator;
import hr.fer.zemris.nos.system.ICryptoSystem;
import hr.fer.zemris.nos.utils.ConstantUtils;
import hr.fer.zemris.nos.utils.Pair;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

/**
 * Created by mihael on 05.05.17..
 */
public class RSASystem extends AsymmetricCryptoSystem {

    public RSASystem() {
        super(new RSA(), new RSAKeyGen());
    }

    @Override
    public String getName() {
        return RSA.RSA_ALGORITHM_NAME;
    }

    @Override
    public void storeFile(String file, String inputFileName, PublicKey publicKey, PrivateKey privateKey, byte[] encrypted, Object ... ignored)
            throws IOException {
        try {
            EncryptedFileGen encryptedFileGen = new EncryptedFileGen();
            String publicKeySize = Integer.toString(KeyFactory.getInstance(RSA.RSA_ALGORITHM_NAME).getKeySpec(publicKey, RSAPublicKeySpec.class)
                    .getModulus().bitLength(), ConstantUtils.HEX_BASE_VALUE);
            String privateKeySize = Integer.toString(KeyFactory.getInstance(RSA.RSA_ALGORITHM_NAME).getKeySpec(privateKey, RSAPrivateKeySpec.class)
                    .getModulus().bitLength(), ConstantUtils.HEX_BASE_VALUE);
            encryptedFileGen.storeFile(file, inputFileName, "Encrypted file", new String[]{"RSA"}, new String[]{publicKeySize, privateKeySize},
                    new Pair<>("Data", Base64.getEncoder().encodeToString(encrypted)));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new CryptoException(e);
        }
    }

    @Override
    public byte[] loadEncryptedData(String file) throws IOException {
        return Base64.getDecoder().decode(EncryptedFileGen.loadFile(file)[0]);
    }
}
