package hr.fer.zemris.nos.symmetric.aes;

/**
 * Created by mihael on 03.05.17..
 */
public enum AESKeyDescription {

    AES_128(16, 10, 4), AES_192(24, 12, 6), AES_256(32, 14, 8);

    int keySize;
    int noOfRounds;
    int keyBlockSize;

    AESKeyDescription(int keySize, int noOfRounds, int keyBlockSize) {
        this.keySize = keySize;
        this.keyBlockSize = keyBlockSize;
        this.noOfRounds = noOfRounds;
    }

    public int getKeySize() {
        return keySize;
    }

    public int getNoOfRounds() {
        return noOfRounds;
    }

    public int getKeyBlockSize() {
        return keyBlockSize;
    }

    public static AESKeyDescription getDescriptionForBlockSize(int noOfColumns) {
        for (AESKeyDescription desc : AESKeyDescription.values()) {
            if (desc.getKeyBlockSize() == noOfColumns) {
                return desc;
            }
        }
        throw new IllegalArgumentException("Description for key with given length does not exists!");
    }

    public static AESKeyDescription getDescriptionForKeySize(int keySize) {
        for (AESKeyDescription desc : AESKeyDescription.values()) {
            if (desc.getKeySize() == keySize) {
                return desc;
            }
        }
        throw new IllegalArgumentException("Description for key with given length does not exists!");
    }
}
