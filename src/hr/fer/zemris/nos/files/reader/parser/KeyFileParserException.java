package hr.fer.zemris.nos.files.reader.parser;

/**
 * Created by mihael on 29.04.17..
 */
public class KeyFileParserException extends RuntimeException {

    public KeyFileParserException(String s) {
        super(s);
    }

    public KeyFileParserException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public KeyFileParserException(Throwable throwable) {
        super(throwable);
    }

    public KeyFileParserException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
