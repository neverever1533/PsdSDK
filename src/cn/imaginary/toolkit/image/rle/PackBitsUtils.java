package cn.imaginary.toolkit.image.rle;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.ArrayList;

import javaev.util.ArrayListUtils;

public class PackBitsUtils {

    public PackBitsUtils() {
    }

    private byte[] getDataRleCompressed(byte[] array) {
        if (null == array) {
            return array;
        }
        ArrayList<Byte> alist = new ArrayList<Byte>();
        byte len = 0;
        byte b;
        for (int i = 0, length = array.length; i < length; i++) {
            b = array[i];

            if (i < length - 1 && b == array[i + 1] && len > -127) {
                len--;
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

    public byte[] getDataCompressed(byte[] array) {
        if (null == array) {
            return array;
        }
        byte[] arr = getDataRleCompressed(array);
        ArrayList<Byte> alist = new ArrayList<Byte>();
        byte len = 0;
        byte b;

        for (int i = arr.length - 1; i >= 0; i--) {
            b = arr[i];
            if (i % 2 == 0) {
                if (b == 0) {
                    if (i - 2 >= 0 && arr[i - 2] == 0 && len < 127) {
                        len++;
                        continue;
                    } else {
                        alist.add(0, len);
                        len = 0;
                    }
                } else {
                    alist.add(0, b);
                }
            } else {
                alist.add(0, b);
            }
        }
        arr = new byte[alist.size()];
        return ArrayListUtils.toArray(alist, arr);
    }

    public byte[] getDataDecompressed(byte[] array) {
        if (null == array) {
            return array;
        }
        ArrayList<Byte> alist = new ArrayList<Byte>();
        int index = 0;
        byte len = 0;
        byte b;
        int length = array.length;
        while (index < length) {
            len = array[index++];
            // if (len <= 0 && len > -128) {
            if (len <= 0) {
                b = array[index++];
                for (int i = 0; i < 1 - len; i++) {
                    alist.add(b);
                }
                // } else if (len > 0 && len < 128) {
            } else if (len > 0) {
                for (int i = 0; i < len + 1; i++) {
                    b = array[index + i];
                    alist.add(b);
                }
                index += len + 1;
            }
        }
        byte[] arr = new byte[alist.size()];
        return ArrayListUtils.toArray(alist, arr);
    }
}