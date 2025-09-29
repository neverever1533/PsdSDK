package cn.imaginary.toolkit.image.photoshopdocument;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileHeader {

    public static String Signature_8BPS = "8BPS";

    public static int Version_PSB = 2;
    public static int Version_PSD = 1;
    public static int Depth_1 = 1;
    public static int Depth_8 = 8;
    public static int Depth_16 = 16;
    public static int Depth_32 = 32;

    private String signature;

    private String[] arr_Version = { "Unknown", "PSD", "PSB" };

    private int channels;
    private int channels_Min = 1;
    private int channels_Max = 56;
    private int colorMode;
    private int depth;
    private int height;
    private int width;
    private int pixels_Min = 1;
    private int pixels_Max_PSD = 30000;
    private int pixels_Max_PSB = 300000;
    private int reserved;
    private int version;

    private int length_;

    private byte[] arr_Reserved = new byte[6];

    //1 FileHeader:26
    public FileHeader() {}

    public String getSignature() {
        return signature;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) throws IOException {
        if (version == 1 || version == 2) {
            this.version = version;
        } else {
            throw new IOException("The version of the File Header is wrong.");
        }
    }

    public String getVersionName() {
        return arr_Version[version];
    }

    public boolean isFilePsb() {
        return version == 2;
    }

    public int getChannels() {
        return channels;
    }

    public void setChannels(int channels) throws IOException {
        if (channels >= channels_Min && channels <= channels_Max) {
            this.channels = channels;
        } else {
            throw new IOException("The number of channels is wrong.");
        }
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) throws IOException {
        if (height >= pixels_Min && (height <= pixels_Max_PSD || (isFilePsb() && height <= pixels_Max_PSB))) {
            this.height = height;
        } else {
            throw new IOException("The height of the image is wrong.");
        }
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) throws IOException {
        if (width >= pixels_Min && (width <= pixels_Max_PSD || (isFilePsb() && width <= pixels_Max_PSB))) {
            this.width = width;
        } else {
            throw new IOException("The width of the image is wrong.");
        }
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) throws IOException {
        switch (depth) {
            case 1:
                this.depth = Depth_1;
                break;
            case 8:
                this.depth = Depth_8;
                break;
            case 16:
                this.depth = Depth_16;
                break;
            case 32:
                this.depth = Depth_32;
                break;
            default:
                throw new IOException("the number of bits per channel is Wrong.");
        }
    }

    public String getColorModeName() {
        return ColorModeData.getColorModeName(colorMode);
    }

    public int getColorMode() {
        return colorMode;
    }

    public void setColorMode(int colorMode) throws IOException {
        if (ColorModeData.isSupported(colorMode)) {
            this.colorMode = colorMode;
        } else {
            throw new IOException("The color mode of the file is wrong.");
        }
    }

    public int getReserved() {
        return reserved;
    }

    public void setReserved(int reserved) {
        if (reserved != 0) {
            reserved = 0;
        }
        this.reserved = reserved;
    }

    public long getLength() {
        return length_;
    }

    private void readSignature(RandomAccessFile rafile) throws IOException {
        //1.1 Signature:4
        //4,Signature: always equal to '8BPS' . Do not try to read the file if the signature does not match this value.
        byte[] arr = new byte[4];
        rafile.read(arr);
        length_ += 4;
        signature = new String(arr);
        if (!signature.equalsIgnoreCase(Signature_8BPS)) {
            throw new IOException("The Signature of the File Header is wrong.");
        }
    }

    private void readVersion(RandomAccessFile rafile) throws IOException {
        //1.2 Version:2
        //2,Version: always equal to 1. Do not try to read the file if the version does not match this value. (**PSB** version is 2.)
        version = rafile.readShort();
        length_ += 2;
    }

    private void readReserved(RandomAccessFile rafile) throws IOException {
        //1.3 Reserved:6
        //6,Reserved: must be zero.
        rafile.skipBytes(6);
        length_ += 6;
        reserved = 0;
    }

    private void readChannels(RandomAccessFile rafile) throws IOException {
        //1.4 Channels:2
        //2,The number of channels in the image, including any alpha channels. Supported range is 1 to 56.
        channels = rafile.readShort();
        length_ += 2;
    }

    private void readHeight(RandomAccessFile rafile) throws IOException {
        //1.5 Height:4
        //4,The height of the image in pixels. Supported range is 1 to 30,000.(**PSB** max of 300,000.)
        height = rafile.readInt();
        length_ += 4;
    }

    private void readWidth(RandomAccessFile rafile) throws IOException {
        //1.6 Width:4
        //4,The width of the image in pixels. Supported range is 1 to 30,000.(*PSB** max of 300,000)
        width = rafile.readInt();
        length_ += 4;
    }

    private void readDepth(RandomAccessFile rafile) throws IOException {
        //1.7 Depth:2
        //2,Depth: the number of bits per channel. Supported values are 1, 8, 16 and 32.
        depth = rafile.readShort();
        length_ += 2;
    }

    private void readColorMode(RandomAccessFile rafile) throws IOException {
        //1.8 ColorMode:2
        //2,The color mode of the file. Supported values are: Bitmap = 0; Grayscale = 1; Indexed = 2; RGB = 3; CMYK = 4; Multichannel = 7; Duotone = 8; Lab = 9.
        colorMode = rafile.readShort();
        length_ += 2;
    }

    public void read(RandomAccessFile rafile) {
        try {
            long location = rafile.getFilePointer();

            readData(rafile);

            rafile.seek(location + getLength());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void readData(RandomAccessFile rafile) throws IOException {
        readSignature(rafile);
        readVersion(rafile);
        readReserved(rafile);
        readChannels(rafile);
        readHeight(rafile);
        readWidth(rafile);
        readDepth(rafile);
        readColorMode(rafile);
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
        sbuilder.append("/Color Mode Name: " + getColorModeName());
        return sbuilder.toString();
    }
}
