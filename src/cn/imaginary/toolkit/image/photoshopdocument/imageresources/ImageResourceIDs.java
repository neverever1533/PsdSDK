package cn.imaginary.toolkit.image.photoshopdocument.imageresources;

import cn.imaginary.toolkit.image.photoshopdocument.imageresources.resources.ColorSamplersResourceFormat;
import cn.imaginary.toolkit.image.photoshopdocument.imageresources.resources.GridAndGuideResourceFormat;
import cn.imaginary.toolkit.image.photoshopdocument.imageresources.resources.PathResourceFormat;
import cn.imaginary.toolkit.image.photoshopdocument.imageresources.resources.SlicesResourceFormat;
import cn.imaginary.toolkit.image.photoshopdocument.imageresources.resources.ThumbnailResourceFormat;
import java.io.IOException;
import java.io.RandomAccessFile;

public class ImageResourceIDs {

    public ImageResourceIDs() {}

    private int id;

    public void read(RandomAccessFile rafile, int length, int id) {
        try {
            if (length > 0) {
                rafile.skipBytes(length);
            } else {
                return;
            }
            this.id = id;
            SlicesResourceFormat srformat;
            GridAndGuideResourceFormat gagrformat;
            PathResourceFormat prformat;
            ThumbnailResourceFormat trformat;
            ColorSamplersResourceFormat csrformat;
            if (id >= 2000 && id <= 2997) {
                // Path Information (saved paths). See See Path resource format.
                // Path resource format.
                prformat = new PathResourceFormat();
                prformat.read(rafile);
            } else if (id > 4000 && id < 5000) {} else {
                switch (id) {
                    case 1025:
                        // Working path (not saved). See See Path resource format.
                        // Working path (not saved).
                        // Path resource format.
                        prformat = new PathResourceFormat();
                        prformat.read(rafile);
                        break;
                    case 1032:
                        // (Photoshop 4.0) Grid and guides information. See See Grid and guides resource format.
                        // Grid and guides resource format.
                        gagrformat = new GridAndGuideResourceFormat();
                        gagrformat.read(rafile);
                        break;
                    case 1033:
                        // (Photoshop 4.0) Thumbnail resource for Photoshop 4.0 only. See See Thumbnail resource format.
                        // Thumbnail resource format.
                        trformat = new ThumbnailResourceFormat();
                        trformat.read(rafile, id);
                        break;
                    case 1036:
                        // (Photoshop 5.0) Thumbnail resource (supersedes resource 1033). See See Thumbnail resource format.
                        // Thumbnail resource format.
                        trformat = new ThumbnailResourceFormat();
                        trformat.read(rafile, id);
                        break;
                    case 1038:
                        // (Obsolete) See ID 1073 below. (Photoshop 5.0) Color samplers resource. See See Color samplers resource format
                        // Color samplers resource format
                        csrformat = new ColorSamplersResourceFormat();
                        csrformat.read(rafile, id);
                        break;
                    case 1050:
                        // (Photoshop 6.0) Slices. See See Slices resource format.
                        // Slices resource format.
                        srformat = new SlicesResourceFormat();
                        srformat.read(rafile);
                        break;
                    case 1073:
                        // (Photoshop CS3) Color samplers resource. Also see ID 1038 for old format. See See Color samplers resource format.
                        // Color samplers resource format.
                        csrformat = new ColorSamplersResourceFormat();
                        csrformat.read(rafile, id);
                        break;
                    case 2999:
                        // Name of clipping path. See See Path resource format.
                        // Path resource format.
                        prformat = new PathResourceFormat();
                        prformat.read(rafile);
                        break;
                    // case ...:
                    // break;
                    default:
                        break;
                }
            }
        } catch (IOException e) {}
    }
}