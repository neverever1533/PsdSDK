package cn.imaginary.toolkit.image.photoshopdocument.layerandmask;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

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

    private byte[] arr_Data;
    private byte[] arr_Filler;

    //Global Layer Mask Info
    public GlobalLayerMaskInfo() {}

    public int getDataLength() {
        return length_Data;
    }

    public void setDataLength(int length) {
        length_Data = length;
    }

    public int getLength() {
        return length_;
    }

    public void setLength(int length) {
        length_ = length;
    }

    public int getOverlayColorSpace() {
        return overlayColorSpace;
    }

    public void setOverlayColorSpace(int overlayColorSpace) {
        this.overlayColorSpace = overlayColorSpace;
    }

    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getGreen() {
        return green;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public int getBlue() {
        return blue;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

    public int getAlpha() {
        return alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    public int getOpacity() {
        return opacity;
    }

    public void setOpacity(int opacity) {
        this.opacity = opacity;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    public byte[] getData() {
        return arr_Data;
    }

    public void setData(byte[] array) {
        arr_Data = array;
    }

    public int getFiller() {
        return filler;
    }

    public void setFiller(int filler) throws IOException {
        if (filler != 0) {
            throw new IOException("The filler of the GlobalLayerMaskInfo is wrong.");
        }
        this.filler = filler;
    }

    public byte[] getFillerData() {
        return arr_Filler;
    }

    public void setFillerData(byte[] array) throws IOException {
        if (null == array) {
            throw new IOException("The filler of the GlobalLayerMaskInfo is wrong.");
        }
        for (int i = 0; i < array.length; i++) {
            if (array[i] != 0) {
                throw new IOException("The filler of the GlobalLayerMaskInfo is wrong.");
            }
        }
        arr_Filler = array;
        setFiller(0);
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
        //Global Layer Mask Info Length:4
        //4,Length of global layer mask info section.
        setDataLength(dinstream.readInt());
        setLength(4 + getDataLength());
    }

    public void readData(DataInputStream dinstream, int length) throws IOException {
        if (length > 0) {
            byte[] arr = new byte[length];
            dinstream.read(arr);
            setData(arr);
            readDataArray(arr);
        }
    }

    public void readDataArray(byte[] array) {
        try {
            DataInputStream dinstream = new DataInputStream(new ByteArrayInputStream(array));
            //Overlay Color Space:2
            //2,Overlay Color Space(undocumented）
            setOverlayColorSpace(dinstream.readShort() & 0xFF);

            //Color Components:8
            //4 * 2,byte color components
            setRed(dinstream.readShort() & 0xFF);
            setGreen(dinstream.readShort() & 0xFF);
            setBlue(dinstream.readShort() & 0xFF);
            setAlpha(dinstream.readShort() & 0xFF);

            //Opacity:2
            //2,Opacity. 0 = transparent, 100 = opaque.
            setOpacity((dinstream.readShort() & 0xFF) % 100);

            //Kind：1
            //1,Kind. 0 = Color selected--i.e. inverted; 1 = Color protected;128 = use value stored per layer. This value is preferred. The others are for backward compatibility with beta versions.
            setKind(dinstream.readByte() & 0xFF);

            //Filler: zeros:Variable
            int length_Filler = array.length - 2 - 8 - 2 - 1;
            if (length_Filler > 0) {
                byte[] arr = new byte[length_Filler];
                dinstream.read(arr);
                setFillerData(arr);
            }
            dinstream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String toString() {
        StringBuilder sbuilder = new StringBuilder();
        sbuilder.append("Global Layer Mask Info Length: " + getLength());
        sbuilder.append("/overlayColorSpace: " + overlayColorSpace);
        sbuilder.append("/red: " + red + "/green: " + green + "/blue: " + blue + "/alpha: " + alpha);
        sbuilder.append("/opacity: " + opacity);
        sbuilder.append("/kind: " + kind);
        sbuilder.append("/filler: " + filler);
        return sbuilder.toString();
    }
}
