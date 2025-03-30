package cn.imaginary.toolkit.image.photoshopdocument.layerandmask.layer;

public class ChannelInfo {

    public ChannelInfo() {}

    private int id;
    public static int ID_Red = 0;
    public static int ID_Green = 1;
    public static int ID_Blue = 2;
    public static int ID_Alpha = -1;
    public static int ID_Mask = -2;

    private int length;

    private byte[] arr_DataCompressed;

    public int getID() {
        return id;
    }

    public int getLength() {
        return length;
    }

    public byte[] getDataCompressed() {
        return arr_DataCompressed;
    }

    public void setID(int id) {
        this.id = id;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setDataCompressed(byte[] arr) {
        arr_DataCompressed = arr;
    }

    public String toString() {
        StringBuilder sbuilder = new StringBuilder();
        sbuilder.append("ID: " + id);
        sbuilder.append("/Length: " + length);
        return sbuilder.toString();
    }
}
