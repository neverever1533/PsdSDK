package cn.imaginary.toolkit.image.photoshopdocument;

import cn.imaginary.toolkit.image.photoshopdocument.imageresources.ImageResourceBlocks;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

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
            long length = readData(rafile);

            length_ += length_Data;
            long space = length_ - length;
            System.out.println("image resources space: " + space);
            if (space > 0) {
                rafile.skipBytes((int) space);
            }
            rafile.seek(location + getLength());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private long readData(RandomAccessFile rafile) throws IOException {
        return readDataArray(rafile);
    }

    private long readDataArray(RandomAccessFile rafile) throws IOException {
        long location = rafile.getFilePointer();
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
                offset++;
                //                throw new IOException("The signature of the Image Resource Blocks is wrong.");
            }
        }
        return rafile.getFilePointer() - location;
    }

    public String toString() {
        StringBuilder sbuilder = new StringBuilder();
        sbuilder.append("Image Resources Length: " + getLength());
        return sbuilder.toString();
    }
}
