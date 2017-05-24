package hr.fer.zemris.nos.symmetric.aes;

import hr.fer.zemris.nos.files.writer.EncryptedFileGen;
import hr.fer.zemris.nos.symmetric.SymmetricCryptoAlgorithm;
import hr.fer.zemris.nos.utils.ArrayUtils;
import hr.fer.zemris.nos.utils.ConstantUtils;
import hr.fer.zemris.nos.utils.Pair;

import java.io.IOException;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

/**
 * Created by mihael on 03.05.17..
 */
public class AES extends SymmetricCryptoAlgorithm {

    // Name of system
    static final String AES_ALGORITHM_NAME = "AES";

    @Override
    public byte[] encrypt(byte[] base64Encoded, Key privateKey) {
        return encryptDecryptBlocks(base64Encoded, AESMode.ENCRYPT, (AESKey)privateKey);
    }

    @Override
    public byte[] decrypt(byte[] encryptedData, Key privateKey) {
        return encryptDecryptBlocks(encryptedData, AESMode.DECRYPT, (AESKey)privateKey);
    }

    private enum AESMode {
        ENCRYPT, DECRYPT
    }

    private static final int AES_256_MOD = 16;
    private static final int INV_POLY_VAL_11 = 11;
    private static final int INV_POLY_VAL_13 = 13;
    private static final int INV_POLY_VAL_9 = 9;
    private static final int INV_POLY_VAL_14 = 14;

    private static void rotate(int[] r) {
        int b = r[0];
        for (int i = 0; i < r.length - 1; i++) {
            r[i] = r[i + 1];
        }
        r[r.length - 1] = b;
    }

    private static void invRotate(int[] r) {
        int b = r[r.length - 1];
        for (int i = r.length - 1; i > 0; i--) {
            r[i] = r[i - 1];
        }
        r[0] = b;
    }

    private static int rCon(int i) {
        return AESConstants.rCon[i];
    }

    private static int[] keyScheduleCore(int[] r, int iteration) {
        rotate(r);
        for (int i = 0; i < AESBlock.NO_OF_ROWS; i++) {
            r[i] = AESConstants.sBox[r[i]];
        }
        r[0] = r[0] ^ rCon(iteration);
        return r;
    }

    private static AESBlock createExtendedKey(AESBlock originalKey, AESKeyDescription keyDesc) {
        int currentKeyPosition = 0;
        int rConIteration = 1;
        int b = (keyDesc.getNoOfRounds() + 1) * keyDesc.getKeySize();
        int[] t = new int[4];


        AESBlock key = new AESBlock(b);

        for (int i = 0; i < keyDesc.getKeySize(); i++) {
            //key[i] = originalKey[i];
            key.setElement(i, originalKey.getElement(i));
        }
        currentKeyPosition += keyDesc.getKeySize();


        while (currentKeyPosition < b) {
            for (int i = 0; i < AESBlock.NO_OF_ROWS; i++) {
                t[i] = key.getElement((currentKeyPosition - 4) + i);
            }

            if (currentKeyPosition % keyDesc.getKeySize() == 0) {
                t = keyScheduleCore(t, rConIteration++);
            }

            if (keyDesc == AESKeyDescription.AES_256 && (currentKeyPosition % keyDesc.getKeySize() == AES_256_MOD)) {
                for (int i = 0; i < AESBlock.NO_OF_ROWS; i++) {
                    t[i] = AESConstants.sBox[t[i]];
                }
            }

            for (int i = 0; i < AESBlock.NO_OF_ROWS; i++) {
                //key[currentKeyPosition] = key[currentKeyPosition - keyDesc.getKeySize()] ^ t[i];
                key.setElement(currentKeyPosition, key.getElement(currentKeyPosition - keyDesc.getKeySize()) ^ t[i]);
                currentKeyPosition++;
            }
        }
        return key;
    }


    private static void substituteBytes(AESBlock output, AESMode mode) {
        for (int i = 0; i < AESBlock.RIJNDAEL_NO_OF_COLS * AESBlock.NO_OF_ROWS; i++) {
            output.setElement(i,  mode == AESMode.ENCRYPT ? AESConstants.sBox[output.getElement(i)]
                    : AESConstants.invSBox[output.getElement(i)]);
        }
    }

    private static void permuteColumns(AESBlock output, AESMode mode) {
        for (int i = 0; i < 4; i++) {
            if (mode == AESMode.ENCRYPT) {
                permuteColumn(output, i);
            } else {
                invPermuteColumn(output, i);
            }
        }
    }

    private static void invPermuteColumn(AESBlock output, int columnIndex) {
        int[] a = new int[AESBlock.NO_OF_ROWS];

        for (int i = 0; i < AESBlock.NO_OF_ROWS; i++) {
            a[i] = output.getElement(i, columnIndex);
        }

        output.setElement(0, columnIndex, xorFactorizeTimes(a, INV_POLY_VAL_14, INV_POLY_VAL_11, INV_POLY_VAL_13, INV_POLY_VAL_9));
        output.setElement(1, columnIndex, xorFactorizeTimes(a, INV_POLY_VAL_9, INV_POLY_VAL_14, INV_POLY_VAL_11, INV_POLY_VAL_13));
        output.setElement(2, columnIndex, xorFactorizeTimes(a, INV_POLY_VAL_13, INV_POLY_VAL_9, INV_POLY_VAL_14, INV_POLY_VAL_11));
        output.setElement(3, columnIndex, xorFactorizeTimes(a, INV_POLY_VAL_11, INV_POLY_VAL_13, INV_POLY_VAL_9, INV_POLY_VAL_14));
    }

    private static int xMultiplyFactorize(int element, int factor) {
        for (int i = 0; i < factor; i++) {
            element = xMultiply(element);
        }
        return element;
    }

    private static int xFactorizeNTimes(int element, int n) {
        switch (n) {
            case INV_POLY_VAL_14:
                return xMultiplyFactorize(element, 3) ^ xMultiplyFactorize(element, 2) ^ xMultiply(element);
            case INV_POLY_VAL_11:
                return xMultiplyFactorize(element, 3) ^ xMultiply(element) ^ element;
            case INV_POLY_VAL_13:
                return xMultiplyFactorize(element, 3) ^ xMultiplyFactorize(element, 2) ^ element;
            case INV_POLY_VAL_9:
                return xMultiplyFactorize(element, 3) ^ element;
            default:
                throw new IllegalArgumentException("Unsupported n was given!");
        }
    }

    private static int xorFactorizeTimes(int[] row, int ... numbers) {
        int res = 0;
        for (int i = 0; i < row.length; i++) {
            res ^= xFactorizeNTimes(row[i], numbers[i]);
        }
        return res;
    }

    private static void permuteColumn(AESBlock output, int columnIndex) {
        int[] a = new int[AESBlock.NO_OF_ROWS];
        int[] b = new int[AESBlock.NO_OF_ROWS];

        for (int i = 0; i < AESBlock.NO_OF_ROWS; i++) {
            //a[i] = output[i][columnIndex];
            a[i] = output.getElement(i, columnIndex);
            b[i] = xMultiply(a[i]);
        }

        //output[0][columnIndex] = b[0] ^ a[3] ^ a[2] ^ b[1] ^ a[1];
        output.setElement(0, columnIndex, b[0] ^ a[3] ^ a[2] ^ b[1] ^ a[1]);
       // output[1][columnIndex] = b[1] ^ a[0] ^ a[3] ^ b[2] ^ a[2];
        output.setElement(1, columnIndex, b[1] ^ a[0] ^ a[3] ^ b[2] ^ a[2]);
       // output[2][columnIndex] = b[2] ^ a[1] ^ a[0] ^ b[3] ^ a[3];
        output.setElement(2, columnIndex, b[2] ^ a[1] ^ a[0] ^ b[3] ^ a[3]);
       // output[3][columnIndex] = b[3] ^ a[2] ^ a[1] ^ b[0] ^ a[0];
        output.setElement(3, columnIndex, b[3] ^ a[2] ^ a[1] ^ b[0] ^ a[0]);
    }

    private static int xMultiply(int a) {
        int p = a >>> 7;
        int res = a << 1;

        return (p == 1) ? res ^ 0x11b : res;

    }

    private AESBlock generateResult(AESBlock input, AESBlock key, AESMode mode) {
        AESKeyDescription keyDesc = AESKeyDescription.getDescriptionForBlockSize(key.getNoOfColumns());
        AESBlock output = new AESBlock(input);

        AESBlock extendedKey = createExtendedKey(key, keyDesc);
        int currentRound = mode == AESMode.ENCRYPT ? 0 : keyDesc.getNoOfRounds();

        addRoundKey(output, extendedKey, keyDesc, currentRound);
        currentRound += mode == AESMode.ENCRYPT? 1 : -1;

        while ((mode == AESMode.ENCRYPT && currentRound < keyDesc.getNoOfRounds()) || (mode == AESMode.DECRYPT && currentRound > 0)) {
            switch (mode) {
                case ENCRYPT:
                    substituteBytes(output, mode);
                    shiftRows(output, mode);
                    permuteColumns(output, mode);
                    addRoundKey(output, extendedKey, keyDesc, currentRound);
                    break;
                case DECRYPT:
                    shiftRows(output, mode);
                    substituteBytes(output, mode);
                    addRoundKey(output, extendedKey, keyDesc, currentRound);
                    permuteColumns(output, mode);
                    break;
            }
            currentRound += mode == AESMode.ENCRYPT? 1 : -1;
        }

        switch (mode) {
            case ENCRYPT:
                substituteBytes(output, mode);
                shiftRows(output, mode);
                break;
            case DECRYPT:
                shiftRows(output, mode);
                substituteBytes(output, mode);
                break;
        }
        addRoundKey(output, extendedKey, keyDesc, currentRound);

        return output;
    }


    private byte[] encryptDecryptBlocks(byte[] dataToEncrypt, AESMode mode, AESKey privateKey) {
        byte[][] data = ArrayUtils.divideByteArray(dataToEncrypt, AESBlock.NO_OF_ROWS * AESBlock.RIJNDAEL_NO_OF_COLS);

        for (int i = 0; i < data.length; i++) {
            data[i] = encryptDecryptBlock(data[i], privateKey, mode);
        }

        return ArrayUtils.mergeByteArrays(data);
    }

    private static void shiftRows(AESBlock output, AESMode mode) {
        for (int i = 1; i < AESBlock.NO_OF_ROWS; i++) {
            for (int j = 0; j < i; j++) {
                if(mode == AESMode.ENCRYPT) {
                    rotate(output.getRow(i));
                } else {
                    invRotate(output.getRow(i));
                }
            }
        }
    }

    private static void addRoundKey(AESBlock output, AESBlock extendedKey, AESKeyDescription keyDesc, int currentRound) {
        for (int i = 0; i < keyDesc.getKeySize(); i++) {
            output.setElement(i, output.getElement(i) ^ extendedKey.getElement(currentRound * keyDesc.getKeySize() + i));
        }
    }


    private byte[] encryptDecryptBlock(byte[] block, AESBlock key, AESMode mode) {

        byte[] output = new byte[block.length];

        AESBlock input = new AESBlock(AESBlock.RIJNDAEL_NO_OF_COLS);
        for (int i = 0; i < AESBlock.RIJNDAEL_NO_OF_COLS; i++) {
            for (int j = 0; j < AESBlock.NO_OF_ROWS; j++) {
                input.setElement(j, i, block[i * AESBlock.RIJNDAEL_NO_OF_COLS + j] & 0xff);
            }
        }

        AESBlock outputBlock = generateResult(input, key, mode);
        for (int i = 0; i < AESBlock.RIJNDAEL_NO_OF_COLS; i++) {
            for (int j = 0; j < AESBlock.NO_OF_ROWS; j++) {
                output[i * AESBlock.RIJNDAEL_NO_OF_COLS + j] = (byte) (outputBlock.getElement(j, i) & 0xff);
            }
        }

        return output;
    }


}
