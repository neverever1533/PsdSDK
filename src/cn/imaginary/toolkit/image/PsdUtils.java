package cn.imaginary.toolkit.image;

import cn.imaginary.toolkit.image.photoshopdocument.ColorModeData;
import cn.imaginary.toolkit.image.photoshopdocument.FileHeader;
import cn.imaginary.toolkit.image.photoshopdocument.ImageData;
import cn.imaginary.toolkit.image.photoshopdocument.ImageResources;
import cn.imaginary.toolkit.image.photoshopdocument.LayerAndMaskInfo;
import cn.imaginary.toolkit.image.photoshopdocument.layerandmask.LayerInfo;
import cn.imaginary.toolkit.image.photoshopdocument.layerandmask.layer.LayerRecords;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class PsdUtils {

    private ArrayList<LayerRecords> alist_Layers;
    private byte[] data_imageData;

    public PsdUtils() {}

    public void read(File file) {
        RandomAccessFile rafile = null;
        try {
            rafile = new RandomAccessFile(file, "r");
            read(rafile);
            rafile.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void read(String filePath) {
        File file = new File(filePath);
        read(file);
    }

    private FileHeader readFileHeader(RandomAccessFile rafile) {
        FileHeader fheader = new FileHeader();
        fheader.read(rafile);
        //        System.out.println(fheader.toString());
        //        System.out.println();
        return fheader;
    }

    private void readColorModeData(RandomAccessFile rafile, FileHeader fheader) {
        ColorModeData cmdata = new ColorModeData();
        cmdata.read(rafile, fheader.getColorMode());
        //        System.out.println(cmdata.toString());
        //        System.out.println();
    }

    private void readImageResources(RandomAccessFile rafile) {
        ImageResources iresources = new ImageResources();
        iresources.read(rafile);
        //        System.out.println(iresources.toString());
        //        System.out.println();
    }

    private void readLayerAndMaskInformation(RandomAccessFile rafile, FileHeader fileHeader) {
        LayerAndMaskInfo laminfo = new LayerAndMaskInfo();
        laminfo.read(rafile, fileHeader);
        LayerInfo linfo = laminfo.getLayerInfo();
        System.out.println(laminfo.toString());
        System.out.println();
        alist_Layers = linfo.getLayerRecordsArrayList();
    }

    private void readImageData(RandomAccessFile rafile, FileHeader fileHeader) {
        ImageData idata = new ImageData();
        idata.read(rafile, fileHeader);
        data_imageData = idata.getImageData();
        System.out.println(idata.toString());
        System.out.println();
    }

    public void read(RandomAccessFile rafile) {
        try {
            System.out.println("start: " + rafile.getFilePointer());

            FileHeader fheader = readFileHeader(rafile);

            System.out.println("start: " + rafile.getFilePointer());

            readColorModeData(rafile, fheader);

            System.out.println("start: " + rafile.getFilePointer());

            readImageResources(rafile);

            System.out.println("start: " + rafile.getFilePointer());

            readLayerAndMaskInformation(rafile, fheader);

            /*System.out.println("start: " + rafile.getFilePointer());

            readImageData(rafile, fheader);
            */

            System.out.println("end: " + rafile.getFilePointer());
            System.out.println("File Length: " + rafile.length());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] getImageData() {
        return data_imageData;
    }

    public ArrayList<LayerRecords> getLayers() {
        return alist_Layers;
    }
}
