package cn.imaginary.toolkit.image.photoshopdocument;

import cn.imaginary.toolkit.image.photoshopdocument.layerandmask.AdditionalLayerInfo;
import cn.imaginary.toolkit.image.photoshopdocument.layerandmask.GlobalLayerMaskInfo;
import cn.imaginary.toolkit.image.photoshopdocument.layerandmask.LayerRecords;
import cn.imaginary.toolkit.image.photoshopdocument.layerandmask.layer.ChannelImageData;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.SortedMap;

public class LayerAndMaskInfo {

    private GlobalLayerMaskInfo glminfo;

    private long length_;
    private long length_Data;

    private ArrayList<LayerRecords> arrayList_LayerRecords;

    private int count_Layer;

    //4 Layer and Mask Information
    public LayerAndMaskInfo() {}

    public long getLength() {
        return length_;
    }

    public int getLayerCount() {
        return count_Layer;
    }

    public void setLayerCount(int count) {
        count_Layer = count;
    }

    public GlobalLayerMaskInfo getGlobalLayerMaskInfo() {
        return glminfo;
    }

    public ArrayList<LayerRecords> getLayerRecordsList() {
        return arrayList_LayerRecords;
    }

    public void read(RandomAccessFile rafile, FileHeader fheader) {
        try {
            long location = rafile.getFilePointer();

            readDataLength(rafile, fheader);
            readData(rafile, fheader);

            length_ += length_Data;
            System.out.println("layer and mask space: " + (location + length_ - rafile.getFilePointer()));
            rafile.seek(location + getLength());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void readData(RandomAccessFile rafile, FileHeader fheader) throws IOException {
        readDataArray(rafile, fheader);
    }

    private void readDataArray(RandomAccessFile rafile, FileHeader fheader) throws IOException {
        long space = length_Data;
        long length_layer = readLayerInfo(rafile, fheader);
        space -= length_layer;
        System.out.println("layer and mask layerinfo read space: " + space);

        long length_mask = readGlobalLayerMaskInfo(rafile, fheader);
        space -= length_mask;
        System.out.println("layer and mask maskinfo read space: " + space);
        //        readAdditionalLayerInfo(rafile, fheader);
    }

    private long readGlobalLayerMaskInfo(RandomAccessFile rafile, FileHeader fheader) {
        //4.3 Global Layer Mask Info:?
        // Global layer mask info (see See Global layer mask info for details).
        glminfo = new GlobalLayerMaskInfo();
        glminfo.read(rafile);
        System.out.println(glminfo.toString());
        System.out.println();
        return glminfo.getLength();
    }

    private long readChanelImageData(RandomAccessFile rafile, FileHeader fheader) throws IOException {
        long location = rafile.getFilePointer();
        for (Iterator<LayerRecords> it = arrayList_LayerRecords.iterator(); it.hasNext();) {
            LayerRecords lrecords = it.next();
            ArrayList<ChannelImageData> arrayList_ChannelImageData = lrecords.getChannelImageDataList();
            for (Iterator<ChannelImageData> itr = arrayList_ChannelImageData.iterator(); itr.hasNext();) {
                ChannelImageData cidata = itr.next();
                cidata.read(rafile, fheader, lrecords);
                System.out.println(cidata.toString());
            }
        }
        return rafile.getFilePointer() - location;
    }

    private long readLayerRecords(RandomAccessFile rafile, FileHeader fheader) throws IOException {
        //4.2.2 Layer Count 2
        int count_Layer = rafile.readShort();

        long location = rafile.getFilePointer();
        //4.2.3 Layer Records ?
        arrayList_LayerRecords = new ArrayList<LayerRecords>();
        for (int i = 0; i < count_Layer; i++) {
            LayerRecords lrecords = new LayerRecords();
            lrecords.read(rafile, fheader);

            arrayList_LayerRecords.add(i, lrecords);

            System.out.println("Layer count: " + i);
            System.out.println(lrecords.toString());
            System.out.println();
        }
        return rafile.getFilePointer() - location;
    }

    private void readLayerInfoDataArray(RandomAccessFile rafile, FileHeader fheader, long length) throws IOException {
        long space = length;

        long length_lrecords = readLayerRecords(rafile, fheader);
        space -= 2 + length_lrecords;

        long length_cidata = readChanelImageData(rafile, fheader);
        space -= length_cidata;
        System.out.println("layer info channelimagedata read space:" + space);
    }

    private void readLayerInfoData(RandomAccessFile rafile, FileHeader fheader, long length) throws IOException {
        //        if (length > 0) {
        //            byte[] arr = new byte[(int) length];
        //            rafile.read(arr);
        //            readDataArray(arr, fheader);
        //        }
        readLayerInfoDataArray(rafile, fheader, length);
    }

    private long readLayerInfo(RandomAccessFile rafile, FileHeader fheader) throws IOException {
        long location = rafile.getFilePointer();
        //4.2 Layer Info:?
        // Layer info (see See Layer info for details).

        //4.2.1 Layer Info Length
        // Length of the layers info section, rounded up to a multiple of 2. (**PSB** length is 8 bytes.)
        long length_ = 0;
        long length_Data;
        if (fheader.isFilePsb()) {
            length_Data = rafile.readLong();
            length_ += 8;
        } else {
            length_Data = rafile.readInt();
            length_ += 4;
        }
        if (length_Data % 2 != 0) {
            length_Data++;
        }
        readLayerInfoData(rafile, fheader, length_Data);
        length_ += length_Data;
        rafile.seek(location + length_);
        return length_;
    }

    private void readDataLength(RandomAccessFile rafile, FileHeader fheader) throws IOException {
        //4.1 Layer and Mask Information Length 4
        // Length of the layer and mask information section. (**PSB** length is 8 bytes.)
        if (fheader.isFilePsb()) {
            length_Data = rafile.readLong();
            length_ += 8;
        } else {
            length_Data = rafile.readInt();
            length_ += 4;
        }
    }

    public String toString() {
        StringBuilder sbuilder = new StringBuilder();
        sbuilder.append("Layer And Mask Information Length: " + getLength());
        return sbuilder.toString();
    }
}
