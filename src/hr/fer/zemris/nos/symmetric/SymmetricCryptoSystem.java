package hr.fer.zemris.nos.symmetric;

import hr.fer.zemris.nos.crypto.ICryptoAlgorithm;
import hr.fer.zemris.nos.keygen.IKeyGenerator;
import hr.fer.zemris.nos.signature.ISignature;
import hr.fer.zemris.nos.system.ICryptoSystem;

/**
 * Created by mihael on 05.05.17..
 */
public abstract class SymmetricCryptoSystem implements ICryptoSystem {

    private SymmetricCryptoAlgorithm cryptoAlgorithm;
    private SymmetricKeyGen keyGen;

    public SymmetricCryptoSystem(SymmetricCryptoAlgorithm cryptoAlgorithm, SymmetricKeyGen keyGen) {
        this.cryptoAlgorithm = cryptoAlgorithm;
        this.keyGen = keyGen;
    }

    @Override
    public ICryptoAlgorithm getCryptoAlgorithm() {
        return cryptoAlgorithm;
    }

    @Override
    public IKeyGenerator getKeyGenerator() {
        return keyGen;
    }


    @Override
    public ISignature getSignatureChecker() {
        return null;
    }
}
