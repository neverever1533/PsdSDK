package cn.imaginary.toolkit.image.photoshopdocument.layerandmask.mask;

import java.io.IOException;
import java.io.RandomAccessFile;

public class LayerBlendingRangesData {

    public LayerBlendingRangesData() {}

    private int length_BlendingRanges;
    private int grayBlend_Black;
    private int grayBlend_White;
    private int grayBlend_Destination;

    public int getLength() {
        return 4 + length_BlendingRanges;
    }

    public void read(RandomAccessFile rafile) {
        try {
            long location = rafile.getFilePointer();
            // Length of layer blending ranges data:4
            length_BlendingRanges = rafile.readInt();

            // Composite gray blend source. Contains 2 black values followed by 2 white values. Present but irrelevant for Lab & Grayscale.:4
            grayBlend_Black = rafile.readShort();
            grayBlend_White = rafile.readShort();

            // Composite gray blend destination range:4
            grayBlend_Destination = rafile.readInt();

            int count = (length_BlendingRanges - 4 - 4) / 8;
            int rangeSource;
            int rangeSestination;
            for (int i = 0; i < count; i++) {
                // First channel source range:4
                rangeSource = rafile.readInt();
                System.out.println("range " + i + " Source: " + rangeSource);
                // First channel destination range:4
                rangeSestination = rafile.readInt();
                System.out.println("range " + i + " Sestination: " + rangeSestination);
            }
            rafile.seek(location + getLength());
        } catch (IOException e) {}
    }

    public String toString() {
        StringBuilder sbuilder = new StringBuilder();
        sbuilder.append("Layer Blending Ranges Data Length: " + getLength());
        sbuilder.append("/grayBlend_Black: " + grayBlend_Black);
        sbuilder.append("/grayBlend_White: " + grayBlend_White);
        sbuilder.append("/grayBlend_Destination: " + grayBlend_Destination);
        return sbuilder.toString();
    }
}
