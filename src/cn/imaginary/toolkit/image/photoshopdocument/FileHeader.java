package cn.imaginary.toolkit.image.photoshopdocument;

import java.io.IOException;
import java.io.RandomAccessFile;

public class FileHeader {

    //1 FileHeader:26
    public FileHeader() {}

    private String signature;
    public static String Signature_FileHeader = "8BPS";

    private int version;
    public static int Version_PSD = 1;
    public static int Version_PSB = 2;
    private String[] arr_Version_Name = { "PSD", "PSB" };

    private boolean is_File_Psb = false;

    private int reserved;

    private int channels;
    private int channels_Min = 1;
    private int channels_Max = 56;

    private int height_Header;
    private int width_Header;
    private int pixels_Min = 1;
    private int pixels_Max_PSD = 30000;
    private int pixels_Max_PSB = 300000;

    private int depth;
    private int[] arr_Depth = { 1, 8, 16, 32 };

    private int colorMode;

    private String unknown = "Unknown";

    public int getVersion() {
        return version;
    }

    public String getVersionName() {
        if (version == Version_PSD || version == Version_PSB) {
            return arr_Version_Name[version - 1];
        } else {
            return unknown;
        }
    }

    public boolean isFilePsb() {
        return is_File_Psb;
    }

    public int getChannels() {
        return channels;
    }

    public int getHeight() {
        return height_Header;
    }

    public int getWidth() {
        return width_Header;
    }

    public int getDepth() {
        return depth;
    }

    public int getColorMode() {
        return colorMode;
    }

    public int getLength() {
        return 26;
    }

    public void read(RandomAccessFile rafile) {
        try {
            long location = rafile.getFilePointer();

            //1.1 Signature:4
            //Signature: always equal to '8BPS' . Do not try to read the file if the signature does not match this value.
            byte[] arr = new byte[4];
            rafile.read(arr);
            signature = new String(arr);
            if (!signature.equalsIgnoreCase(Signature_FileHeader)) {
                throw new IOException("signature does not match.");
            }

            //1.2 Version:2
            //Version: always equal to 1. Do not try to read the file if the version does not match this value. (**PSB** version is 2.)
            version = rafile.readShort();
            if (version == Version_PSD) {
                is_File_Psb = false;
            } else if (version == Version_PSB) {
                is_File_Psb = true;
            } else {
                throw new IOException("version does not match.");
            }

            //1.3 Reserved:6
            //Reserved: must be zero.
            // arr = new byte[6];
            // rafile.read(arr);
            rafile.skipBytes(6);
            reserved = 0;

            //1.4 Channels:2
            // The number of channels in the image, including any alpha channels. Supported range is 1 to 56.
            channels = rafile.readShort();
            if (channels < channels_Min || channels > channels_Max) {
                throw new IOException("the number of channels is wrong.");
            }

            //1.5 Height:4
            // The height of the image in pixels. Supported range is 1 to 30,000.
            // (**PSB** max of 300,000.)
            height_Header = rafile.readInt();
            if (
                (!is_File_Psb && height_Header >= pixels_Min && height_Header <= pixels_Max_PSD) ||
                (is_File_Psb && height_Header >= pixels_Min && height_Header <= pixels_Max_PSB)
            ) {} else {
                throw new IOException("the height of the image in pixels is wrong.");
            }

            //1.6 Width:4
            // The width of the image in pixels. Supported range is 1 to 30,000.
            // (*PSB** max of 300,000)
            width_Header = rafile.readInt();
            if (
                (!is_File_Psb && width_Header >= pixels_Min && width_Header <= pixels_Max_PSD) ||
                (is_File_Psb && width_Header >= pixels_Min && width_Header <= pixels_Max_PSB)
            ) {} else {
                throw new IOException("the width of the image in pixels is wrong.");
            }

            //1.7 Depth:2
            // Depth: the number of bits per channel. Supported values are 1, 8, 16 and 32.
            depth = rafile.readShort();
            switch (depth) {
                case 1:
                    break;
                case 8:
                    break;
                case 16:
                    break;
                case 32:
                    break;
                default:
                    throw new IOException("the number of bits per channel is not supported.");
            }

            //1.8 ColorMode:2
            // The color mode of the file. Supported values are: Bitmap = 0; Grayscale = 1; Indexed = 2; RGB = 3; CMYK = 4; Multichannel = 7; Duotone = 8; Lab = 9.
            colorMode = rafile.readShort();
            switch (colorMode) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 7:
                    break;
                case 8:
                    break;
                case 9:
                    break;
                default:
                    throw new IOException("the number of bits per channel is not supported.");
            }

            rafile.seek(location + getLength());
        } catch (IOException e) {}
    }

    public String toString() {
        StringBuilder sbuilder = new StringBuilder();
        sbuilder.append("FileHeader Length: " + getLength());
        sbuilder.append("/Signature: " + signature);
        sbuilder.append("/Version: " + getVersion());
        sbuilder.append("/Resvered: " + reserved);
        sbuilder.append("/Channels: " + getChannels());
        sbuilder.append("/Height: " + getHeight());
        sbuilder.append("/Width: " + getWidth());
        sbuilder.append("/Depth: " + getDepth());
        sbuilder.append("/Color Mode: " + getColorMode());
        return sbuilder.toString();
    }
}
