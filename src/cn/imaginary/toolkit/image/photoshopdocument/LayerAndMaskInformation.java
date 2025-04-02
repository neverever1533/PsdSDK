package cn.imaginary.toolkit.image.photoshopdocument;

import cn.imaginary.toolkit.image.photoshopdocument.FileHeader;
import cn.imaginary.toolkit.image.photoshopdocument.layerandmask.AdditionalLayerInformation;
import cn.imaginary.toolkit.image.photoshopdocument.layerandmask.GlobalLayerMaskInfo;
import cn.imaginary.toolkit.image.photoshopdocument.layerandmask.LayerInfo;
import java.io.IOException;
import java.io.RandomAccessFile;

public class LayerAndMaskInformation {

    //4 Layer and Mask Information
    public LayerAndMaskInformation() {}

    private long length_LayerAndMaskInformation;

    public long getLength() {
        return 4 + length_LayerAndMaskInformation;
    }

    public void read(RandomAccessFile rafile, FileHeader fheader) {
        try {
            long location = rafile.getFilePointer();

            //4.1 Layer and Mask Information Length 4
            // Length of the layer and mask information section. (**PSB** length is 8 bytes.)
            if (!fheader.isFilePsb()) {
                length_LayerAndMaskInformation = rafile.readInt();
            } else {
                length_LayerAndMaskInformation = rafile.readLong();
            }

            //4.2 Layer Info:?
            // Layer info (see See Layer info for details).
            LayerInfo linfo = new LayerInfo();
            linfo.read(rafile, fheader);
            System.out.println(linfo.toString());
            System.out.println();

            //4.3 Global Layer Mask Info:?
            // Global layer mask info (see See Global layer mask info for details).
            GlobalLayerMaskInfo glminfo = new GlobalLayerMaskInfo();
            glminfo.read(rafile);
            System.out.println(glminfo.toString());
            System.out.println();

            //4.4 Additional Layer Information:?
            // (Photoshop 4.0 and later)
            // Series of tagged blocks containing various types of data. See See Additional Layer Information for the list of the types of data that can be included here.
            AdditionalLayerInformation alinfo = new AdditionalLayerInformation();
            alinfo.read(rafile);
            System.out.println(alinfo.toString());
            System.out.println();

            rafile.seek(location + getLength());
        } catch (IOException e) {}
    }

    public String toString() {
        StringBuilder sbuilder = new StringBuilder();
        sbuilder.append("Layer And Mask Information Length: " + getLength());
        return sbuilder.toString();
    }
}
