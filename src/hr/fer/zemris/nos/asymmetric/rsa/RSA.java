package hr.fer.zemris.nos.asymmetric.rsa;

import hr.fer.zemris.nos.asymmetric.AsymmetricCryptoAlgorithm;
import hr.fer.zemris.nos.crypto.CryptoException;
import hr.fer.zemris.nos.utils.ArrayUtils;
import hr.fer.zemris.nos.utils.ConstantUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;


/**
 * Created by mihael on 30.04.17..
 */
public class RSA extends AsymmetricCryptoAlgorithm {

    // Name of system
    static final String RSA_ALGORITHM_NAME = "RSA";
    private static final int MESSAGE_SIZE = 11;


    @Override
    public byte[] encrypt(byte[] base64Encoded, Key publicKey) {
        try {
            int keySize = getKeySize(publicKey);

            int blockSize = keySize / ConstantUtils.ONE_BYTE_TO_BITS - MESSAGE_SIZE;
            byte[][] data = ArrayUtils.divideByteArrayWithKeepingSize(base64Encoded, blockSize);

            for (int i = 0; i < data.length; i++) {
                data[i] = encryptBlock(data[i], publicKey);
            }

            return ArrayUtils.mergeByteArrays(data);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new CryptoException(e);
        }
    }

    public byte[] encryptBlock(byte[] base64EncodedBlock, Key publicKey) {
        byte[] encryptedData;
        try {

            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM_NAME);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            encryptedData = cipher.doFinal(base64EncodedBlock);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
                | BadPaddingException | IllegalBlockSizeException e) {
            throw new CryptoException(e);
        }
        return encryptedData;
    }

    @Override
    public byte[] decrypt(byte[] encryptedData, Key privateKey) {
        try {
            int keySize = getKeySize(privateKey);

            int blockSize = keySize / ConstantUtils.ONE_BYTE_TO_BITS;
            byte[][] data = ArrayUtils.divideByteArrayWithKeepingSize(encryptedData, blockSize);

            for (int i = 0; i < data.length; i++) {
                data[i] = decryptBlock(data[i], privateKey);
            }

            return ArrayUtils.mergeByteArrays(data);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new CryptoException(e);
        }
    }


    public byte[] decryptBlock(byte[] encryptedBlock, Key privateKey) {
        byte[] decryptedData;
        try {
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM_NAME);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            decryptedData = cipher.doFinal(encryptedBlock);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException
                | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            throw new CryptoException(e);
        }
        return decryptedData;
    }


    private static int getKeySize(Key key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        int keySize;
        if(key instanceof PublicKey) {
            keySize = KeyFactory.getInstance(RSA_ALGORITHM_NAME).getKeySpec(key, RSAPublicKeySpec.class)
                    .getModulus().bitLength();
        } else {
            keySize = KeyFactory.getInstance(RSA_ALGORITHM_NAME).getKeySpec(key, RSAPrivateKeySpec.class)
                    .getModulus().bitLength();
        }

        return keySize;
    }

}
