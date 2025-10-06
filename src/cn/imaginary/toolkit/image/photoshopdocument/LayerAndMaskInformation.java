package cn.imaginary.toolkit.image.photoshopdocument;

import cn.imaginary.toolkit.image.photoshopdocument.layerandmask.GlobalLayerMaskInfo;
import cn.imaginary.toolkit.image.photoshopdocument.layerandmask.LayerRecords;
import cn.imaginary.toolkit.image.photoshopdocument.layerandmask.layer.ChannelImageData;
import cn.imaginary.toolkit.image.photoshopdocument.layerandmask.layer.ChannelInfo;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Iterator;

public class LayerAndMaskInfo {

    private GlobalLayerMaskInfo glminfo;

    private long length_;
    private long length_Data;
    private long length_LayerInfo;
    private long length_Data_LayerInfo;


    private ArrayList<LayerRecords> arrayList_LayerRecords;

    private int length_MaskInfo;
    private int count_Layer;

    private byte[] arr_Data;
    private byte[] arr_Data_LayerInfo;

    //4 Layer and Mask Information
    public LayerAndMaskInfo() {
    }

    public long getLength() {
        return length_;
    }

    public long getDataLength() {
        return length_Data;
    }

    public long getLayerInfoLength() {
        return length_LayerInfo;
    }

    public long getLayerInfoDataLength() {
        return length_Data_LayerInfo;
    }

    public byte[] getData() {
        return arr_Data;
    }

    public byte[] getLayerInfoData() {
        return arr_Data_LayerInfo;
    }

    public void setData(byte[] array) {
        arr_Data = array;
    }

    public void setLayerInfoData(byte[] array) {
        arr_Data_LayerInfo = array;
    }

    public void setLength(long length) {
        length_ = length;
    }

    public void setDataLength(long length) {
        length_Data = length;
    }

    public void setLayerInfoLength(long length) {
        length_LayerInfo = length;
    }

    public void setLayerInfoDataLength(long length) {
        length_Data_LayerInfo = length;
    }

    public void setMaskInfoLength(int length) {
        length_MaskInfo = length;
    }

    public int getMaskInfoLength() {
        return length_MaskInfo;
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
            readDataLength(rafile, fheader);
            readData(rafile, fheader, getDataLength());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void readDataLength(RandomAccessFile rafile, FileHeader fheader) throws IOException {
        //4.1 Layer and Mask Information Length:4
        //4,Length of the layer and mask information section. (**PSB** length is 8 bytes.)
        long length;
        if (fheader.isFilePsb()) {
            length = rafile.readLong();
            length_ += 8;
        } else {
            length = rafile.readInt();
            length_ += 4;
        }
        setDataLength(length);
        setLength(length_ + getDataLength());
    }

    private void readData(RandomAccessFile rafile, FileHeader fheader, long length) throws IOException {
        if (length > 0) {
            byte[] arr = new byte[(int) length];
            rafile.read(arr);
            setData(arr);
            readDataArray(arr, fheader);
        }
    }

    private void readDataArray(byte[] array, FileHeader fheader) throws IOException {
        try {
            DataInputStream dinstream = new DataInputStream(new ByteArrayInputStream(array));
            //Layer Info:?
            //Layer info (see See Layer info for details).
            readLayerInfo(dinstream, fheader);

            //Global layer mask info
            //Variable,Global layer mask info (see See Global layer mask info for details).
            readMaskInfo(dinstream);
            dinstream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void readLayerInfo(DataInputStream dinstream, FileHeader fheader) throws IOException {
        //Layer Info Length:4
        //4,Length of the layers info section, rounded up to a multiple of 2. (**PSB** length is 8 bytes.)
        long length;
        long length_ = 0;
        if (fheader.isFilePsb()) {
            length = dinstream.readLong();
            length_ += 8;
        } else {
            length = dinstream.readInt();
            length_ += 4;
        }
        if (length % 2 != 0) {
            length++;
        }
        setLayerInfoDataLength(length);
        setLayerInfoLength(length_ + getLayerInfoDataLength());

        long length_layerinfo = getLayerInfoDataLength();
        byte[] arr = new byte[(int) length_layerinfo];
        dinstream.read(arr);
        setLayerInfoData(arr);
        readLayerInfoDataArray(arr, fheader);
    }

    private void readLayerInfoDataArray(byte[] array, FileHeader fheader) {
        try {
            DataInputStream dinstream = new DataInputStream(new ByteArrayInputStream(array));
            //Layer Count:2
            //2,Layer count. If it is a negative number, its absolute value is the number of layers and the first alpha channel contains the transparency data for the merged result.
            int count = dinstream.readShort();
            setLayerCount(count);

            //information:variable
            //Variable,Information about each layer. See Layer records describes the structure of this information for each layer.
            //Layer Records
            arrayList_LayerRecords = new ArrayList<LayerRecords>();
            for (int i = 0; i < count_Layer; i++) {
                LayerRecords lrecords = new LayerRecords();
                lrecords.read(dinstream, fheader);
                arrayList_LayerRecords.add(i, lrecords);
                System.out.println("Layer count: " + i);
                System.out.println(lrecords.toString());
                System.out.println();
            }

            //Channel image data:variable
            //Variable,Channel image data. Contains one or more image data records (see See Channel image data for structure) for each layer. The layers are in the same order as in the layer information (previous row of this table).
            for (Iterator<LayerRecords> it = arrayList_LayerRecords.iterator(); it.hasNext(); ) {
                LayerRecords lrecords = it.next();
                byte[][][] arrays_data_image =
                        new byte[lrecords.getChannels()][lrecords.getHeight()][lrecords.getWidth()];
                ArrayList<ChannelInfo> arrayList_ChannelInfo = lrecords.getChannelInfoList();
                for (Iterator<ChannelInfo> itr = arrayList_ChannelInfo.iterator(); itr.hasNext(); ) {
                    ChannelInfo cinfo = itr.next();
                    int id = cinfo.getID();
                    ChannelImageData cidata = new ChannelImageData();
                    cidata.read(dinstream, cinfo.getDataLength(), fheader, lrecords.getWidth(), lrecords.getHeight());
                    if (id < 0) {
                        arrays_data_image[2 - id] = cidata.getImageData();
                    } else {
                        arrays_data_image[id] = cidata.getImageData();
                    }
                    System.out.print("channel id: " + cinfo.getID());
                    System.out.println("/" + cidata.toString());
                }
                System.out.println();
                lrecords.setImageData(arrays_data_image);
            }
            dinstream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void readMaskInfo(DataInputStream dinstream) {
        //4.3 Global Layer Mask Info:Variable
        // Variable,Global layer mask info (see See Global layer mask info for details).
        glminfo = new GlobalLayerMaskInfo();
        glminfo.read(dinstream);
        System.out.println(glminfo.toString());
        System.out.println();
        setMaskInfoLength(glminfo.getLength());
    }

    public String toString() {
        StringBuilder sbuilder = new StringBuilder();
        sbuilder.append("Layer And Mask Information Length: " + getLength());
        return sbuilder.toString();
    }
}
