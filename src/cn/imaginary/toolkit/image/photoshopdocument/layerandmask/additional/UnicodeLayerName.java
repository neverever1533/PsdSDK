package cn.imaginary.toolkit.image.photoshopdocument.layerandmask.additional;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class UnicodeLayerName {

    public static String Key = "luni";
    private Charset charset;
    public static Charset Charset_Default = StandardCharsets.UTF_16;
    private byte[] arr_Data;
    private long length_;
    private int length_Data;

    public UnicodeLayerName() {}

    public long getLength() {
        return length_;
    }

    public void setName(String name, String charsetName) {
        setName(name, Charset.forName(charsetName));
    }

    public void setName(String name, Charset charset) {
        if (null != name) {
            setNameBytes(name.getBytes(charset), charset);
        }
    }

    public void setNameBytes(byte[] array, String charsetName) {
        setNameBytes(array, Charset.forName(charsetName));
    }

    public void setNameBytes(byte[] array, Charset charset) {
        setNameBytes(array);
        setCharset(charset);
    }

    private void setNameBytes(byte[] array) {
        setData(array);
    }

    public byte[] getNameBytes() {
        return getData();
    }

    public String getName(String charsetName) {
        return getName(Charset.forName(charsetName));
    }

    public String getName(Charset charset) {
        return new String(getNameBytes(), charset);
    }

    public Charset getCharset() {
        if (null == charset) {
            charset = Charset_Default;
        }
        return charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public void setCharset(String charsetName) {
        setCharset(Charset.forName(charsetName));
    }

    public byte[] getData() {
        return arr_Data;
    }

    private void setData(byte[] array) {
        arr_Data = array;
    }

    private void readKey(String key) throws IOException {
        if (null == key || !key.equalsIgnoreCase(Key)) {
            throw new IOException("The key of the Section dvider setting is wrong.");
        }
    }

    public void read(byte[] array, String key) throws IOException {
        readKey(key);
        readDataLength(array);
        readData(array);
        length_ = length_Data;
    }

    private void readData(byte[] array) {
        if (length_Data > 0) {
            setData(array);
        }
    }

    private void readDataLength(byte[] array) {
        if (null != array) {
            length_Data = array.length;
        }
    }

    public String toString() {
        StringBuilder sbuilder = new StringBuilder();
        sbuilder.append("UnicodeLayerName length: " + length_);
        sbuilder.append("/key: " + Key);
        sbuilder.append("/name: " + getName(getCharset()));
        return sbuilder.toString();
    }
}
