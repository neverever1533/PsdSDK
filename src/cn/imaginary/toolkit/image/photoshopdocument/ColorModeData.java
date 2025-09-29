package cn.imaginary.toolkit.image.photoshopdocument;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public class ColorModeData {

    public static int Bitmap = 0;
    public static int Grayscale = 1;
    public static int Indexed = 2;
    public static int RGB = 3;
    public static int CMYK = 4;
    public static int Multichannel = 7;
    public static int Duotone = 8;
    public static int Lab = 9;

    public static String[] arr_ColorMode = {
        "Bitmap",
        "Grayscale",
        "Indexed",
        "RGB",
        "CMYK",
        "Multichannel",
        "Duotone",
        "Lab",
    };

    private int length_Data;
    private int colorMode;

    private long length_;

    private byte[] arr_Data;

    //2 Color Mode Data
    public ColorModeData() {}

    public static boolean isSupported(int colorMode) {
        if ((colorMode >= 0 && colorMode <= 4) || (colorMode >= 7 && colorMode <= 9)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isSupported(String colorModeName) {
        if (null != colorModeName) {
            for (int i = 0; i < arr_ColorMode.length; i++) {
                if (colorModeName.equalsIgnoreCase(arr_ColorMode[i])) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String getColorModeName(int colorMode) {
        if (isSupported(colorMode)) {
            return arr_ColorMode[colorMode];
        } else {
            return null;
        }
    }

    public byte[] getData() {
        return arr_Data;
    }

    public int getColorMode() {
        return colorMode;
    }

    public void setColorMode(int colorMode) throws IOException {
        switch (colorMode) {
            case 0:
                this.colorMode = Bitmap;
                break;
            case 1:
                this.colorMode = Grayscale;
                break;
            case 2:
                this.colorMode = Indexed;
                break;
            case 3:
                this.colorMode = RGB;
                break;
            case 4:
                this.colorMode = CMYK;
                break;
            case 7:
                this.colorMode = Multichannel;
                break;
            case 8:
                this.colorMode = Duotone;
                break;
            case 9:
                this.colorMode = Lab;
                break;
            default:
                throw new IOException("The color mode of the file is wrong.");
        }
    }

    public String getColorModeName() {
        if (colorMode >= 7 && colorMode <= 9) {
            return arr_ColorMode[colorMode - 2];
        } else {
            return arr_ColorMode[colorMode];
        }
    }

    public long getLength() {
        return length_;
    }

    private void readDataLength(RandomAccessFile rafile) throws IOException {
        //2.1 Color Data Length:4
        //4,The length of the following color data.
        int length = rafile.readInt();
        length_ += 4;
        //Only indexed color and duotone (see the mode field in the File header section) have color mode data. For all other modes, this section is just the 4-byte length field, which is set to zero.
        //Indexed color images: length is 768; color data contains the color table for the image, in non-interleaved order.
        //Duotone images: color data contains the duotone specification (the format of which is not documented). Other applications that read Photoshop files can treat a duotone image as a gray image, and just preserve the contents of the duotone information when reading and writing the file.

        /*switch (colorMode) {
            case 2:
                length_Data = 768;
                break;
            case 8:
                length_Data = length;
                break;
            default:
                length_Data = 4;
                break;
        }*/

        length_Data = length;
    }

    public void read(RandomAccessFile rafile, int colorMode) {
        try {
            long location = rafile.getFilePointer();

            setColorMode(colorMode);
            readDataLength(rafile);
            readData(rafile);

            length_ += length_Data;
            rafile.seek(location + getLength());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void readDataArray(byte[] array) {
        // codePreview(array);
    }

    private void readData(RandomAccessFile rafile) throws IOException {
        //2.2 Color Data:Variable
        //Variable,The color data.
        if (length_Data > 0) {
            byte[] arr = new byte[length_Data];
            rafile.read(arr);
            arr_Data = arr;
            readDataArray(arr);
        }
    }

    public String toString() {
        StringBuilder sbuilder = new StringBuilder();
        sbuilder.append("Color Mode Data Length: " + getLength());
        sbuilder.append("/Color Mode: " + colorMode);
        sbuilder.append("/Color Mode Name: " + getColorModeName(colorMode));
        sbuilder.append("/Color Data Length: " + length_Data);
        return sbuilder.toString();
    }
}
