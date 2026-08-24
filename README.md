# PsdSDK
Psd SDK for Java

[Adobe Photoshop File Formats Specification](https://www.adobe.com/devnet-apps/photoshop/fileformatashtml/)

## 说明（About）：
### 相关信息：
#### 可获取的文件头信息（<u>FileHeader</u>）：
- version, channels, height, width, depth, color_mode.

#### 可获取的图层信息（<u>LayerInfo</u>）：
- <u>Base</u> :
    - left (x), right, top (y), bottom.
    - width, height.
    - channels.
    - opacity.
    - etc.
- <u>Mask/AdjustmentData</u> :
    - left, right, etc.
- <u>LayerType</u> :
    - file, folder open, folder closed, hidden.
- <u>LayerName</u> :
    - byte[] :
        - new String(nameBytes, Charset.forName(charsetName));

#### 可获取的全局图层蒙版信息（<u>GlobalLayerMaskInfo</u>）：
- <u>GlobalMask</u> :
    - red, green, blue, alpha, opacity, kind.

#### 解析后获取的图像像素数据结构（<u>Pixels</u>）：
- byte[channels][height][width]
    - <u>channels</u> (3 - 6) :
        - id = 0 : red
        - id = 1 : green
        - id = 2 : blue
        - id = 3 : alpha (-1)
        - id = 4 : mask (-2)
        - id = 5 : real mask (-3)

## 项目使用方法（Usage）：
### _PsdSDK_ :

将<u>PsdSDK.jar</u>加入项目依赖包。

#### _PsdTool_ （替换PsdUtils.java）:
```java
import cn.imaginary.toolkit.PsdTool;
    ...
    PsdTool psdUtils = new PsdTool();
    String psdFilePath = "xx.psd";
    File psdFile = new File(psdFilePath);
    psdTool.read(psdFile);
```

#### _~~PsdUtils~~_ （旧版使用）:
```java
import cn.imaginary.toolkit.image.PsdUtils;
    ...
    PsdUtils psdUtils = new PsdUtils();
    String psdFilePath = "xx.psd";
    File psdFile = new File(psdFilePath);
    psdUtils.read(psdFile);
```

---

#### _Layers_ （获取所有图层）:
```java
import cn.imaginary.toolkit.image.photoshopdocument.layerandmask.LayerRecords;
    ...
//    ArrayList<LayerRecords> arrayList = psdUtils.getLayers();//PsdUtils的方法
    ArrayList<LayerRecords> arrayList = psdUtils.getLayerRecordsList();//PsdTool的方法
    LayerRecords layerRecords = arrayList.get(0);
```

#### _pixels_ （选择psd文件图层，导出为png）:
```java
    byte[][][] arrays = layerRecords.getImageData();
    File dirFile = psdFile.getParentFile();
    String name = layerRecords.getName(layerRecords.getCharset());
    name += ".png";
    write(putils.getImage(arrays, lrecords.getWidth(), lrecords.getHeight()), new File(dirFile, name));
```

#### _ImageData_ （导出psd文件预览图为png）:
```java
    ImageData idata = putils.getImageData();
    arrays = idata.getImageData();
    name = psdFile.getName();
    name = name.substring(0, name.length() - 4) + ".png";
    write(putils.getImage(arrays, fheader.getWidth(), fheader.getHeight()), new File(dirFile, name));
```

---

- _Write_ （导出图像为文件）:

方法：
```java
public void write(BufferedImage image, File file) {
    ImageIO.write(image, "png", file);
}
```

- _~~BufferedImage~~_ （旧版使用，若PsdUtils.java没有getImage()方法，则手动获取图层像素数组并导出为png）:
```java
public void exportImage(File file, byte[][][] arrays, int width, int height) {
    byte[][][] arrays = layerRecords.getImageData();
    int channels = arrays.length;

    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    int rgb;
    int a = 0xff;
    ...
    for (int x = 0; x < width; x++) {
        for (int y = 0; y < height; y++) {
            if (channels > 3) {
                a = arrays[3][y][x] & 0xff;
            }
            r = arrays[0][y][x] & 0xff;
            ...
            rgb = (a << 24) | (r << 16) | (g << 8) | b;
            image.setRGB(x, y, rgb);
        }
        write(image, file);
    }
}
```

## 许可（License）：

------------------

License :
 [Apache License (Version 2.0)](http://www.apache.org/licenses/)

------------------

