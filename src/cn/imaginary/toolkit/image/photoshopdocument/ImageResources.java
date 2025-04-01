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
                byte[] arr = new byte[4];
                String signature;
                while (point < length_ImageResources) {
                    // irblocks.read(rafile);
                    // len = irblocks.getLength();
                    rafile.read(arr);
                    signature = new String(arr);
                    rafile.seek(rafile.getFilePointer() - 4);
                    if (signature.equalsIgnoreCase(ImageResourceBlocks.Signature_ImageResourceBlocks)) {
                        irblocks.read(rafile);
                        // irblocks.read(rafile, length_ImageResources);
                        len = irblocks.getLength();
                        System.out.println(irblocks.toString());
                    } else {
                        len = (int) (length_ImageResources - point);
                        System.out.println("unknown data size: " + len);
                        /*byte[] array = new byte[16];
                        rafile.read(array);
                        for (int i = 0, length = array.length; i < length; i++) {
                            System.out.println(array[i]);
                        }
                        rafile.skipBytes(size - 16);*/
                        // throw new IOException("wrong Image Resource Blocks signature");
                    }
                    point += len;
                    System.out.println("point: " + point);
                }
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