package cn.imaginary.toolkit.image.photoshopdocument.layerandmask.layer;

public class ChannelInfo {

    public ChannelInfo() {}

    private int id_Channel;
    public static int ID_Channel_Red = 0;
    public static int ID_Channel_Green = 1;
    public static int ID_Channel_Blue = 2;
    public static int ID_Channel_Alpha = -1;
    public static int ID_Channel_Mask = -2;

    private long length_ChannelData;

    private byte[] arr_DataCompressed;

    public int getChannelID() {
        return id_Channel;
    }

    public long getChannelDataLength() {
        return length_ChannelData;
    }

    public byte[] getChannelDataCompressed() {
        return arr_DataCompressed;
    }

    public void setChannelID(int id) {
        id_Channel = id;
    }

    public void setChannelDataLength(long length) {
        length_ChannelData = length;
    }

    public void setChannelDataCompressed(byte[] array) {
        arr_DataCompressed = array;
    }

    public String toString() {
        StringBuilder sbuilder = new StringBuilder();
        sbuilder.append("Channel ID: " + id_Channel);
        sbuilder.append("/Channel Data Length: " + length_ChannelData);
        return sbuilder.toString();
    }
}