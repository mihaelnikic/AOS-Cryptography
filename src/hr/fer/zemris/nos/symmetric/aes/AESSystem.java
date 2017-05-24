package hr.fer.zemris.nos.symmetric.aes;

import hr.fer.zemris.nos.files.writer.EncryptedFileGen;
import hr.fer.zemris.nos.symmetric.SymmetricCryptoSystem;
import hr.fer.zemris.nos.utils.ConstantUtils;
import hr.fer.zemris.nos.utils.Pair;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

/**
 * Created by mihael on 05.05.17..
 */
public class AESSystem extends SymmetricCryptoSystem {

    public AESSystem() {
        super(new AES(), new AESKeyGen());
    }

    @Override
    public String getName() {
        return AES.AES_ALGORITHM_NAME;
    }

    @Override
    public void storeFile(String file, String inputFileName, PublicKey publicKey, PrivateKey privateKey, byte[] encrypted, Object ... ignored) throws IOException {
        EncryptedFileGen encryptedFileGen = new EncryptedFileGen();
        int hexKeySize = ((AESKey) privateKey).getAesKeyDescription().getKeySize();
        String len = String.format(ConstantUtils.FOUR_SPACE_HEX_FORMAT, (hexKeySize * ConstantUtils.ONE_BYTE_TO_BITS) & 0xFFFF);
        encryptedFileGen.storeFile(file, inputFileName, "Encrypted file", new String[] { AES.AES_ALGORITHM_NAME }, new String[] { len }, new Pair<>("Data",
                Base64.getEncoder().encodeToString(encrypted)));
    }

    @Override
    public byte[] loadEncryptedData(String file) throws IOException {
        return Base64.getDecoder().decode(EncryptedFileGen.loadFile(file)[0]);
    }

}
