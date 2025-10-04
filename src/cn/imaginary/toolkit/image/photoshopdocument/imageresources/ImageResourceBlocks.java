package cn.imaginary.toolkit.image.photoshopdocument.imageresources;

// import cn.imaginary.toolkit.image.photoshopdocument.imageresources.ImageResourceIDs;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

public class ImageResourceBlocks {

    public static String Signature_8BIM = "8BIM";

    private String signature;

    private int id;
    private int length_Data;
    private int length_;

    private byte[] arr_Data;
    private byte[] arr_Name;

    private Charset charset;

    public ImageResourceBlocks() {
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) throws IOException {
        if (signature.equalsIgnoreCase(Signature_8BIM)) {
            this.signature = signature;
        } else {
            throw new IOException("The signature of the Image Resource Blocks is wrong.");
        }
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
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

    public int getLength() {
        return length_;
    }

    public void setLength(int length) {
        length_ = length;
    }

    public int getDataLength() {
        return length_Data;
    }

    public void setDataLength(int length) {
        length_Data = length;
    }

    public void read(DataInputStream dinstream) {
        try {
            //3.2.1 Signature:4
            //4,Signature: '8BIM'
            byte[] arr = new byte[4];
            dinstream.read(arr);
            setSignature(new String(arr));

            //3.2.2 Image Resource ID:2
            //2,Unique identifier for the resource. Image resource IDs contains a list of resource IDs used by Photoshop.
            setID(dinstream.readShort());

            //3.2.3 Name:Variable
            //Variable,Name: Pascal string, padded to make the size even (a null name consists of two bytes of 0)
            int length_Name = dinstream.readByte() & 0xFF;
            if (length_Name % 2 == 0) {
                length_Name++;
            }
            arr = new byte[length_Name];
            dinstream.read(arr);
            setNameBytes(arr);

            //3.2.4 Resource Data Size:4
            //4,Actual size of resource data that follows
            int length = dinstream.readInt();
            if ((length % 2) != 0) {
                length++;
            }
            setDataLength(length);
            setLength(4 + 2 + 1 + getNameBytes().length + 4 + getDataLength());

            //3.2.5 Resource Data:Variable
            //Variable,The resource data, described in the sections on the individual resource types. It is padded to make the size even.
            if (length > 0) {
                arr = new byte[length];
                dinstream.read(arr);
                setData(arr);
                readData(arr, id);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void readData(byte[] array, int id) {
        /*ImageResourceIDs irIDs = new ImageResourceIDs();
        irIDs.read(array, id);*/
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
