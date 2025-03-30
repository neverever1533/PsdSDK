package cn.imaginary.toolkit.image;

import cn.imaginary.toolkit.image.photoshopdocument.ColorModeData;
import cn.imaginary.toolkit.image.photoshopdocument.FileHeader;
import cn.imaginary.toolkit.image.photoshopdocument.ImageData;
import cn.imaginary.toolkit.image.photoshopdocument.ImageResources;
import cn.imaginary.toolkit.image.photoshopdocument.LayerAndMaskInformation;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class PsdUtils {

    public PsdUtils() {}

    public void read(File file) {
        RandomAccessFile rafile = null;
        try {
            rafile = new RandomAccessFile(file, "r");
            read(rafile);
        } catch (FileNotFoundException e) {} finally {
            try {
                rafile.close();
            } catch (IOException e) {}
        }
    }

    public void read(String filePath) {
        File file = new File(filePath);
        read(file);
    }

    public void read(RandomAccessFile rafile) {
        try {
            System.out.println("start: " + rafile.getFilePointer());

            FileHeader fheader = new FileHeader();
            fheader.read(rafile);
            System.out.println(fheader.toString());

            System.out.println("start: " + rafile.getFilePointer());

            ColorModeData cmdata = new ColorModeData();
            cmdata.read(rafile, fheader.getColorMode());
            System.out.println(cmdata.toString());

            System.out.println("start: " + rafile.getFilePointer());

            ImageResources iresources = new ImageResources();
            iresources.read(rafile);
            System.out.println(iresources.toString());

            System.out.println("start: " + rafile.getFilePointer());

            LayerAndMaskInformation laminfo = new LayerAndMaskInformation();
            laminfo.read(rafile, fheader);

            System.out.println("start: " + rafile.getFilePointer());

            ImageData idata = new ImageData();
            idata.read(rafile, fheader);
            System.out.println("end: " + rafile.getFilePointer());
        } catch (IOException e) {}
    }
}
