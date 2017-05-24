package hr.fer.zemris.nos.signature;

import hr.fer.zemris.nos.crypto.CryptoException;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Created by mihael on 05.05.17..
 */
public final class Digest {

    private Digest() {}

    public static final String SHA_256 = "SHA-256";

    public static byte[] createDigest(String digestAlgorithmName, byte[] input) {
        try {
            MessageDigest md = MessageDigest.getInstance(digestAlgorithmName);

            md.update(input);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new CryptoException(e);
        }
    }

    public static boolean checkDigest(byte[] digest1, byte[] digest2) {
        return Arrays.equals(digest1, digest2);
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        System.out.println(createDigest(SHA_256, "a".getBytes("UTF-8")).length);
    }

}
