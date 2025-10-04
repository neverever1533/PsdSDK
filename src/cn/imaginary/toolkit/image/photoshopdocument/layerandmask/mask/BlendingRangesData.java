package cn.imaginary.toolkit.image.photoshopdocument.layerandmask.mask;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public class BlendingRangesData {

    private int length_;
    private int length_Data;
    private int grayBlend_Black_Source;
    private int grayBlend_White_Source;
    private int grayBlend_Black_Destination;
    private int grayBlend_White_Destination;

    private int[][] arrs_grayBlend;
    private byte[] arr_Data;

    public BlendingRangesData() {
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

    public byte[] getData() {
        return arr_Data;
    }

    public void setData(byte[] arr) {
        arr_Data = arr;
    }

    public void setGrayBlendData(int[][] arrs) {
        arrs_grayBlend = arrs;
    }

    public int[][] getGrayBlendData() {
        return arrs_grayBlend;
    }

    public void read(DataInputStream dinstream) {
        try {
            readDataLength(dinstream);
            readData(dinstream, getDataLength());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void readDataLength(DataInputStream dinstream) throws IOException {
        //Data Length:4
        //4,Length of layer blending ranges data
        int length = dinstream.readInt();
        setDataLength(length);
        setLength(4 + getDataLength());
    }

    private void readData(DataInputStream dinstream, int length) throws IOException {
        if (length > 0) {
            byte[] arr = new byte[length];
            dinstream.read(arr);
            setData(arr);
            readDataArray(arr);
        }
    }

    private void readDataArray(byte[] array) {
        try {
            int length = array.length;
            DataInputStream dinstream = new DataInputStream(new ByteArrayInputStream(array));
            //Composite gray blend source:4
            //4,Composite gray blend source. Contains 2 black values followed by 2 white values. Present but irrelevant for Lab & Grayscale.
            grayBlend_Black_Source = dinstream.readShort() & 0xff;
            grayBlend_White_Source = dinstream.readShort() & 0xff;

            //Composite gray blend destination:4
            //4,Composite gray blend destination range
            // grayBlend_Destination = rafile.readInt();
            grayBlend_Black_Destination = dinstream.readShort() & 0xff;
            grayBlend_White_Destination = dinstream.readShort() & 0xff;

            int count = (length - 8) / 8;
            if (count > 0) {
                int[][] arrs = new int[count][4];

                int rangeSource;
                int rangeDestination;
                for (int i = 0; i < count; i++) {
                    //channel source range:4
                    //4,channel source range
                    rangeSource = dinstream.readShort() & 0xff;
                    arrs[i][0] = rangeSource;
                    // System.out.println("black range " + i + " Source: " + rangeSource);

                    rangeSource = dinstream.readShort() & 0xff;
                    arrs[i][1] = rangeSource;
                    // System.out.println("white range " + i + " Source: " + rangeSource);

                    //channel destination range:4
                    //4,channel destination range
                    rangeDestination = dinstream.readShort() & 0xff;
                    arrs[i][2] = rangeDestination;
                    // System.out.println("black range " + i + " Destination: " + rangeDestination);

                    rangeDestination = dinstream.readShort() & 0xff;
                    arrs[i][3] = rangeDestination;
                    // System.out.println("white range " + i + " Destination: " + rangeDestination);
                }
                setGrayBlendData(arrs);
            }
            dinstream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String toString() {
        StringBuilder sbuilder = new StringBuilder();
        sbuilder.append("Layer Blending Ranges Data Length: " + getLength());
        sbuilder.append("/grayBlend_Black_Source: " + grayBlend_Black_Source);
        sbuilder.append("/grayBlend_White_Source: " + grayBlend_White_Source);
        sbuilder.append("/grayBlend_Black_Destination: " + grayBlend_Black_Destination);
        sbuilder.append("/grayBlend_White_Destination: " + grayBlend_White_Destination);
        return sbuilder.toString();
    }
}
