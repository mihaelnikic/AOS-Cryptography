package hr.fer.zemris.nos.utils;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Arrays;

/**
 * Created by mihael on 03.05.17..
 */
public final class ArrayUtils {

    private ArrayUtils() {}

    public static void copyTwoDimensionalArray(int input[][], int output[][]) {
        for(int i=0; i<input.length; i++)
            for(int j=0; j<input[i].length; j++)
                output[i][j] = input[i][j];
    }

    public static int[][] createTwoDimensionalArrayFrom(int input[][]) {
        int output[][] = new int[input.length][input[0].length];
        copyTwoDimensionalArray(input, output);
        return output;
    }

    public static int[] toIntArray(byte buf[])
    {
        int size = (buf.length / 4) + ((buf.length % 4 == 0) ? 0 : 1);

        ByteBuffer bb = ByteBuffer.allocate(size *4);
        bb.put(buf);

        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.rewind();
        IntBuffer ib =  bb.asIntBuffer();
        int [] result = new int [size];
        ib.get(result);

        return result;
//        IntBuffer intBuf =
//                ByteBuffer.wrap(buf)
//                        .order(ByteOrder.BIG_ENDIAN)
//                        .asIntBuffer();
//        int[] array = new int[intBuf.remaining()];
//        intBuf.get(array);
//        return array;
    }

    public static byte[] toByteArray(int[] ints)
    {
        final ByteBuffer buf = ByteBuffer.allocate(ints.length * 4)
                .order(ByteOrder.LITTLE_ENDIAN);
        buf.asIntBuffer().put(ints);
        return buf.array();
    }

    public static byte[][] divideByteArray(byte[] source, int chunkSize) {


        byte[][] res = new byte[(int)Math.ceil(source.length / (double)chunkSize)][chunkSize];
        int start = 0;

        for(int i = 0; i < res.length; i++) {
            res[i] = Arrays.copyOfRange(source,start, start + chunkSize);
            start += chunkSize ;
        }

        return res;
    }

    public static byte[][] divideByteArrayWithKeepingSize(byte[] source, int chunkSize) {

        byte[][] res = new byte[(int)Math.ceil(source.length / (double)chunkSize)][];
        int start = 0;

        for(int i = 0; i < res.length; i++) {
           // res[i] = new byte[(source.length > (start + chunkSize))?chunkSize:source.length - start];
            res[i] = Arrays.copyOfRange(source,start, start + ((source.length > start + chunkSize)? chunkSize:source.length - start));
            start += chunkSize ;
        }

        return res;
    }

    public static void main(String[] args) {
        byte[] bytes = new byte[100];
        byte[][] divided = divideByteArrayWithKeepingSize(bytes, 30);
    }

    public static int[][] divideIntArray(int[] source, int chunkSize) {


        int[][] res = new int[(int)Math.ceil(source.length / (double)chunkSize)][chunkSize];
        int start = 0;

        for(int i = 0; i < res.length; i++) {
            res[i] = Arrays.copyOfRange(source,start, start + chunkSize);
            start += chunkSize ;
        }

        return res;
    }

    public static byte[] mergeByteArrays(byte[][] source) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        for (byte[] b : source) {
            os.write(b, 0, b.length);
        }
        return os.toByteArray();
//        int size = 0;
//        for (int i = 0; i < source.length; i++) {
//            size += source[i].length;
//        }
//        ByteBuffer bb = ByteBuffer.allocate(size);
//        for (int i = 0; i < source.length; i++) {
//            bb.put(source[i]);
//        }
//        return bb.array();
    }

    public static int[] mergeIntArrays(int[][] source) {
        int size = 0;
        for ( int[] a: source )
            size += a.length;

        int[] res = new int[size];

        int destPos = 0;
        for ( int i = 0; i < source.length; i++ ) {
            if ( i > 0 ) destPos += source[i-1].length;
            int length = source[i].length;
            System.arraycopy(source[i], 0, res, destPos, length);
        }

        return res;
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public static int[] hexStringToIntegerArray(String s) {
        int len = s.length();
        if (len % 2 != 0) {
            throw new IllegalArgumentException("Only even number given as length is acceptable!");
        }
        int[] data = new int[len / 2];
        for (int i = 0, j = 0; i < data.length; i++, j+=2) {
            data[i] = Integer.valueOf(s.substring(j, j + 2), 16);
        }
        return data;
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = ConstantUtils.hexArray[v >>> 4];
            hexChars[j * 2 + 1] = ConstantUtils.hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

}
