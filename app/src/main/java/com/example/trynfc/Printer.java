package com.example.trynfc;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.os.IBinder;
import android.util.Log;

import androidx.core.view.ViewCompat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

public class Printer {
    public static final int BMP_PRINT_FAST = 2;
    public static final int BMP_PRINT_SLOW = 3;
    public static final int DATA_FILE_ERROR = -2;
    @Deprecated
    public static final int PRINTER_EXIST_PAPER = 1;
    @Deprecated
    public static final int PRINTER_NO_PAPER = 0;
    @Deprecated
    public static final int PRINTER_PAPER_ERROR = 2;
    public static final int PRINTER_STATUS_NO_PAPER = 0;
    public static final int PRINTER_STATUS_OK = 1;
    public static final int PRINTER_STATUS_OVER_HEAT = -1;
    public static final int PRINTER_STATUS_OVER_HEAT_NOPAPER = -2;
    public static final int PRINT_OK = 3;
    public static final int TEXT_TOOL_ONG_ERROR = -1;
    private static int nTargetIndex = 0;
    static Printer printer = null;
    private final String DATAFILE_EXT = ".bin";
    private final String DATAFILE_NAME = "/data/media/printer";
    private final int DEVICECODE_INDEX = 512;
    private final String NOTIFY_FILE = "/proc/printer";
    private final String PRINTER_STATUS = "proc/printer_errfb";
    private final String READ_FILE = "proc/printer_status";
    private String m_strDataFile;

    public static Printer getInstance() {
        if (printer == null) {
            printer = new Printer();
        }
        return printer;
    }

    public void printText(String data) throws Exception {
        printText(data, 1, false);
    }

    public void printText(String data, int size) throws Exception {
        printText(data, size, false);
    }

    public void printText(String data, boolean r2lFlag) throws Exception {
        printText(data, 1, r2lFlag);
    }

    public int printText(String data, int size, boolean r2lFlag) throws Exception {
        Log.e("sunchao", "data.length.........." + data.length());
        if (data.length() > 870) {
            try {
                throw new Exception("text more than 2k characters,ERROR CODE: -1");
            } catch (Exception e) {
                e.printStackTrace();
            } catch (Throwable th) {
            }
        } else {
            if (size < 1) {
                size = 1;
            } else if (size > 4) {
                size = 4;
            }
            byte[] header = prepareTextHeader(size, r2lFlag);
            String data_file = checkAvailablePath();
            if (data_file == null) {
                return -2;
            }
            try {
                writeFile(data_file, header, data.getBytes("UNICODE"), 1);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            notifyToPrint(data_file);
            return 3;
        }
        return -1;
    }

    public void printFormattedTextPrepare() {
        printFormattedTextPrepare(false);
    }

    public void printFormattedTextPrepare(boolean r2lFlag) {
        this.m_strDataFile = checkAvailablePath();
        writeFile(this.m_strDataFile, prepareTextHeader(1, r2lFlag), null, 0);
    }

    public void addString(String str, int size, boolean isBold) {
        if (size > 4 || size < 1) {
            size = 1;
        }
        char[] boldFlag = {17691, 1};
        char[] unboldFlag = {17691, 256};
        StringBuffer buffer = new StringBuffer(str);
        if (size != 1) {
            char[] e = {22299, 1};
            e[1] = (char) size;
            buffer.insert(0, e);
        }
        if (isBold) {
            buffer.insert(0, boldFlag);
        } else {
            buffer.insert(0, unboldFlag);
        }
        try {
            writeFile(this.m_strDataFile, null, buffer.toString().getBytes("UNICODE"), 3);
        } catch (Exception var8) {
            var8.printStackTrace();
        }
    }

    public void printFormattedText() {
        notifyToPrint(this.m_strDataFile);
    }

    public void printBitmap(String filePath) {
        printBitmap(filePath, 2);
    }

    public void printBitmap(String filePath, int speed) {
        try {
            FileInputStream e = new FileInputStream(filePath);
            printBitmap((InputStream) e, speed);
            e.close();
        } catch (Exception var4) {
            var4.printStackTrace();
        }
    }

    public void printBitmap(InputStream is) {
        printBitmap(is, 2);
    }

    public void print32Bitmap(Bitmap bmp) {
        short var14;
        short var16;
        short var15;
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Log.d("zhangdi", "width = " + width + ",height = " + height);
        int[] pixels = new int[(width * height)];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        Log.d("zhangdi", "alpha = " + ViewCompat.MEASURED_STATE_MASK);
        for (int newBmp = 0; newBmp < height; newBmp++) {
            for (int j = 0; j < width; j++) {
                int grey = pixels[(width * newBmp) + j];
                int alpha = (-16777216 & grey) >> 24;
                int green = (65280 & grey) >> 8;
                int blue = grey & 255;
                if (((16711680 & grey) >> 16) > 127) {
                    var14 = 255;
                } else {
                    var14 = 0;
                }
                if (blue > 127) {
                    var16 = 255;
                } else {
                    var16 = 0;
                }
                if (green > 127) {
                    var15 = 255;
                } else {
                    var15 = 0;
                }
                pixels[(width * newBmp) + j] = (alpha << 24) | (var14 << 16) | (var15 << 8) | var16;
                if (pixels[(width * newBmp) + j] == -1) {
                    pixels[(width * newBmp) + j] = -1;
                } else {
                    pixels[(width * newBmp) + j] = -16777216;
                }
            }
        }
        Bitmap var13 = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        var13.setPixels(pixels, 0, width, 0, 0, width, height);
        printBitmap(var13);
    }

    public void printBitmap(InputStream is, int speed) {
        DataInputStream dis = new DataInputStream(is);
        byte[] bf = new byte[14];
        byte[] bi = new byte[40];
        try {
            dis.read(bf, 0, 14);
            dis.read(bi, 0, 40);
        } catch (Exception var27) {
            var27.printStackTrace();
        }
        int BMPDataOffset = ChangeInt(bf, 13);
        int width = ChangeInt(bi, 7);
        int height = ChangeInt(bi, 11);
        byte[] image_bytes = new byte[((width * height) / 8)];
        int nbitcount = ((bi[15] & 255) << 8) | (bi[14] & 255);
        int nsizeimage = ChangeInt(bi, 23);
        int[] image = new int[(width * height)];
        int nArray = 0;
        switch (nbitcount) {
            case 1:
                int dataArrayLen = (width * height) / 8;
                if (dataArrayLen <= nsizeimage) {
                    try {
                        dis.read(new byte[(BMPDataOffset - 54)], 0, BMPDataOffset - 54);
                        dis.read(image_bytes, 0, dataArrayLen);
                        return;
                    } catch (Exception var26) {
                        var26.printStackTrace();
                        return;
                    }
                } else {
                    return;
                }
            case 8:
                int dataArrayLen2 = width * height;
                if (dataArrayLen2 <= nsizeimage) {
                    int header = (BMPDataOffset - 54) / 4;
                    if (header >= 0) {
                        int[] data_file = new int[header];
                        byte[] imageData = new byte[dataArrayLen2];
                        for (int i = 0; i < header; i++) {
                            try {
                                data_file[i] = (dis.readByte() & 255) | ((dis.readByte() & 255) << 8) | ((dis.readByte() & 255) << 16) | ((dis.readByte() & 255) << 24);
                            } catch (IOException var25) {
                                var25.printStackTrace();
                            }
                        }
                        try {
                            dis.read(imageData, 0, dataArrayLen2);
                        } catch (Exception var24) {
                            var24.printStackTrace();
                        }
                        int i2 = height - 1;
                        while (i2 >= 0) {
                            int j = 0;
                            while (true) {
                                int nArray2 = nArray;
                                if (j >= width) {
                                    i2--;
                                    nArray = nArray2;
                                } else {
                                    nArray = nArray2 + 1;
                                    int index = unsignedByteToInt(imageData[nArray2]);
                                    if ((((data_file[index] & 255) + ((data_file[index] >> 8) & 255)) + ((data_file[index] >> 16) & 255)) / 3 < 127) {
                                        image[(i2 * width) + j] = 1;
                                    } else {
                                        image[(i2 * width) + j] = 0;
                                    }
                                    j++;
                                }
                            }
                        }
                        for (int n = 0; n < (width * height) / 8; n++) {
                            image_bytes[n] = (byte) ((((byte) (image[(n * 8) + 0] & 1)) << 7) | (((byte) (image[(n * 8) + 1] & 1)) << 6) | (((byte) (image[(n * 8) + 2] & 1)) << 5) | (((byte) (image[(n * 8) + 3] & 1)) << 4) | (((byte) (image[(n * 8) + 4] & 1)) << 3) | (((byte) (image[(n * 8) + 5] & 1)) << 2) | (((byte) (image[(n * 8) + 6] & 1)) << 1) | (((byte) (image[(n * 8) + 7] & 1)) << 0));
                        }
                        break;
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            case 24:
                int dataArrayLen3 = width * height * 3;
                if (dataArrayLen3 <= nsizeimage) {
                    byte[] imageData2 = new byte[(dataArrayLen3 + 1)];
                    try {
                        dis.read(imageData2, 0, dataArrayLen3);
                    } catch (Exception var23) {
                        var23.printStackTrace();
                    }
                    int i3 = height - 1;
                    while (i3 >= 0) {
                        int j2 = 0;
                        while (true) {
                            int nArray22 = nArray;
                            if (j2 >= width) {
                                i3--;
                                nArray = nArray22;
                            } else {
                                int nArray23 = nArray22 + 1 + 1;
                                nArray = nArray23 + 1;
                                if (((unsignedByteToInt(imageData2[nArray23]) + unsignedByteToInt(imageData2[nArray])) + unsignedByteToInt(imageData2[nArray23])) / 3 < 127) {
                                    image[(i3 * width) + j2] = 1;
                                } else {
                                    image[(i3 * width) + j2] = 0;
                                }
                                j2++;
                            }
                        }
                    }
                    for (int n2 = 0; n2 < (width * height) / 8; n2++) {
                        image_bytes[n2] = (byte) ((((byte) (image[n2 * 8] & 1)) << 7) | (((byte) (image[(n2 * 8) + 1] & 1)) << 6) | (((byte) (image[(n2 * 8) + 2] & 1)) << 5) | (((byte) (image[(n2 * 8) + 3] & 1)) << 4) | (((byte) (image[(n2 * 8) + 4] & 1)) << 3) | (((byte) (image[(n2 * 8) + 5] & 1)) << 2) | (((byte) (image[(n2 * 8) + 6] & 1)) << 1) | (((byte) (image[(n2 * 8) + 7] & 1)) << 0));
                    }
                    break;
                } else {
                    return;
                }
            default:
                return;
        }
        byte[] var28 = prepareBitmapHeader(width, height, speed);
        String var29 = checkAvailablePath();
        if (var29 != null) {
            writeFile(var29, var28, image_bytes, 2);
            notifyToPrint(var29);
        }
    }

    public void printBitmap(Bitmap bitmap) {
        printBitmap(bitmap, 2);
    }

    public void printBitmap(Bitmap bitmap, int speed) {
        if (bitmap != null) {
            int nBmpWidth = bitmap.getWidth();
            int nBmpHeight = bitmap.getHeight();
            int bufferSize = nBmpHeight * ((nBmpWidth * 3) + (nBmpWidth % 4));
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            long bfSize = (long) (bufferSize + 54);
            long biWidth = (long) nBmpWidth;
            long biHeight = (long) nBmpHeight;
            long biSizeImage = (long) (nBmpHeight * nBmpWidth * 3);
            try {
                writeWord(bout, 19778);
                writeDword(bout, bfSize);
                writeWord(bout, 0);
                writeWord(bout, 0);
                writeDword(bout, 54);
                writeDword(bout, 40);
                writeLong(bout, biWidth);
                writeLong(bout, biHeight);
                writeWord(bout, 1);
                writeWord(bout, 24);
                writeDword(bout, 0);
                writeDword(bout, biSizeImage);
                writeLong(bout, 0);
                writeLong(bout, 0);
                writeDword(bout, 0);
                writeDword(bout, 0);
                byte[] e = new byte[bufferSize];
                int wWidth = (nBmpWidth * 3) + (nBmpWidth % 4);
                int nCol = 0;
                int nRealCol = nBmpHeight - 1;
                while (nCol < nBmpHeight) {
                    int byteArrayInputStream = 0;
                    int wByteIdex = 0;
                    while (byteArrayInputStream < nBmpWidth) {
                        int clr = bitmap.getPixel(byteArrayInputStream, nCol);
                        if (((Color.blue(clr) + Color.green(clr)) + Color.red(clr)) / 3 >= 155) {
                            e[(nRealCol * wWidth) + wByteIdex] = -1;
                            e[(nRealCol * wWidth) + wByteIdex + 1] = -1;
                            e[(nRealCol * wWidth) + wByteIdex + 2] = -1;
                        } else {
                            e[(nRealCol * wWidth) + wByteIdex] = 0;
                            e[(nRealCol * wWidth) + wByteIdex + 1] = 0;
                            e[(nRealCol * wWidth) + wByteIdex + 2] = 0;
                        }
                        byteArrayInputStream++;
                        wByteIdex += 3;
                    }
                    nCol++;
                    nRealCol--;
                }
                bout.write(e, 0, bufferSize);
                ByteArrayInputStream byteArrayInputStream2 = new ByteArrayInputStream(bout.toByteArray());
                printBitmap((InputStream) byteArrayInputStream2, speed);
                bout.close();
                byteArrayInputStream2.close();
            } catch (IOException var22) {
                var22.printStackTrace();
            }
        }
    }

    public void printEndLine() throws Exception {
        printText("\n\n\n");
    }

    @Deprecated
    public boolean voltageCheck() {
        return true;
    }

    @Deprecated
    public int getPaperStatus() {
        byte ret = 2;
        byte[] arr = new byte[4];
        try {
            RandomAccessFile e3 = new RandomAccessFile("/proc/printer", "r");
            try {
                int i1 = e3.read(arr);
                e3.close();
                if (i1 == 2) {
                    if (arr[1] == 48) {
                        ret = 0;
                    } else {
                        ret = 1;
                    }
                } else if (i1 == 1) {
                    if (arr[0] == 48) {
                        ret = 0;
                    } else {
                        ret = 1;
                    }
                }
                return ret;
            } catch (Exception var8) {
                var8.printStackTrace();
                if (0 == 2) {
                    if (arr[1] == 48) {
                        ret = 0;
                    } else {
                        ret = 1;
                    }
                } else if (0 == 1) {
                    if (arr[0] == 48) {
                        ret = 0;
                    } else {
                        ret = 1;
                    }
                }
                return ret;
            }
        } catch (Exception var9) {
            var9.printStackTrace();
            if (0 == 2) {
                if (0 == 1) {
                    if (arr[0] == 48) {
                        ret = 1;
                    } else {
                        ret = 0;
                    }
                }
            } else if (arr[1] == 48) {
                ret = 1;
            } else {
                ret = 0;
            }
            return ret;
        }
    }

    public int getPrinterStatus() {
        byte ret = -2;
        byte[] arr = new byte[4];
        try {
            RandomAccessFile e3 = new RandomAccessFile("/proc/printer", "r");
            try {
                int i1 = e3.read(arr);
                e3.close();
                if (i1 == 2) {
                    if (arr[1] != 48) {
                        ret = -1;
                    } else if (arr[0] == 49) {
                        ret = -2;
                    } else {
                        ret = 1;
                    }
                } else if (i1 == 1) {
                    if (arr[0] == 48) {
                        ret = 0;
                    } else {
                        ret = 1;
                    }
                }
                return ret;
            } catch (Exception var8) {
                var8.printStackTrace();
                if (0 == 2) {
                    if (arr[1] != 48) {
                        ret = 0;
                    } else if (arr[0] == 49) {
                        ret = -1;
                    } else {
                        ret = 1;
                    }
                } else if (0 == 1) {
                    if (arr[0] == 48) {
                        ret = 0;
                    } else {
                        ret = 1;
                    }
                }
                Log.e("sunchao", "first..i..=......" + 0);
                return ret;
            }
        } catch (Exception var9) {
            var9.printStackTrace();
            if (0 == 2) {
                if (0 == 1) {
                    if (arr[0] == 48) {
                        ret = 1;
                    } else {
                        ret = 0;
                    }
                }
            } else if (arr[1] != 48) {
                ret = 0;
            } else if (arr[0] == 49) {
                ret = 1;
            } else {
                ret = -1;
            }
            Log.e("sunchao", "second..i..=......" + 0);
            return ret;
        }
    }

    private void notifyToPrint(String data_file) {
        try {
            FileOutputStream e = new FileOutputStream("/proc/printer");
            e.write(data_file.getBytes());
            e.close();
        } catch (Exception var3) {
            var3.printStackTrace();
        }
    }

    private byte[] prepareTextHeader(int size, boolean r2lFlag) {
        byte i = 1;
        byte[] text_header = new byte[10];
        text_header[0] = 29;
        text_header[1] = 96;
        text_header[2] = 80;
        text_header[3] = 82;
        text_header[4] = 73;
        text_header[5] = 78;
        text_header[6] = 84;
        text_header[7] = 1;
        text_header[8] = (byte) size;
        if (!r2lFlag) {
            i = 0;
        }
        text_header[9] = i;
        return text_header;
    }

    private byte[] prepareBitmapHeader(int width, int height, int speed) {
        if (!(speed == 2 || speed == 3)) {
            speed = 2;
        }
        byte[] bArr = new byte[13];
        bArr[0] = 29;
        bArr[1] = 96;
        bArr[2] = 80;
        bArr[3] = 82;
        bArr[4] = 73;
        bArr[5] = 78;
        bArr[6] = 84;
        bArr[7] = 2;
        bArr[8] = (byte) speed;
        bArr[9] = (byte) (width / 8);
        bArr[11] = (byte) (height & 255);
        bArr[12] = (byte) ((65280 & height) >> 8);
        return bArr;
    }

    private void writeFile(String filename, byte[] header, byte[] data, int type) {
        if (type == 3) {
            type = 1;
            try {
                new FileOutputStream(filename, true);
            } catch (Exception var8) {
                var8.printStackTrace();
                return;
            }
        }
        try {
            FileOutputStream fos = new FileOutputStream(filename);
            if (header != null) {
                fos.write(header);
            }
            if (data != null) {
                if (type == 1) {
                    fos.write(data, 2, data.length - 2);
                } else if (type == 2) {
                    fos.write(data);
                }
            }
            fos.close();
        } catch (IOException var7) {
            var7.printStackTrace();
        }
    }

    private String checkAvailablePath() {
        String str = "/data/media/printer" + nTargetIndex + ".bin";
        nTargetIndex++;
        if (nTargetIndex > 50) {
            nTargetIndex = 0;
        }
        File file = new File(str);
        if (file.exists()) {
            file.delete();
        }
        return str;
    }

    private int ChangeInt(byte[] bi, int start) {
        return ((bi[start] & 255) << 24) | ((bi[start - 1] & 255) << 16) | ((bi[start - 2] & 255) << 8) | (bi[start - 3] & 255);
    }

    private int unsignedByteToInt(byte b) {
        return b & 255;
    }

    private void writeWord(ByteArrayOutputStream stream, int value) throws IOException {
        stream.write(new byte[]{(byte) (value & 255), (byte) ((value >> 8) & 255)}, 0, 2);
    }

    private void writeDword(ByteArrayOutputStream stream, long value) throws IOException {
        stream.write(new byte[]{(byte) ((int) (value & 255)), (byte) ((int) ((value >> 8) & 255)), (byte) ((int) ((value >> 16) & 255)), (byte) ((int) ((value >> 24) & 255))}, 0, 4);
    }

    private void writeLong(ByteArrayOutputStream stream, long value) throws IOException {
        stream.write(new byte[]{(byte) ((int) (value & 255)), (byte) ((int) ((value >> 8) & 255)), (byte) ((int) ((value >> 16) & 255)), (byte) ((int) ((value >> 24) & 255))}, 0, 4);
    }

    public boolean isPrinterOperation() {
        String content = "";
        try {
            RandomAccessFile e3 = new RandomAccessFile("proc/printer_status", "r");
            try {
                content = e3.readLine();
                e3.close();
                if (content == null || "".equals(content)) {
                    return false;
                }
                if ("0x0".equals(content)) {
                    return true;
                }
                if ("0x1".equals(content)) {
                }
                return false;
            } catch (Exception var6) {
                var6.printStackTrace();
                if (content != null) {
                }
                return false;
            }
        } catch (Exception var7) {
            var7.printStackTrace();
        }
        return false;
    }

    public int getCurrentPrinterStatus() {
        String content = "";
        try {
            RandomAccessFile e3 = new RandomAccessFile("proc/printer_errfb", "r");
            try {
                content = e3.readLine();
                e3.close();
                if ("".equals(content) || content == null || "0x0".equals(content)) {
                    return 0;
                }
                if ("0x1".equals(content)) {
                    return 1;
                }
                if ("0x2".equals(content)) {
                    return 2;
                }
                if ("0x3".equals(content)) {
                    return 3;
                }
                if ("0x4".equals(content)) {
                    return 4;
                }
                return 0;
            } catch (Exception var6) {
                var6.printStackTrace();
                if (!"".equals(content)) {
                }
                return 0;
            }
        } catch (Exception var7) {
            var7.printStackTrace();
            "".equals(content);
        }
        return 0;
    }

    public int readPrintDistanceFromNvram() {
        try {
            Class e = Class.forName("android.os.ServiceManager");
            IBinder binder = (IBinder) e.getMethod("getService", new Class[]{String.class}).invoke(e.newInstance(), new Object[]{"NvRAMAgent"});
            if (binder == null) {
                return 0;
            }
            try {
                byte[] e2 = NvRAMAgent.Stub.asInterface(binder).readFile(35);
                byte[] distances = new byte[4];
                for (int i = 1; i <= distances.length; i++) {
                    distances[i - 1] = e2[i + 519];
                }
                return bytesToInt(distances);
            } catch (Exception var6) {
                var6.printStackTrace();
                return 0;
            }
        } catch (Exception var7) {
            var7.printStackTrace();
            return 0;
        }
    }

    public int bytesToInt(byte[] res) {
        return (res[0] & 255) | ((res[1] << 8) & 65280) | ((res[2] << 24) >>> 8) | (res[3] << 24);
    }
}