package javaev.util;

import java.util.ArrayList;

public class ArrayListUtils {

    public ArrayListUtils() {}

    public static byte[] toArray(ArrayList<Byte> alist, byte[] array) {
        if (null == alist || null == array) {
            return null;
        }
        int size = alist.size();
        int len = array.length;
        for (int i = 0, length = size; i < length; i++) {
            if (i < len) {
                array[i] = alist.get(i);
            } else {
                break;
            }
        }
        return array;
    }
}
