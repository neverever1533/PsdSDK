package cn.imaginary.toolkit.image.photoshopdocument.layerandmask;

import cn.imaginary.toolkit.image.photoshopdocument.FileHeader;
// import cn.imaginary.toolkit.image.photoshopdocument.layerandmask.layer.ChannelImageData;
import cn.imaginary.toolkit.image.photoshopdocument.layerandmask.layer.ChannelInfo;
import cn.imaginary.toolkit.image.photoshopdocument.layerandmask.layer.LayerRecords;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Iterator;

public class LayerInfo {

    private long length_Data;
    private int count_Layer;
    private ArrayList<LayerRecords> arrayList_LayerRecords;
    private long length_;

    //4.2 Layer Info ?
    public LayerInfo() {}

    public long getLength() {
        return length_;
    }

    public int getLayerCount() {
        return count_Layer;
    }

    public ArrayList<LayerRecords> getLayerRecordsArrayList() {
        return arrayList_LayerRecords;
    }

    private void readDataArray(RandomAccessFile rafile, FileHeader fheader) throws IOException {
        readLayerCount(rafile);
        readLayerRecords(rafile, fheader);
    }

    private void readData(RandomAccessFile rafile, FileHeader fheader) throws IOException {
        //        if (length_Data > 0) {
        //            byte[] arr = new byte[(int) length_Data];
        //            rafile.read(arr);
        //            readDataArray(arr, fheader);
        //        }
        readDataArray(rafile, fheader);
    }

    public void read(RandomAccessFile rafile, FileHeader fheader) {
        try {
            long location = rafile.getFilePointer();

            readDataLength(rafile, fheader);
            readData(rafile, fheader);

            length_ += length_Data;
            rafile.seek(location + getLength());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void readLayerRecords(RandomAccessFile rafile, FileHeader fheader) throws IOException {
        //4.2.3 Layer Records ?
        arrayList_LayerRecords = new ArrayList<LayerRecords>();
        for (int i = 0; i < count_Layer; i++) {
            LayerRecords lrecords = new LayerRecords();
            lrecords.read(rafile, fheader);
            arrayList_LayerRecords.add(i, lrecords);

            System.out.println("Layer: " + i);
            System.out.println(lrecords.toString());
            System.out.println();
        }

        for (Iterator<LayerRecords> it = arrayList_LayerRecords.iterator(); it.hasNext();) {
            LayerRecords lrecords = (LayerRecords) it.next();
            ArrayList<ChannelInfo> arrayList_ChannelInfo = lrecords.getChannelInfoArrayList();
            for (Iterator<ChannelInfo> itr = arrayList_ChannelInfo.iterator(); itr.hasNext();) {
                ChannelInfo cinfo = (ChannelInfo) itr.next();
                byte[] arr = new byte[(int) (cinfo.getDataLength())];
                rafile.read(arr);
                cinfo.setData(arr);
                /*ChannelImageData cidata = new ChannelImageData();
                    cidata.read(rafile, fheader, lrecords, cinfo);
                    cinfo.setData(cidata.getData());*/
            }
        }
    }

    private void readLayerCount(RandomAccessFile rafile) throws IOException {
        //4.2.2 Layer Count 2
        count_Layer = rafile.readShort();
    }

    private void readDataLength(RandomAccessFile rafile, FileHeader fheader) throws IOException {
        //4.2.1 Layer Info Length
        // Length of the layers info section, rounded up to a multiple of 2. (**PSB** length is 8 bytes.)
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
    }

    public String toString() {
        String sbuilder = "Layer Info Length: " + getLength() + "/Layer Count: " + count_Layer;
        return sbuilder;
    }
}
