package cn.imaginary.toolkit.image;

import cn.imaginary.toolkit.image.photoshopdocument.ColorModeData;
import cn.imaginary.toolkit.image.photoshopdocument.FileHeader;
import cn.imaginary.toolkit.image.photoshopdocument.ImageData;
import cn.imaginary.toolkit.image.photoshopdocument.ImageResources;
import cn.imaginary.toolkit.image.photoshopdocument.LayerAndMaskInfo;
import cn.imaginary.toolkit.image.photoshopdocument.layerandmask.LayerRecords;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class PsdUtils {

    private ArrayList<LayerRecords> alist_Layers;
    private ImageData idata;
    private FileHeader fheader;

    public PsdUtils() {}

    public FileHeader getFileHeader() {
        return fheader;
    }

    public ImageData getImageData() {
        return idata;
    }

    public ArrayList<LayerRecords> getLayers() {
        return alist_Layers;
    }

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

    public void read(RandomAccessFile rafile) {
        try {
            System.out.println("fileheader start: " + rafile.getFilePointer());
            fheader = new FileHeader();
            fheader.read(rafile);
            System.out.println(fheader.toString());
            System.out.println();

            System.out.println("colormodedata start: " + rafile.getFilePointer());
            ColorModeData cmdata = new ColorModeData();
            cmdata.read(rafile, fheader.getColorMode());
            System.out.println(cmdata.toString());
            System.out.println();

            System.out.println("imageresources start: " + rafile.getFilePointer());
            ImageResources iresources = new ImageResources();
            iresources.read(rafile);
            System.out.println(iresources.toString());
            System.out.println();

            System.out.println("layerandmaskinfo start: " + rafile.getFilePointer());
            LayerAndMaskInfo laminfo = new LayerAndMaskInfo();
            laminfo.read(rafile, fheader);
            System.out.println(laminfo.toString());
            System.out.println();
            alist_Layers = laminfo.getLayerRecordsList();

            System.out.println("imagedata start: " + rafile.getFilePointer());
            idata = new ImageData();
            idata.read(rafile, fheader);
            System.out.println(idata.toString());
            System.out.println();

            System.out.println("end: " + rafile.getFilePointer());
            System.out.println("File Length: " + rafile.length());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
