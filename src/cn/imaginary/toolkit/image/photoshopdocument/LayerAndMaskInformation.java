package cn.imaginary.toolkit.image.photoshopdocument;

import cn.imaginary.toolkit.image.photoshopdocument.layerandmask.AdditionalLayerInformation;
import cn.imaginary.toolkit.image.photoshopdocument.layerandmask.GlobalLayerMaskInfo;
import cn.imaginary.toolkit.image.photoshopdocument.layerandmask.LayerInfo;
import java.io.IOException;
import java.io.RandomAccessFile;

public class LayerAndMaskInformation {

    //4 Layer and Mask Information
    public LayerAndMaskInformation() {}

    private int length_LayerAndMaskInformation;

    public int getLength() {
        return 4 + length_LayerAndMaskInformation;
    }

    public void read(RandomAccessFile rafile, FileHeader fheader) {
        try {
            long location = rafile.getFilePointer();

            //4.1 Layer and Mask Information Length 4
            // Length of the layer and mask information section. (**PSB** length is 8 bytes.)
            int version = fheader.getVersion();
            if (version == FileHeader.Version_PSD) {
                length_LayerAndMaskInformation = rafile.readInt();
            } else if (version == FileHeader.Version_PSB) {
              byte[] arr=new byte[8];
                length_LayerAndMaskInformation = rafile.read(arr);
            }

            //4.2 Layer Info:?
            // Layer info (see See Layer info for details).
            LayerInfo linfo = new LayerInfo();
            linfo.read(rafile);
            System.out.println(linfo.toString());

            //4.3 Global Layer Mask Info:?
            // Global layer mask info (see See Global layer mask info for details).
            GlobalLayerMaskInfo glminfo = new GlobalLayerMaskInfo();
            glminfo.read(rafile);
            System.out.println(glminfo.toString());

            //4.4 Additional Layer Information:?
            // (Photoshop 4.0 and later)
            // Series of tagged blocks containing various types of data. See See Additional Layer Information for the list of the types of data that can be included here.
            AdditionalLayerInformation alinfo = new AdditionalLayerInformation();
            alinfo.read(rafile);
            System.out.println(alinfo.toString());

            System.out.println("4.location_LayerAndMaskInformation: " + rafile.getFilePointer());
            System.out.println();

            rafile.seek(location + getLength());
        } catch (IOException e) {}
        System.out.println();
    }

    public String toString() {
        StringBuilder sbuilder = new StringBuilder();
        sbuilder.append("Layer And Mask Information Length: " + length_LayerAndMaskInformation);
        return sbuilder.toString();
    }
}
