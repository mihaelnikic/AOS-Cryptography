package hr.fer.zemris.nos.crypto;

/**
 * Created by mihael on 04.05.17..
 */
public class CryptoException extends RuntimeException {

    public CryptoException(String s) {
        super(s);
    }

    public CryptoException(Throwable throwable) {
        super(throwable);
    }
}
