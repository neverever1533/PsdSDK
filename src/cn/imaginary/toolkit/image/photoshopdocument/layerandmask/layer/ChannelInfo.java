package cn.imaginary.toolkit.image.photoshopdocument.layerandmask.layer;

import cn.imaginary.toolkit.image.photoshopdocument.FileHeader;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public class ChannelInfo {

    private int id;
    public static int ID_Red = 0;
    public static int ID_Green = 1;
    public static int ID_Blue = 2;
    public static int ID_Alpha = -1;
    public static int ID_Mask = -2;
    public static int ID_Mask_Real = -3;
    private long length_;
    private long length_Data;
    private byte[] arr_Data;
    private byte[][] arrs_Data_Image;

    //4.2.3.3 Channel Info ?
    public ChannelInfo() {
    }

    public byte[] getData() {
        return arr_Data;
    }

    public void setData(byte[] array) {
        arr_Data = array;
    }

    public byte[][] getImageData() {
        return arrs_Data_Image;
    }

    public void setImageData(byte[][] array) {
        arrs_Data_Image = array;
    }

    public long getDataLength() {
        return length_Data;
    }

    public void setDataLength(long length) {
        length_Data = length;
    }

    public int getID() {
        return id;
    }

    public long getLength() {
        return length_;
    }

    public void setLength(long length) {
        length_ = length;
    }

    public void setID(int id) throws IOException {
        switch (id) {
            case 0:
                this.id = ID_Red;
                break;
            case 1:
                this.id = ID_Green;
                break;
            case 2:
                this.id = ID_Blue;
                break;
            case -1:
                this.id = ID_Alpha;
                break;
            case -2:
                this.id = ID_Mask;
                break;
            case -3:
                this.id = ID_Mask_Real;
                break;
            default:
                throw new IOException("The id of the ChannelInfo is wrong.");
        }
    }

    public void read(DataInputStream dinstream, FileHeader fheader) {
        try {
            // Channel information. Six bytes per channel, consisting of:
            //4.2.3.3.1 Channel ID:2
            // 2 bytes for Channel ID: 0 = red, 1 = green, etc.;
            // -1 = transparency mask; -2 = user supplied layer mask, -3 real user supplied layer mask (when both a user mask and a vector mask are present)
            setID(dinstream.readShort());
            length_ += 2;

            //4.2.3.3.2 Channel Data Length:4
            // 4 bytes for length of corresponding channel data. (**PSB** 8 bytes for length of corresponding channel data.) See See Channel image data for structure of channel data.
            long length;
            if (fheader.isFilePsb()) {
                length = dinstream.readLong();
                length_ += 8;
            } else {
                length = dinstream.readInt();
                length_ += 4;
            }
            setDataLength(length);
            setLength(length_ + getDataLength());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String toString() {
        StringBuilder sbuilder = new StringBuilder();
        sbuilder.append("ChannelInfo ID: " + getID());
        sbuilder.append("/ChannelInfo Data Length: " + getDataLength());
        return sbuilder.toString();
    }
}
