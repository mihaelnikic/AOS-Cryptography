package hr.fer.zemris.nos.asymmetric;

import hr.fer.zemris.nos.crypto.ICryptoAlgorithm;
import hr.fer.zemris.nos.keygen.IKeyGenerator;
import hr.fer.zemris.nos.signature.ISignature;
import hr.fer.zemris.nos.system.ICryptoSystem;

/**
 * Created by mihael on 05.05.17..
 */
public abstract class AsymmetricCryptoSystem implements ICryptoSystem {

    private AsymmetricCryptoAlgorithm cryptoAlgorithm;
    private AsymmetricKeyGenerator keyGenerator;

    public AsymmetricCryptoSystem(AsymmetricCryptoAlgorithm cryptoAlgorithm, AsymmetricKeyGenerator keyGenerator) {
        this.cryptoAlgorithm = cryptoAlgorithm;
        this.keyGenerator = keyGenerator;
    }

    @Override
    public ICryptoAlgorithm getCryptoAlgorithm() {
        return cryptoAlgorithm;
    }

    @Override
    public IKeyGenerator getKeyGenerator() {
        return keyGenerator;
    }

    @Override
    public ISignature getSignatureChecker() {
        return null;
    }
}
