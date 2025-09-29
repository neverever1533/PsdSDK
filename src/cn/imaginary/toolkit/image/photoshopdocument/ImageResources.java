package cn.imaginary.toolkit.image.photoshopdocument;

import cn.imaginary.toolkit.image.photoshopdocument.imageresources.ImageResourceBlocks;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;

public class ImageResources {

    private ArrayList<ImageResourceBlocks> arrayList_ImageResourceBlocks;
    private int length_Data;
    private long length_;

    //3 Image Resources
    public ImageResources() {}

    public int getDataLength() {
        return length_Data;
    }

    public void setDataLength(int length) {
        length_Data = length;
    }

    public long getLength() {
        return length_;
    }

    public ArrayList<ImageResourceBlocks> getArrayListImageResourceBlocks() {
        return arrayList_ImageResourceBlocks;
    }

    private void readDataLength(RandomAccessFile rafile) throws IOException {
        //3.1 Image Resources length:4
        //4,Length of image resource section. The length may be zero.
        length_Data = rafile.readInt();
        length_ += 4;
    }

    public void read(RandomAccessFile rafile) {
        try {
            long location = rafile.getFilePointer();

            readDataLength(rafile);
            readData(rafile);

            length_ += length_Data;
            rafile.seek(location + getLength());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void readData(RandomAccessFile rafile) throws IOException {
        readDataArray(rafile);
    }

    private void readDataArray(RandomAccessFile rafile) throws IOException {
        //3.2 Image resources:Variable
        //Variable,Image resources (Image Resource Blocks ).
        byte[] arr;
        String signature;
        long len;
        long offset = 0;
        arrayList_ImageResourceBlocks = new ArrayList<ImageResourceBlocks>();
        while (offset < length_Data) {
            arr = new byte[4];
            rafile.read(arr);
            signature = new String(arr);
            rafile.seek(rafile.getFilePointer() - 4);
            if (signature.equalsIgnoreCase(ImageResourceBlocks.Signature_8BIM)) {
                ImageResourceBlocks irblocks = new ImageResourceBlocks();
                irblocks.read(rafile);
                arrayList_ImageResourceBlocks.add(irblocks);
                len = irblocks.getLength();
                offset += len;
                System.out.println(irblocks.toString());
            } else {
                throw new IOException("The signature of the Image Resource Blocks is wrong.");
                /* len = length_Data - offset;
                System.out.println("unknown data size: " + len);
                rafile.skipBytes(len); */
//                 arr = new byte[(int) len];
//                 rafile.read(arr);
//                 codePreview(arr);
            }
        }
    }

    public String toString() {
        StringBuilder sbuilder = new StringBuilder();
        sbuilder.append("Image Resources Length: " + getLength());
        return sbuilder.toString();
    }
}
