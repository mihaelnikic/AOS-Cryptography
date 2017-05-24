package hr.fer.zemris.nos.symmetric.aes;

import hr.fer.zemris.nos.crypto.CryptoException;
import hr.fer.zemris.nos.files.structures.KeyMap;
import hr.fer.zemris.nos.symmetric.SymmetricKeyGen;
import hr.fer.zemris.nos.utils.ArrayUtils;
import hr.fer.zemris.nos.utils.ConstantUtils;

import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;

/**
 * Created by mihael on 05.05.17..
 */
public class AESKeyGen extends SymmetricKeyGen {

    // Name of system
    private static final String AES_ALGORITHM_NAME = "AES";
    // Name of associative fields in key file
    private static final String METHOD = "Method";
    private static final String DESCRIPTION = "Description";
    private static final String KEY_LENGTH = "Key length";
    private static final String SECRET_KEY = "Secret key";

    @Override
    public PrivateKey generatePrivateKey(int keyLength) {
        try {
            AESKeyDescription keyDescription = AESKeyDescription.getDescriptionForKeySize(keyLength);
            KeyGenerator keyGen = KeyGenerator.getInstance(AES_ALGORITHM_NAME);
            keyGen.init(keyDescription.getKeySize() * ConstantUtils.ONE_BYTE_TO_BITS);
            String hex = ArrayUtils.bytesToHex(keyGen.generateKey().getEncoded());

            return new AESKey(ArrayUtils.hexStringToIntegerArray(hex), keyDescription);
        } catch (IllegalArgumentException | NoSuchAlgorithmException e) {
            throw new CryptoException(e);
        }
    }

    @Override
    public int getPrivateKeySize(PrivateKey privateKey) {
        return ((AESKey) privateKey).getAesKeyDescription().getKeySize() * ConstantUtils.ONE_BYTE_TO_BITS;
    }

    @Override
    protected PrivateKey loadPrivateKeyFromMap(KeyMap keyMap) {
        AESKeyDescription keyDesc = AESKeyDescription.getDescriptionForKeySize(Integer.valueOf(keyMap.get(KEY_LENGTH), ConstantUtils.HEX_BASE_VALUE) / ConstantUtils.ONE_BYTE_TO_BITS);
        return new AESKey(ArrayUtils.hexStringToIntegerArray(keyMap.get(SECRET_KEY)), keyDesc);
    }

    @Override
    protected KeyMap storePrivateKeyWithMap(PrivateKey privateKey) {
        KeyMap keyMap = new KeyMap();

        keyMap.put(DESCRIPTION, SECRET_KEY);
        keyMap.put(METHOD, AES_ALGORITHM_NAME);

        int hexKeySize = ((AESKey) privateKey).getAesKeyDescription().getKeySize();
        keyMap.put(KEY_LENGTH, String.format(ConstantUtils.FOUR_SPACE_HEX_FORMAT, (hexKeySize * ConstantUtils.ONE_BYTE_TO_BITS) & 0xFFFF));
        keyMap.put(SECRET_KEY, privateKey.getFormat());

        return keyMap;
    }

    @Override
    public Key createKeyFromBytes(byte[] k) {
        int[] intKey = ArrayUtils.toIntArray(k);
        return new AESKey(intKey, AESKeyDescription.getDescriptionForKeySize(intKey.length));
    }
}
