package hr.fer.zemris.nos.symmetric.aes;

import hr.fer.zemris.nos.utils.ArrayUtils;
import hr.fer.zemris.nos.utils.Pair;

/**
 * Created by mihael on 04.05.17..
 */
public class AESBlock {

    public static final int NO_OF_ROWS = 4;
    public static final int RIJNDAEL_NO_OF_COLS = 4;

    public int[][] block;
    private int noOfColumns;


    public AESBlock(int[] intInput, int noOfColumns) {

        this.noOfColumns = noOfColumns;

        if(intInput.length != NO_OF_ROWS * noOfColumns) {
            throw new IllegalArgumentException("Size of block must be " + (NO_OF_ROWS * noOfColumns) +  "!");
        }
        block = new int[NO_OF_ROWS][noOfColumns];
        for (int i = 0; i < NO_OF_ROWS * noOfColumns; i++) {
            block[i % NO_OF_ROWS][i / noOfColumns] = intInput[i];
        }
    }

    public AESBlock(byte[] input, int noOfColumns) {
        this(ArrayUtils.toIntArray(input), noOfColumns);
    }

    /* obrisati*/
    AESBlock(int[][] block) {
        this.block = block;
        this.noOfColumns = block[0].length;
    }

    public AESBlock(int noOfColumns) {
        this.noOfColumns = noOfColumns;
        this.block = new int[NO_OF_ROWS][noOfColumns];
    }

    public AESBlock(AESBlock other) {
        this.noOfColumns = other.noOfColumns;
        this.block = ArrayUtils.createTwoDimensionalArrayFrom(other.block);
    }

    public int[] getRow(int rowIndex) {
        return block[rowIndex];
    }

    public int getElement(int index) {
        return block[index % NO_OF_ROWS][index / NO_OF_ROWS];
    }

    public void setElement(int index, int value) {
        block[index % NO_OF_ROWS][index / NO_OF_ROWS] = value;
    }

    public int getElement(int row, int column) {
        return block[row][column];
    }

    public void setElement(int row, int column, int value) {
        block[row][column] = value;
    }


    private static Pair<Integer, Integer> forIndexInTwoDimRow(int[][] r, int index) {
        int row = index % NO_OF_ROWS;
        int column = index/4;
        return Pair.createPair(row, column);
    }

    public byte[] getByteValue() {
        return ArrayUtils.toByteArray(getIntValue());
    }

    public int[] getIntValue() {
        int[] output = new int[NO_OF_ROWS * noOfColumns];
        int index = 0;
        for (int i = 0; i < noOfColumns; i++) {
            for (int j = 0; j < NO_OF_ROWS; j++) {
                output[index++] = block[j][i];
            }
        }
        return output;
    }

    public int getNoOfColumns() {
        return noOfColumns;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < noOfColumns; i++) {
            for (int j = 0; j < NO_OF_ROWS; j++) {
                String hex = Integer.toHexString(block[j][i]);
                if(hex.length() <= 1) {
                    string.append("0");
                }
                string.append(hex);
            }
        }
        return string.toString();
    }
}
