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

public class Psd {
    private FileHeader fheader;
    private ColorModeData cmdata;
    private ImageResources iresources;
    private LayerAndMaskInfo laminfo;
    private ImageData idata;

    public Psd() {
    }

    public FileHeader getFileHeader() {
        return fheader;
    }

    public ColorModeData getColorModeData() {
        return cmdata;
    }

    public ImageResources getImageResources() {
        return iresources;
    }

    public LayerAndMaskInfo getLayerAndMaskInfo() {
        return laminfo;
    }

    public ImageData getImageData() {
        return idata;
    }

    public void setFileHeader(FileHeader fileHeader) {
        fheader = fileHeader;
    }

    public void setColorModeData(ColorModeData colorModeData) {
        cmdata = colorModeData;
    }

    public void setImageResources(ImageResources imageResources) {
        iresources = imageResources;
    }

    public void setLayerAndMaskInfo(LayerAndMaskInfo layerAndMaskInfo) {
        laminfo = layerAndMaskInfo;
    }

    public void setImageData(ImageData imageData) {
        idata = imageData;
    }

    public ArrayList<LayerRecords> getLayerRecordsList() {
        LayerAndMaskInfo laminfo = getLayerAndMaskInfo();
        if (null != laminfo) {
            return laminfo.getLayerRecordsList();
        }
        return null;
    }

    public void read(File file) {
        try {
            RandomAccessFile rafile = new RandomAccessFile(file, "r");
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
        FileHeader fheader = new FileHeader();
        fheader.read(rafile);
        setFileHeader(fheader);

        ColorModeData cmdata = new ColorModeData();
        cmdata.read(rafile, fheader.getColorMode());
        setColorModeData(cmdata);

        ImageResources iresources = new ImageResources();
        iresources.read(rafile);
        setImageResources(iresources);

        LayerAndMaskInfo laminfo = new LayerAndMaskInfo();
        laminfo.read(rafile, fheader);
        setLayerAndMaskInfo(laminfo);

        ImageData idata = new ImageData();
        idata.read(rafile, fheader);
        setImageData(idata);
    }
}
