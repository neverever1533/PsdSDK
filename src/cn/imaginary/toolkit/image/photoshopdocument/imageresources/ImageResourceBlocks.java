package cn.imaginary.toolkit.image.photoshopdocument.imageresources;

// import cn.imaginary.toolkit.image.photoshopdocument.imageresources.ImageResourceIDs;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.SortedMap;
import javaev.lang.StringUtils;

public class ImageResourceBlocks {

    public static String Signature_8BIM = "8BIM";

    private String signature;

    private int id;
    private int length_Data;

    private long length_;

    private byte[] arr_Data;
    private byte[] arr_Name;

    private Charset charset;

    public ImageResourceBlocks() {}

    public String getSignature() {
        return signature;
    }

    public int getID() {
        return id;
    }

    public String getName(String charsetName) {
        return getName(Charset.forName(charsetName));
    }

    public String getName(Charset charset) {
        return new String(getNameBytes(), charset);
    }

    public void setName(String name, String charsetName) {
        setName(name, Charset.forName(charsetName));
    }

    public void setName(String name, Charset charset) {
        if (null != name) {
            setNameBytes(name.getBytes(charset), charset);
        }
    }

    public byte[] getNameBytes() {
        return arr_Name;
    }

    private void setNameBytes(byte[] array) {
        arr_Name = array;
    }

    public void setNameBytes(byte[] array, String charsetName) {
        setNameBytes(array, Charset.forName(charsetName));
    }

    public void setNameBytes(byte[] array, Charset charset) {
        setNameBytes(array);
        setCharset(charset);
    }

    public Charset getCharset() {
        if (null == charset) {
            charset = Charset.defaultCharset();
        }
        return charset;
    }

    public void setCharset(String charsetName) {
        setCharset(Charset.forName(charsetName));
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public byte[] getData() {
        return arr_Data;
    }

    public void setData(byte[] array) {
        arr_Data = array;
    }

    public long getLength() {
        return length_;
    }

    private void readData(RandomAccessFile rafile, int id) throws IOException {
        //3.2.5 Resource Data:Variable
        //Variable,The resource data, described in the sections on the individual resource types. It is padded to make the size even.
        readDataArray(rafile, length_Data, id);
    }

    private void readDataLength(RandomAccessFile rafile) throws IOException {
        //3.2.4 Resource Data Size:4
        //4,Actual size of resource data that follows
        length_Data = rafile.readInt();
        length_ += 4;
        if ((length_Data % 2) != 0) {
            length_Data++;
        }
    }

    private void readName(RandomAccessFile rafile) throws IOException {
        //3.2.3 Name:Variable
        //Variable,Name: Pascal string, padded to make the size even (a null name consists of two bytes of 0)
        int length_Name = rafile.readByte() & 0xFF;
        length_ += 1;
        if (length_Name % 2 == 0) {
            length_Name++;
        }
        byte[] arr = new byte[length_Name];
        rafile.read(arr);
        length_ += length_Name;
        //        checkCharset(arr);
        setNameBytes(arr);
    }

    private void readID(RandomAccessFile rafile) throws IOException {
        //3.2.2 Image Resource ID:2
        //2,Unique identifier for the resource. Image resource IDs contains a list of resource IDs used by Photoshop.
        id = rafile.readShort();
        length_ += 2;
    }

    private void readSignature(RandomAccessFile rafile) throws IOException {
        //3.2.1 Signature:4
        //4,Signature: '8BIM'
        byte[] arr = new byte[4];
        rafile.read(arr);
        length_ += 4;
        signature = new String(arr);

        if (!signature.equalsIgnoreCase(Signature_8BIM)) {
            throw new IOException("The signature of the Image Resource Blocks is wrong.");
        }
    }

    public void read(RandomAccessFile rafile) {
        try {
            long location = rafile.getFilePointer();

            readSignature(rafile);
            readID(rafile);
            readName(rafile);
            readDataLength(rafile);
            readData(rafile, id);

            length_ += length_Data;
            rafile.seek(location + getLength());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void readDataArray(byte[] array, int length, int id) {
        /*ImageResourceIDs irIDs = new ImageResourceIDs();
        irIDs.read(array, id);*/
    }

    private void readDataArray(RandomAccessFile rafile, int length, int id) throws IOException {
        if (length > 0) {
            byte[] arr = new byte[length];
            rafile.read(arr);
            setData(arr);
            readDataArray(arr, length, id);
        }
        //        rafile.skipBytes(length);
    }

    public String toString() {
        StringBuilder sbuilder = new StringBuilder();
        sbuilder.append("Image Resource Blocks Length: " + getLength());
        sbuilder.append("/Signature: " + signature);
        sbuilder.append("/Resource ID :" + id);
        sbuilder.append("/Name Length: " + getNameBytes().length);
        //        sbuilder.append("/Name: " + getName(getCharset()));
        sbuilder.append("/Name: " + getName("gbk"));
        sbuilder.append("/Resource Data Length: " + length_Data);
        return sbuilder.toString();
    }
}
