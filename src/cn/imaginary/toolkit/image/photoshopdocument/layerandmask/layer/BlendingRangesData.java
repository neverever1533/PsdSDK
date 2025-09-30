package cn.imaginary.toolkit.image.photoshopdocument.layerandmask.layer;

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

    public BlendingRangesData() {}

    public int getLength() {
        return length_;
    }

    public int[][] getGrayBlendArrays() {
        return arrs_grayBlend;
    }

    public void read(RandomAccessFile rafile) {
        try {
            long location = rafile.getFilePointer();

            readDataLength(rafile);
            readData(rafile);

            length_ += length_Data;
//            System.out.println("blending ranges data space: " + (location + length_ - rafile.getFilePointer()));
            rafile.seek(location + getLength());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void readData(RandomAccessFile rafile) throws IOException {
        readDataArray(rafile);
    }

    private void readDataArray(RandomAccessFile rafile) throws IOException {
        readGrayBlend(rafile);
        readChannelsSource(rafile);
    }

    private void readChannelsSource(RandomAccessFile rafile) throws IOException {
        int count = (length_Data - 8) / 8;
        if (count > 0) {
            arrs_grayBlend = new int[4][count];

            int rangeSource;
            int rangeDestination;
            for (int i = 0; i < count; i++) {
                // First channel source range:4
                // rangeSource = rafile.readInt();
                rangeSource = rafile.readShort() & 0xff;
                // System.out.println("black range " + i + " Source: " + rangeSource);

                arrs_grayBlend[0][i] = rangeSource;
                rangeSource = rafile.readShort() & 0xff;
                // System.out.println("white range " + i + " Source: " + rangeSource);

                arrs_grayBlend[1][i] = rangeSource;
                // First channel destination range:4
                // rangeSestination = rafile.readInt();
                rangeDestination = rafile.readShort() & 0xff;
                // System.out.println("black range " + i + " Destination: " + rangeDestination);

                arrs_grayBlend[2][i] = rangeDestination;
                rangeDestination = rafile.readShort() & 0xff;
                // System.out.println("white range " + i + " Destination: " + rangeDestination);

                arrs_grayBlend[3][i] = rangeDestination;
            }
        }
    }

    private void readGrayBlend(RandomAccessFile rafile) throws IOException {
        // Composite gray blend source. Contains 2 black values followed by 2 white values. Present but irrelevant for Lab & Grayscale.:4
        grayBlend_Black_Source = rafile.readShort() & 0xff;
        grayBlend_White_Source = rafile.readShort() & 0xff;

        // Composite gray blend destination range:4
        // grayBlend_Destination = rafile.readInt();
        grayBlend_Black_Destination = rafile.readShort() & 0xff;
        grayBlend_White_Destination = rafile.readShort() & 0xff;
    }

    private void readDataLength(RandomAccessFile rafile) throws IOException {
        // Length of layer blending ranges data:4
        length_Data = rafile.readInt();
        length_ += 4;
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