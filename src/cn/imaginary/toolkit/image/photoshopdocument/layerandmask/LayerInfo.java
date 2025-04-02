package cn.imaginary.toolkit.image.photoshopdocument.layerandmask;

import cn.imaginary.toolkit.image.photoshopdocument.FileHeader;
// import cn.imaginary.toolkit.image.photoshopdocument.layerandmask.layer.ChannelImageData;
import cn.imaginary.toolkit.image.photoshopdocument.layerandmask.layer.LayerRecords;
import java.io.IOException;
import java.io.RandomAccessFile;

public class LayerInfo {

    //4.2 Layer Info ?
    public LayerInfo() {}

    private int length_LayerInfo;
    private int layerCount;

    public int getLength() {
        return 4 + length_LayerInfo;
    }

    public int getLayerCount() {
        return layerCount;
    }

    public void read(RandomAccessFile rafile, FileHeader fheader) {
        try {
            long location = rafile.getFilePointer();

            //4.2.1 Layer Info Length 4
            length_LayerInfo = rafile.readInt();

            //4.2.2 Layer Count 2
            layerCount = rafile.readShort();
            //layerCount = rafile.readShort() & 0xFF;

            //4.2.3 Layer Records ?
            for (int i = 0; i < layerCount; i++) {
                System.out.println("Layer: " + i);

                LayerRecords lrecords = new LayerRecords();
                lrecords.read(rafile, fheader);
                System.out.println(lrecords.toString());
                System.out.println();
                /*ChannelImageData cidata = new ChannelImageData();
                cidata.read(
                    rafile,
                    lrecords.getLength(),
                    lrecords.getTop(),
                    lrecords.getLeft(),
                    lrecords.getBottom(),
                    lrecords.getRight()
                );*/
            }

            rafile.seek(location + getLength());
        } catch (IOException e) {}
    }

    public String toString() {
        StringBuilder sbuilder = new StringBuilder();
        sbuilder.append("Layer Info Length: " + getLength());
        sbuilder.append("/Layer Count: " + layerCount);
        return sbuilder.toString();
    }
}
