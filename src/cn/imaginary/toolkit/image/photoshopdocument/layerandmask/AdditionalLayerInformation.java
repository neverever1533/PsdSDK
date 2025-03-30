package cn.imaginary.toolkit.image.photoshopdocument.layerandmask;

import java.io.IOException;
import java.io.RandomAccessFile;

public class AdditionalLayerInformation {

    //4.4 Additional Layer Information
    public AdditionalLayerInformation() {}

    private byte[] arr_Data;

    private int length_AdditionalLayerInformation;

    private int length_Data;

    private int key;

    private String signature;
    public static String signature_AdditionalLayerInformation = "8BIM";
    public static String signature_AdditionalLayerInformation_ = "8B64";

    private int getLength() {
        return length_AdditionalLayerInformation;
    }

    public void read(RandomAccessFile rafile) {
        try {
            //4.4 Additional Layer Information: ?
            long location = rafile.getFilePointer();

            //4.4.1 Signature：4
            // Signature: '8BIM' or '8B64'
            byte[] arr = new byte[4];
            rafile.read(arr);
            signature = new String(arr);
            System.out.println("signature: " + signature);
            if (
                !(signature.equalsIgnoreCase(signature_AdditionalLayerInformation) ||
                    signature.equalsIgnoreCase(signature_AdditionalLayerInformation_))
            ) {
                throw new IOException("Wrong Additional Layer Information Signature");
            }

            //4.4.2 Key：4
            //Key: a 4-character code (See individual sections)
            key = rafile.readInt();
            System.out.println("key: " + key);

            //4.4.3 Data below Length：4
            //Length data below, rounded up to an even byte count.
            // (**PSB**, the following keys have a length count of 8 bytes: LMsk, Lr16, Lr32, Layr, Mt16, Mt32, Mtrn, Alph, FMsk, lnk2, FEid, FXid, PxSD.
            length_Data = rafile.readInt();
            if (length_Data % 2 != 0) {
                length_Data++;
            }
            System.out.println("length_Data: " + length_Data);

            //4.4.4 Data：?
            // Data (See individual sections)
            arr_Data = new byte[length_Data];
            rafile.read(arr_Data);

            length_AdditionalLayerInformation = 4 + 4 + 4 + length_Data;

            System.out.println("location_globallayermaskinfo: " + rafile.getFilePointer());
            System.out.println();

            rafile.seek(location + getLength());
        } catch (IOException e) {}
    }
}
