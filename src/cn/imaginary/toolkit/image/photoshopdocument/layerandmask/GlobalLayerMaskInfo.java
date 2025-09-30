package cn.imaginary.toolkit.image.photoshopdocument.layerandmask;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public class GlobalLayerMaskInfo {

    private int length_;
    private int length_Data;
    private int overlayColorSpace;
    private int red;
    private int green;
    private int blue;
    private int alpha;
    private int opacity;
    private int kind;
    private int filler;
    private int length_Filler;

    //4.3 Global Layer Mask Info
    public GlobalLayerMaskInfo() {}

    public int getLength() {
        return length_;
    }

    public int getOverlayColorSpace() {
        return overlayColorSpace;
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    public int getAlpha() {
        return alpha;
    }

    public int getOpacity() {
        return opacity;
    }

    public int getKind() {
        return kind;
    }

    public void read(RandomAccessFile rafile) {
        try {
            long location = rafile.getFilePointer();

            readDataLength(rafile);
            readData(rafile);

            length_ += length_Data;
            System.out.println("global layer mask info space: " + (location + length_ - rafile.getFilePointer()));
            rafile.seek(location + getLength());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void readData(RandomAccessFile rafile) throws IOException {
        if (length_Data > 0) {
            readDataArray(rafile);
        }
    }

    public void readDataArray(RandomAccessFile rafile) throws IOException {
        readOverlayColorSpace(rafile);
        readColorComponents(rafile);
        readOpacity(rafile);
        readKind(rafile);
        readFiller(rafile);
    }

    private void readFiller(RandomAccessFile rafile) throws IOException {
        //4.3.6 Filler: zeros：?
        length_Filler = length_Data - length_;

        rafile.skipBytes(length_Filler);
        filler = 0;
    }

    private void readKind(RandomAccessFile rafile) throws IOException {
        //4.3.5 Kind：1
        // Kind. 0 = Color selected--i.e. inverted; 1 = Color protected;128 = use value stored per layer. This value is preferred. The others are for backward compatibility with beta versions.
        kind = rafile.readByte() & 0xFF;
        length_ += 1;
    }

    private void readOpacity(RandomAccessFile rafile) throws IOException {
        //4.3.4 Opacity:2
        // Opacity. 0 = transparent, 100 = opaque.
        opacity = rafile.readShort();
        length_ += 2;
    }

    private void readColorComponents(RandomAccessFile rafile) throws IOException {
        //4.3.3 Color Components:8
        // 4 * 2 byte color components
        red = rafile.readShort();
        green = rafile.readShort();
        blue = rafile.readShort();
        alpha = rafile.readShort();
        length_ += 8;
    }

    private void readOverlayColorSpace(RandomAccessFile rafile) throws IOException {
        //4.3.2 Overlay Color Space(undocumented）：2
        overlayColorSpace = rafile.readShort();
        length_ += 2;
    }

    private void readDataLength(RandomAccessFile rafile) throws IOException {
        //4.3.1 Global Layer Mask Info Length:4
        // Length of global layer mask info section.
        length_Data = rafile.readInt();
        length_ += 4;
    }

    public String toString() {
        StringBuilder sbuilder = new StringBuilder();
        sbuilder.append("Global Layer Mask Info Length: " + getLength());
        sbuilder.append("/overlayColorSpace: " + overlayColorSpace);
        sbuilder.append("/color: " + red + "/" + green + "/" + blue + "/" + alpha);
        sbuilder.append("/opacity: " + opacity);
        sbuilder.append("/kind: " + kind);
        sbuilder.append("/filler: " + filler);
        return sbuilder.toString();
    }
}
