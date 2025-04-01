package cn.imaginary.toolkit.image.photoshopdocument.imageresources;

import java.io.IOException;
import java.io.RandomAccessFile;

public class ImageResourceBlocks {

    public ImageResourceBlocks() {}

    private String signature;
    public static String Signature_ImageResourceBlocks = "8BIM";

    private int id_Resource;

    private int length_Name;

    private String name;

    private int length_ResourceData;

    private int length_ImageResourceBlocks;

    public String getSignature() {
        return signature;
    }

    public int getResourceID() {
        return id_Resource;
    }

    public int getNameLength() {
        return length_Name;
    }

    public String getName() {
        return name;
    }

    public int getLengthResourceData() {
        return length_ResourceData;
    }

    public int getLength() {
        return length_ImageResourceBlocks;
    }

    // public void read(RandomAccessFile rafile, int length) {
    public void read(RandomAccessFile rafile) {
        try {
            byte[] arr;

            /*long sign = rafile.getFilePointer();
			      System.out.println(length);
            arr = new byte[256];
            rafile.read(arr);
            for (int i = 0, len = arr.length; i < len; i++) {
                // System.out.println(arr[i] & 0xFF);
                System.out.println(arr[i]);
            }
            rafile.seek(sign);*/

            //3.2.1.1 signature:4
            // Signature: '8BIM'
            arr = new byte[4];
            rafile.read(arr);
            signature = new String(arr);

            if (!signature.equalsIgnoreCase(Signature_ImageResourceBlocks)) {
                rafile.seek(rafile.getFilePointer() - 4);
                throw new IOException("wrong Image Resource Blocks signature");
            }

            //3.2.1.2 Image Resource ID:2
            // Unique identifier for the resource. Image resource IDs contains a list of resource IDs used by Photoshop.
            id_Resource = rafile.readShort();

            //3.2.1.4 Name:?
            // Name: Pascal string, padded to make the size even (a null name consists of two bytes of 0)
            //3.2.1.3 Name Length:1
            // length_Name = rafile.readByte();
            length_Name = rafile.readByte() & 0xFF;
            /*if (length_Name == 0) {
                length_Name = 1;
            }*/
            if (length_Name % 2 == 0) {
                length_Name++;
            }

            arr = new byte[length_Name];
            rafile.read(arr);
            name = new String(arr);
            // name = new String(arr, "utf-8");
            // name = new String(arr, "utf-16");

            //3.2.1.5 Resource Data Size:4
            // Actual size of resource data that follows
            length_ResourceData = rafile.readInt();
            System.out.println("??? length: " + length_ResourceData);
            if (length_ResourceData % 2 != 0) {
                // length_ResourceData++;
                rafile.skipBytes(1);
            }

            int length_offset;
            length_offset = 4 + 2 + 1 + length_Name + 4;
            /*int length_ = length - length_offset;
            if (length_ResourceData > length_) {
                length_ResourceData = length_;
            }*/

            //3.2.1.6 Resource Data:?
            // The resource data, described in the sections on the individual resource types. It is padded to make the size even.
            rafile.skipBytes(length_ResourceData);
            /*ImageResourceIDs irIDs = new ImageResourceIDs();
            irIDs.read(rafile, length_ResourceData, id_Resource);*/

            // System.out.println(toString());
            length_ImageResourceBlocks = length_offset + length_ResourceData;
        } catch (IOException e) {}
    }

    public String toString() {
        StringBuilder sbuilder = new StringBuilder();
        sbuilder.append("signature: " + signature);
        sbuilder.append("/Resource ID :" + id_Resource);
        sbuilder.append("/Name Length: " + length_Name);
        sbuilder.append("/Name: " + name);
        sbuilder.append("/Resource Data Length: " + length_ResourceData);
        return sbuilder.toString();
    }
}
