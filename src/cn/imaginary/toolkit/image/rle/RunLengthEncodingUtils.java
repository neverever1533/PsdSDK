package cn.imaginary.toolkit.image.rle;

import java.util.ArrayList;
import javaev.util.ArrayListUtils;

public class RunLengthEncodingUtils {

    public RunLengthEncodingUtils() {}

    public byte[] getDataRleCompressed(byte[] array) {
        ArrayList<Byte> alist = new ArrayList<Byte>();
        byte len = 1;
        byte b;
        for (int i = 0, length = array.length; i < length; i++) {
            b = array[i];
            if (i < length - 1 && b == array[i + 1]) {
                len++;
            } else {
                alist.add(len);
                alist.add(b);
                len = 1;
            }
        }
        byte[] arr = new byte[alist.size()];
        return ArrayListUtils.toArray(alist, arr);
    }

    public byte[] getDataRleDecompressed(byte[] array) {
        ArrayList<Byte> alist = new ArrayList<Byte>();
        byte b_count = 1;
        byte b_item;
        byte len = 1;
        for (int i = 0, length = array.length; i < length; i++) {
            if (i % 2 == 0) {
                b_count = array[i];
            } else {
                b_item = array[i];
                while (len <= b_count) {
                    alist.add(b_item);
                    len++;
                }
                len = 1;
            }
        }
        byte[] arr = new byte[alist.size()];
        return ArrayListUtils.toArray(alist, arr);
    }
}