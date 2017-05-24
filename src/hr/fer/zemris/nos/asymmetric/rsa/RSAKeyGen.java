package hr.fer.zemris.nos.asymmetric.rsa;

import hr.fer.zemris.nos.asymmetric.AsymmetricKeyGenerator;
import hr.fer.zemris.nos.crypto.CryptoException;
import hr.fer.zemris.nos.files.structures.KeyMap;
import hr.fer.zemris.nos.utils.ConstantUtils;
import hr.fer.zemris.nos.utils.Pair;

import java.math.BigInteger;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

/**
 * Created by mihael on 05.05.17..
 */
public class RSAKeyGen extends AsymmetricKeyGenerator {

    // Name of system
    public static final String RSA_ALGORITHM_NAME = "RSA";
    public static final int DEFAULT_KEY_SIZE = 2048;
    // Name of associative fields in key file
    private static final String MODULUS = "Modulus";
    private static final String PRIVATE_KEY_EXPONENT = "Private exponent";
    private static final String PUBLIC_KEY_EXPONENT = "Public exponent";
    private static final String METHOD = "Method";
    private static final String DESCRIPTION = "Description";
    private static final String KEY_LENGTH = "Key length";
    private static final String PRIVATE_KEY = "Private Key";
    private static final String PUBLIC_KEY = "Public Key";

    @Override
    public Pair<PrivateKey, PublicKey> generateKeys(int keyLength) {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA_ALGORITHM_NAME);
            keyPairGenerator.initialize(keyLength);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            return Pair.createPair(privateKey, publicKey);
        } catch (Exception e) {
            throw new CryptoException(e);
        }
    }

    @Override
    public int getPublicKeySize(PublicKey publicKey) {
        try {
            return KeyFactory.getInstance(RSA_ALGORITHM_NAME).getKeySpec(publicKey, RSAPublicKeySpec.class)
                    .getModulus().bitLength();
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new CryptoException(e);
        }
    }

    @Override
    public int getPrivateKeySize(PrivateKey privateKey) {
        try {
            return KeyFactory.getInstance(RSA_ALGORITHM_NAME).getKeySpec(privateKey, RSAPrivateKeySpec.class)
                    .getModulus().bitLength();
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new CryptoException(e);
        }
    }

    private static Key loadSpecificKey(KeyMap map, boolean isPrivate) {
        BigInteger exponent = new BigInteger(isPrivate? map.get(PRIVATE_KEY_EXPONENT) : map.get(PUBLIC_KEY_EXPONENT),
                ConstantUtils.HEX_BASE_VALUE);
        BigInteger modulus = new BigInteger(map.get(MODULUS), ConstantUtils.HEX_BASE_VALUE);

        Key key;
        KeyFactory fact;
        try {
            fact = KeyFactory.getInstance(RSA_ALGORITHM_NAME);

            if(!isPrivate) {
                RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(modulus, exponent);
                key = fact.generatePublic(rsaPublicKeySpec);
            } else {
                RSAPrivateKeySpec rsaPrivateKeySpec = new RSAPrivateKeySpec(modulus, exponent);
                key = fact.generatePrivate(rsaPrivateKeySpec);
            }
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new CryptoException(e);
        }

        return key;
    }

    @Override
    protected PublicKey loadPublicKeyFromMap(KeyMap keyMap) {
        return (PublicKey) loadSpecificKey(keyMap, false);
    }

    @Override
    protected KeyMap storePublicKeyWithMap(PublicKey publicKey) {
        try {
            KeyMap keyMap = new KeyMap();
            keyMap.put(METHOD, RSA_ALGORITHM_NAME);

            KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM_NAME);
            RSAPublicKeySpec rsaPubKeySpec = keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
            keyMap.put(KEY_LENGTH, Integer.toString(rsaPubKeySpec.getModulus().bitLength(), ConstantUtils.HEX_BASE_VALUE));

            generateSpecificKeyParts(keyMap, PUBLIC_KEY, PUBLIC_KEY_EXPONENT, rsaPubKeySpec.getPublicExponent(), rsaPubKeySpec.getModulus());

            return keyMap;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new CryptoException(e);
        }
    }

    @Override
    protected PrivateKey loadPrivateKeyFromMap(KeyMap keyMap) {
        return (PrivateKey) loadSpecificKey(keyMap, true);
    }

    @Override
    protected KeyMap storePrivateKeyWithMap(PrivateKey privateKey) {
        try {
            KeyMap keyMap = new KeyMap();
            keyMap.put(METHOD, RSA_ALGORITHM_NAME);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM_NAME);

            RSAPrivateKeySpec rsaPrivateKeySpec = keyFactory.getKeySpec(privateKey, RSAPrivateKeySpec.class);

            keyMap.put(KEY_LENGTH, Integer.toString(rsaPrivateKeySpec.getModulus().bitLength(), ConstantUtils.HEX_BASE_VALUE));

            generateSpecificKeyParts(keyMap, PRIVATE_KEY, PRIVATE_KEY_EXPONENT, rsaPrivateKeySpec.getPrivateExponent()
                    , rsaPrivateKeySpec.getModulus());

            return keyMap;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new CryptoException(e);
        }
    }

    private void generateSpecificKeyParts(KeyMap keyMap, String description, String exponentField, BigInteger exponent, BigInteger modulus) {
        keyMap.put(DESCRIPTION, description);
        keyMap.put(MODULUS, modulus.toString(ConstantUtils.HEX_BASE_VALUE));
        keyMap.put(exponentField, exponent.toString(ConstantUtils.HEX_BASE_VALUE));
    }
}
