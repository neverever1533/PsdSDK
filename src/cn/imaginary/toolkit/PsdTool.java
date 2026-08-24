package cn.imaginary.toolkit;

import cn.imaginary.toolkit.image.Psd;
import cn.imaginary.toolkit.image.RGBArray;
import cn.imaginary.toolkit.image.photoshopdocument.ColorModeData;
import cn.imaginary.toolkit.image.photoshopdocument.FileHeader;
import cn.imaginary.toolkit.image.photoshopdocument.ImageData;
import cn.imaginary.toolkit.image.photoshopdocument.ImageResources;
import cn.imaginary.toolkit.image.photoshopdocument.LayerAndMaskInfo;
import cn.imaginary.toolkit.image.photoshopdocument.layerandmask.LayerRecords;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class PsdTool {
    private Psd psd = new Psd();

    public PsdTool() {
    }

    public FileHeader getFileHeader() {
        return psd.getFileHeader();
    }

    public BufferedImage getImage(byte[][][] arrays, int width, int height) {
        RGBArray rgbArray = new RGBArray();
        return rgbArray.getImage(arrays, width, height);
    }

    public ImageData getImageData() {
        return psd.getImageData();
    }

    public ArrayList<LayerRecords> getLayerRecordsList() {
        return psd.getLayerRecordsList();
    }

    public void read(File file) {
        psd.read(file);
    }

    public void read(String filePath) {
        File file = new File(filePath);
        read(file);
    }

    public void read(RandomAccessFile rafile) {
        try {
            System.out.println("fileheader start: " + rafile.getFilePointer());
            psd.read(rafile);
            FileHeader fheader = psd.getFileHeader();
            System.out.println(fheader.toString());
            System.out.println();

            System.out.println("colormodedata start: " + rafile.getFilePointer());
            ColorModeData cmdata = psd.getColorModeData();
            System.out.println(cmdata.toString());
            System.out.println();

            System.out.println("imageresources start: " + rafile.getFilePointer());
            ImageResources iresources = psd.getImageResources();
            System.out.println(iresources.toString());
            System.out.println();

            System.out.println("layerandmaskinfo start: " + rafile.getFilePointer());
            LayerAndMaskInfo laminfo = psd.getLayerAndMaskInfo();
            System.out.println(laminfo.toString());
            System.out.println();

            System.out.println("imagedata start: " + rafile.getFilePointer());
            ImageData idata = psd.getImageData();
            System.out.println(idata.toString());
            System.out.println();

            System.out.println("end: " + rafile.getFilePointer());
            System.out.println("File Length: " + rafile.length());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
