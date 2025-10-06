package cn.imaginary.toolkit.image.rle;

import java.util.ArrayList;

public class PackBitsUtils {

    public PackBitsUtils() {
    }

    private byte[] getDataRleCompressed(byte[] array) {
        if (null == array) {
            return null;
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
        return toArray(alist);
    }

    public byte[] getDataCompressed(byte[] array) {
        if (null == array) {
            return null;
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
        return toArray(alist);
    }

    public byte[] getDataDecompressed(byte[] array) {
        if (null == array) {
            return null;
        }
        int length = array.length;
        ArrayList<Byte> alist = new ArrayList<Byte>();
        int index = 0;
        byte len = 0;
        byte b;
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
        return toArray(alist);
    }

    private byte[] toArray(ArrayList<Byte> arrayList) {
        return toArray(arrayList, null);
    }

    private byte[] toArray(ArrayList<Byte> arrayList, byte[] array) {
        if (null == arrayList) {
            return null;
        }
        if (null == array) {
            array = new byte[arrayList.size()];
        }
        for (int i = 0; i < array.length; i++) {
            array[i] = arrayList.get(i);
        }
        return array;
    }
}
