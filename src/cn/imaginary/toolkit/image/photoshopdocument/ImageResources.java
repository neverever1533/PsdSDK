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
    private int length_;
    private byte[] arr_Data;

    //3 Image Resources
    public ImageResources() {}

    public int getDataLength() {
        return length_Data;
    }

    public void setDataLength(int length) {
        length_Data = length;
    }

    public byte[] getData() {
        return arr_Data;
    }

    public void setData(byte[] array) {
        arr_Data = array;
    }

    public int getLength() {
        return length_;
    }

    public void setLength(int length) {
        length_ = length;
    }

    public ArrayList<ImageResourceBlocks> getArrayListImageResourceBlocks() {
        return arrayList_ImageResourceBlocks;
    }

    public void read(RandomAccessFile rafile) {
        try {
            readDataLength(rafile);
            readData(rafile, getDataLength());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void readDataLength(RandomAccessFile rafile) throws IOException {
        //3.1 Image Resources length:4
        //4,Length of image resource section. The length may be zero.
        setDataLength(rafile.readInt());
        setLength(4 + getDataLength());
    }

    private void readData(RandomAccessFile rafile, int length) throws IOException {
        if (length > 0) {
            byte[] arr = new byte[length];
            rafile.read(arr);
            setData(arr);
            readDataArray(arr);
        }
    }

    private void readDataArray(byte[] array) {
        try {
            DataInputStream dinstream = new DataInputStream(new ByteArrayInputStream(array));
            //3.2 Image resources:Variable
            //Variable,Image resources (Image Resource Blocks ).
            byte[] arr;
            String signature;
            long len;
            int offset = 0;
            arrayList_ImageResourceBlocks = new ArrayList<ImageResourceBlocks>();
            while (offset < array.length) {
                dinstream.mark(offset);
                arr = new byte[4];
                dinstream.read(arr);
                signature = new String(arr);
                dinstream.reset();
                if (signature.equalsIgnoreCase(ImageResourceBlocks.Signature_8BIM)) {
                    ImageResourceBlocks irblocks = new ImageResourceBlocks();
                    irblocks.read(dinstream);
                    arrayList_ImageResourceBlocks.add(irblocks);
                    offset += irblocks.getLength();
                    System.out.println(irblocks.toString());
                } else {
                    offset++;
                    //                throw new IOException("The signature of the Image Resource Blocks is wrong.");
                }
            }
            dinstream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String toString() {
        StringBuilder sbuilder = new StringBuilder();
        sbuilder.append("Image Resources Length: " + getLength());
        return sbuilder.toString();
    }
}
