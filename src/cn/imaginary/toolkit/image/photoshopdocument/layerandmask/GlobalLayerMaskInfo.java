package cn.imaginary.toolkit.image.photoshopdocument.layerandmask;

import java.io.IOException;
import java.io.RandomAccessFile;

public class GlobalLayerMaskInfo {

    //4.3 Global Layer Mask Info
    public GlobalLayerMaskInfo() {}

    private int length_GlobalLayerMaskInfo;
    private int overlayColorSpace;
    private int opacity;
    private int kind;
    private int filler;

    public int getLength() {
        return 4 + length_GlobalLayerMaskInfo;
    }

    private int getOverlayColorSpace() {
        return overlayColorSpace;
    }

    private int getOpacity() {
        return opacity;
    }

    private int getKind() {
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
            int component1 = rafile.readShort();
            int component2 = rafile.readShort();
            int component3 = rafile.readShort();
            int component4 = rafile.readShort();

            //4.3.4 Opacity:2
            // Opacity. 0 = transparent, 100 = opaque.
            opacity = rafile.readShort();

            //4.3.5 Kind：1
            // Kind. 0 = Color selected--i.e. inverted; 1 = Color protected;128 = use value stored per layer. This value is preferred. The others are for backward compatibility with beta versions.
            kind = rafile.readByte() & 0xFF;

            //4.3.6 Filler: zeros：?
            int length_Filler = length_GlobalLayerMaskInfo - 2 - 8 - 2 - 1;
            rafile.skipBytes(length_Filler);
            filler = 0;

            System.out.println("location_globallayermaskinfo: " + rafile.getFilePointer());
            System.out.println();

            rafile.seek(location + getLength());
        } catch (IOException e) {}
    }

    public String toString() {
        StringBuilder sbuilder = new StringBuilder();
        sbuilder.append("length_GlobalLayerMaskInfo: " + length_GlobalLayerMaskInfo);
        sbuilder.append("/overlayColorSpace: " + overlayColorSpace);
        sbuilder.append("/component: " + component1 + "/" + component2 + "/" + component3 + "/" + component4);
        sbuilder.append("/opacity: " + opacity);
        sbuilder.append("/kind: " + kind);
        sbuilder.append("/filler: " + filler);
        return sbuilder.toString();
    }
}
