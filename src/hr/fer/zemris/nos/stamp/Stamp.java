package hr.fer.zemris.nos.stamp;

import hr.fer.zemris.nos.crypto.AbstractCryptoAlgorithm;
import hr.fer.zemris.nos.envelope.EnvelopeSystem;
import hr.fer.zemris.nos.signature.ISignature;
import hr.fer.zemris.nos.signature.SignatureSystem;
import hr.fer.zemris.nos.utils.Pair;

import java.security.Key;

/**
 * Created by mihael on 10.05.17..
 */
public class Stamp extends AbstractCryptoAlgorithm implements ISignature {

    private static String STAMP_ALGORITHM_NAME = "Stamp";
    private static final int DEFAULT_ASYMMETRIC_KEY_SIZE = 2048;


    private SignatureSystem signature;
    private EnvelopeSystem envelope;
    private int encryptedKeySize;

    public Stamp(SignatureSystem signature, EnvelopeSystem envelope, int asymmetricKeySize) {
        this.signature = signature;
        this.envelope = envelope;
        this.encryptedKeySize = asymmetricKeySize / 8;
    }

    public Stamp(SignatureSystem signature, EnvelopeSystem envelope) {
        this.signature = signature;
        this.envelope = envelope;
        this.encryptedKeySize = DEFAULT_ASYMMETRIC_KEY_SIZE / 8;
    }

    @Override
    public byte[] encrypt(byte[] base64Encoded, Key publicKey) {
        return envelope.getCryptoAlgorithm().encrypt(base64Encoded, publicKey);
    }

    @Override
    public byte[] decrypt(byte[] encryptedData, Key privateKey) {
        return envelope.getCryptoAlgorithm().decrypt(encryptedData, privateKey);
    }


    @Override
    public byte[] createDigest(byte[] input, Key privateKey) {
        return signature.getSignatureChecker().createDigest(input, privateKey);
    }

    @Override
    public boolean checkDigest(byte[] original, byte[] encryptedSignature, Key publicKey) {
        return signature.getSignatureChecker().checkDigest(original, encryptedSignature, publicKey);
    }


    public static Pair<byte[], byte[]> divideByteArrayIntoPairs(byte[] encryptedData, int firstSize) {
        byte[] key = new byte[firstSize];
        byte[] data = new byte[encryptedData.length - firstSize];
        for (int i = 0; i < firstSize; i++) {
            key[i] = encryptedData[i];
        }
        for (int i = firstSize, j = 0; i < encryptedData.length; i++, j++) {
            data[j] = encryptedData[i];
        }
        return Pair.createPair(key, data);
    }

    public int getEncryptedKeySize() {
        return encryptedKeySize;
    }

    //
//    public static void main(String[] args) {
//        RSASystem rsaSystem = new RSASystem();
//        AESSystem aesSystem = new AESSystem();
//
//        Pair<PrivateKey, PublicKey> keyB = ((AsymmetricKeyGenerator) rsaSystem.getKeyGenerator())
//                .generateKeys(1024);
//        Pair<PrivateKey, PublicKey> keyA = ((AsymmetricKeyGenerator) rsaSystem.getKeyGenerator())
//                .generateKeys(1024);
//
//
//        EnvelopeSystem envelope = new EnvelopeSystem(aesSystem, rsaSystem, 1024);
//        SignatureSystemOld signature = new SignatureSystemOld(rsaSystem, 1024);
//        StampOld stamp = new StampOld(signature, envelope, 1024);
//
//        byte[] enc = stamp.encrypt("Dobar dan sinko kako je?".getBytes(), new StampKey(keyA.getX(), keyB.getY()));
//        Pair<byte[], byte[]> stampDecrypted = divideByteArrayIntoPairs(stamp.decrypt(enc, new StampKey(keyB.getX(), keyA.getY())), 1);
//        //provjera dekriptiranja
//        System.out.println(new String(stampDecrypted.getY()));
//        //provjera potpisa
//        System.out.println(stampDecrypted.getX()[0] == 1);
//        //   System.out.println(Digest.checkDigest(
//        //           Digest.createDigest(Digest.SHA_256, Base64.getEncoder().encode("Dobar dan sinko kako je?".getBytes())),
//        //           stampDecrypted.getX()));
//
//        System.out.println();
//
}