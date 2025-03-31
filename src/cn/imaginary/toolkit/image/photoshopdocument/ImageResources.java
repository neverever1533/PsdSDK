package cn.imaginary.toolkit.image.photoshopdocument;

import cn.imaginary.toolkit.image.photoshopdocument.imageresources.ImageResourceBlocks;
import java.io.IOException;
import java.io.RandomAccessFile;

public class ImageResources {

    //3 Image Resources
    public ImageResources() {}

    private int length_ImageResources;

    public int getLength() {
        return 4 + length_ImageResources;
    }

    public void read(RandomAccessFile rafile) {
        try {
            long location = rafile.getFilePointer();

            //3.1 Image Resources length:4
            // Length of image resource section. The length may be zero.
            length_ImageResources = rafile.readInt();
            System.out.println("length_ImageResources: " + length_ImageResources);

            if (length_ImageResources > 0) {
                // Image resources (Image Resource Blocks ):?
                //3.2 Image Resource Blocks:?
                ImageResourceBlocks irblocks = new ImageResourceBlocks();
                int len;
                long point = 0;
                while (point < length_ImageResources) {
                    irblocks.read(rafile);
                    len = irblocks.getLength();
                    //if (irblocks.getSignature().equalsIgnoreCase(ImageResourceBlocks.Signature_ImageResourceBlocks)) {
                    System.out.println(irblocks.toString());
                    //}
                    point += len;
                    //rafile.skipBytes(len);
                }
                //rafile.skipBytes((int) (length_ImageResources - point));
            }
            System.out.println("3.location_ImageResources: " + rafile.getFilePointer());
            System.out.println();

            rafile.seek(location + getLength());
        } catch (IOException e) {}
    }

    public String toString() {
        StringBuilder sbuilder = new StringBuilder();
        sbuilder.append("Image Resources Length: " + length_ImageResources);
        return sbuilder.toString();
    }
}
