package cn.imaginary.toolkit.image.photoshopdocument;

import java.io.IOException;
import java.io.RandomAccessFile;

public class ColorModeData {

    //2 Color Mode Data
    public ColorModeData() {}

    private int length_ColorData;

    private int colorMode;
    private static String[] arr_ColorMode_Name = {
        "Bitmap",
        "Grayscale",
        "Indexed",
        "RGB",
        "CMYK",
        "Unknown",
        "Unknown",
        "Multichannel",
        "Duotone",
        "Lab",
    };
    private String unknown = "Unknown";

    // private byte[] arr_ColorModeData;
    public int getColorMode() {
        return colorMode;
    }

    public String getColorModeName() {
        if (colorMode < 0 || colorMode >= arr_ColorMode_Name.length) {
            return unknown;
        } else {
            return arr_ColorMode_Name[colorMode];
        }
    }

    public int getLength() {
        return 4 + length_ColorData;
    }

    public void read(RandomAccessFile rafile, int colorMode) {
        try {
            long location = rafile.getFilePointer();

            //2.1 Color Data Length 4
            // The length of the following color data.
            length_ColorData = rafile.readInt();
            if (length_ColorData == 0) {} else {
                colorData(rafile, length_ColorData, colorMode);
            }

            System.out.println("location_colormodedata: " + rafile.getFilePointer());
            System.out.println();

            rafile.seek(location + getLength());
        } catch (IOException e) {}
    }

    private void colorData(RandomAccessFile rafile, int length, int colorMode) {
        //2.2 Color Data ？
        // The color data.
        try {
            this.colorMode = colorMode;
            // Only indexed color and duotone (see the mode field in the File header section) have color mode data. For all other modes, this section is just the 4-byte length field, which is set to zero.
            // Indexed color images: length is 768; color data contains the color table for the image, in non-interleaved order.
            // Duotone images: color data contains the duotone specification (the format of which is not documented). Other applications that read Photoshop files can treat a duotone image as a gray image, and just preserve the contents of the duotone information when reading and writing the file.
            int len;
            switch (colorMode) {
                case 2:
                    len = 768;
                    break;
                case 8:
                    len = length;
                    break;
                default:
                    len = 4;
                    break;
            }

            byte[] arr_ColorData = new byte[len];
            rafile.read(arr_ColorData);
            rafile.skipBytes(length - len);
        } catch (IOException e) {}
    }

    public String toString() {
        StringBuilder sbuilder = new StringBuilder();
        sbuilder.append("Color Mode: " + colorMode);
        sbuilder.append("/Color Mode Name: " + getColorModeName());
        sbuilder.append("/Color Data Length: " + length_ColorData);
        return sbuilder.toString();
    }
}
