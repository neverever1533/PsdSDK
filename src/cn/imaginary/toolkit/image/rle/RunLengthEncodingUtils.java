package cn.imaginary.toolkit.image.rle;

import java.util.ArrayList;

import javaev.util.ArrayListUtils;

public class RunLengthEncodingUtils {

    public RunLengthEncodingUtils() {
    }

    public byte[] getDataRleCompressed(byte[] array) {
        ArrayList<Byte> alist = new ArrayList<Byte>();
        byte len = 0;
        byte b;
        for (int i = 0, length = array.length; i < length; i++) {
            b = array[i];
            if (i < length - 1 && b == array[i + 1] && len < 255) {
                len++;
                continue;
            } else {
                alist.add(len);
                len = 0;
            }
            alist.add(b);
        }
        byte[] arr = new byte[alist.size()];
        return ArrayListUtils.toArray(alist, arr);
    }

    public byte[] getDataRleDecompressed(byte[] array) {
        ArrayList<Byte> alist = new ArrayList<Byte>();
        int index = 0;
        byte len = 0;
        byte b;
        int length = array.length;
        while (index < length) {
            len = array[index++];
            if (len >= 0) {
                b = array[index++];
                for (int i = 0; i < len + 1; i++) {
                    alist.add(b);
                }
            }
        }
        byte[] arr = new byte[alist.size()];
        return ArrayListUtils.toArray(alist, arr);
    }
}
