package cn.imaginary.toolkit.image.photoshopdocument;

import cn.imaginary.toolkit.image.photoshopdocument.imageresources.ImageResourceBlocks;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class ImageResources {

    //3 Image Resources
    public ImageResources() {}

    private ArrayList arrayList_ImageResourceBlocks;

    private int length_ImageResources;

    public int getLength() {
        return 4 + length_ImageResources;
    }

    public ArrayList getArrayListImageResourceBlocks() {
        return arrayList_ImageResourceBlocks;
    }

    public void read(RandomAccessFile rafile) {
        try {
            long location = rafile.getFilePointer();

            //3.1 Image Resources length:4
            // Length of image resource section. The length may be zero.
            length_ImageResources = rafile.readInt();

            if (length_ImageResources > 0) {
                // Image resources (Image Resource Blocks ):?
                //3.2 Image Resource Blocks:?
                ImageResourceBlocks irblocks = new ImageResourceBlocks();
                int len;
                long point = 0;
                byte[] arr = new byte[4];
                String signature;
                arrayList_ImageResourceBlocks = new ArrayList<ImageResourceBlocks>();
                while (point < length_ImageResources) {
                    // irblocks.read(rafile);
                    // len = irblocks.getLength();
                    rafile.read(arr);
                    signature = new String(arr);
                    rafile.seek(rafile.getFilePointer() - 4);
                    if (signature.equalsIgnoreCase(ImageResourceBlocks.Signature_ImageResourceBlocks)) {
                        irblocks.read(rafile);
                        arrayList_ImageResourceBlocks.add(irblocks);
                        len = irblocks.getLength();
                        System.out.println(irblocks.toString());
                    } else {
                        len = (int) (length_ImageResources - point);
                        System.out.println("unknown data size: " + len);
                        rafile.skipBytes(len);
                    }
                    point += len;
                    System.out.println("point: " + point);
                }
            }

            rafile.seek(location + getLength());
        } catch (IOException e) {}
    }

    public String toString() {
        StringBuilder sbuilder = new StringBuilder();
        sbuilder.append("Image Resources Length: " + getLength());
        return sbuilder.toString();
    }
}
