package cn.imaginary.toolkit.image.photoshopdocument.layerandmask;

import java.io.IOException;
import java.io.RandomAccessFile;

public class GlobalLayerMaskInfo {

    //4.3 Global Layer Mask Info
    public GlobalLayerMaskInfo() {}

    private int length_GlobalLayerMaskInfo;
    private int overlayColorSpace;

    private int red;
    private int green;
    private int blue;
    private int alpha;

    private int opacity;
    private int kind;
    private int filler;

    public int getLength() {
        return 4 + length_GlobalLayerMaskInfo;
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

            //4.3.1 Global Layer Mask Info Length:4
            // Length of global layer mask info section.
            length_GlobalLayerMaskInfo = rafile.readInt();

            //4.3.2 Overlay Color Space(undocumented）：2
            overlayColorSpace = rafile.readShort();

            //4.3.3 Color Components:8
            // 4 * 2 byte color components
            red = rafile.readShort();
            green = rafile.readShort();
            blue = rafile.readShort();
            alpha = rafile.readShort();

            //4.3.4 Opacity:2
            // Opacity. 0 = transparent, 100 = opaque.
            opacity = rafile.readShort();

            //4.3.5 Kind：1
            // Kind. 0 = Color selected--i.e. inverted; 1 = Color protected;128 = use value stored per layer. This value is preferred. The others are for backward compatibility with beta versions.
            kind = rafile.readByte() & 0xFF;

            //4.3.6 Filler: zeros：?
            int length_Filler = length_GlobalLayerMaskInfo - 2 - 8 - 2 - 1;
            if (length_Filler > 0) {
                byte[] arr = new byte[length_Filler];
                rafile.read(arr);
                System.out.println("filler: ");
                for (int i = 0; i < arr.length; i++) {
                    System.out.println(arr[i]);
                }
            }
            // rafile.skipBytes(length_Filler);
            // filler = 0;

            rafile.seek(location + getLength());
        } catch (IOException e) {}
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
