package cn.imaginary.toolkit.image.photoshopdocument.layerandmask.layer;

public class ChannelInfo {

    public static int ID_Channel_Red = 0;
    public static int ID_Channel_Green = 1;
    public static int ID_Channel_Blue = 2;
    public static int ID_Channel_Alpha = -1;
    public static int ID_Channel_Mask = -2;
    private int id_Channel;
    private long length_;
    private long length_Data;
    private byte[] arr_Data;

    public ChannelInfo() {}

    public int getID() {
        return id_Channel;
    }

    public void setID(int id) {
        id_Channel = id;
    }

    public long getLength() {
        return length_;
    }

    public long getDataLength() {
        return length_Data;
    }

    public void setDataLength(long length) {
        length_Data = length;
    }

    public byte[] getData() {
        return arr_Data;
    }

    public void setData(byte[] array) {
        arr_Data = array;
    }

    public String toString() {
        StringBuilder sbuilder = new StringBuilder();
        sbuilder.append("Channel ID: " + id_Channel);
        sbuilder.append("/Channel Data Length: " + length_Data);
        return sbuilder.toString();
    }
}
