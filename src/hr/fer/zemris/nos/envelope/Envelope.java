package hr.fer.zemris.nos.envelope;

import hr.fer.zemris.nos.asymmetric.AsymmetricCryptoSystem;
import hr.fer.zemris.nos.crypto.AbstractCryptoAlgorithm;
import hr.fer.zemris.nos.files.writer.EncryptedFileGen;
import hr.fer.zemris.nos.symmetric.SymmetricCryptoSystem;
import hr.fer.zemris.nos.utils.ArrayUtils;
import hr.fer.zemris.nos.utils.ConstantUtils;
import hr.fer.zemris.nos.utils.Pair;

import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.util.Base64;

/**
 * Created by mihael on 05.05.17..
 */
public class Envelope extends AbstractCryptoAlgorithm {

    static final String ENVELOPE_ALGORITHM_NAME = "Envelope";

    private SymmetricCryptoSystem textCryptoSystem;
    private AsymmetricCryptoSystem keyCryptoSystem;
    private int encryptedKeyBlockSize;

    public Envelope(SymmetricCryptoSystem textCryptoSystem, AsymmetricCryptoSystem keyCryptoSystem, int asymmetricKeySize) {
        this.textCryptoSystem = textCryptoSystem;
        this.keyCryptoSystem = keyCryptoSystem;
        this.encryptedKeyBlockSize = asymmetricKeySize / ConstantUtils.ONE_BYTE_TO_BITS;
    }

    private static final int ENVELOPE_PRIVATE_KEY_LEN = 16;

    @Override
    public byte[] encrypt(byte[] base64Encoded, Key publicKey) {
        PrivateKey key = textCryptoSystem.getKeyGenerator().generatePrivateKey(ENVELOPE_PRIVATE_KEY_LEN);
        byte[] c1 = textCryptoSystem.getCryptoAlgorithm().encrypt(base64Encoded, key);
        byte[] c2 = keyCryptoSystem.getCryptoAlgorithm().encrypt(key.getEncoded(), publicKey);
        return ArrayUtils.mergeByteArrays(new byte[][]{ c2,  c1 });
    }

    @Override
    public byte[] decrypt(byte[] encryptedData, Key privateKey) {
        Pair<byte[], byte[]> message = mergeByteArrays(encryptedData, encryptedKeyBlockSize);

        byte[] k = keyCryptoSystem.getCryptoAlgorithm().decrypt(message.getX(), privateKey);
        byte[] p = textCryptoSystem.getCryptoAlgorithm().decrypt(message.getY(), textCryptoSystem.getKeyGenerator().createKeyFromBytes(k));

        return p;
    }



    public static Pair<byte[], byte[]> mergeByteArrays(byte[] encryptedData, int encryptedKeyBlockSize) {
        byte[] key = new byte[encryptedKeyBlockSize];
        byte[] data = new byte[encryptedData.length - encryptedKeyBlockSize];
        for (int i = 0; i < encryptedKeyBlockSize; i++) {
            key[i] = encryptedData[i];
        }
        for (int i = encryptedKeyBlockSize, j = 0; i < encryptedData.length; i++, j++) {
            data[j] = encryptedData[i];
        }
        return Pair.createPair(key, data);
    }

    public int getEncryptedKeyBlockSize() {
        return encryptedKeyBlockSize;
    }

    //    public static void main(String[] args) {
//        AESSystem aesSystem = new AESSystem();
//        RSASystem rsaSystem = new RSASystem();
//
//        Pair<PrivateKey, PublicKey> keys = ((AsymmetricKeyGenerator) rsaSystem.getKeyGenerator())
//                .generateKeys(1024);//RSAKeyGen.DEFAULT_KEY_SIZE);
//
//        Envelope envelope = new Envelope(aesSystem, rsaSystem, 1024);
//        byte[] enc = envelope.encrypt(Base64.getEncoder().encode("Dobar dan sinko kako je?".getBytes()), keys.getY());
//        System.out.println(new String(Base64.getDecoder().decode(envelope.decrypt(enc, keys.getX()))));
//     //   System.out.println(new String(Base64.getEncoder().encode("Dobar dan sinko kako je?".getBytes())));
//    }



}
