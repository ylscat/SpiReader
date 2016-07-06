package com.fangstar.spi.reader;

import android.util.Log;

import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created at 2016/7/6.
 *
 * @author YinLanShan
 */
public class Port {
    private static final String TAG = "Port";

    private static final String DIR = "/sys/class/gpio/";
    private static final String NSS = DIR + "gpio87/value";
    private static final String SCK = DIR + "gpio86/value";
    private static final String MOSI = DIR + "gpio89/value";
    private static final String MISO = DIR + "gpio88/value";
    private static final String RESET = DIR + "gpio70/value";
    private static final String IRQ = DIR + "gpio69/value";

    public RandomAccessFile miso, irq;
    public FileOutputStream nss, sck, mosi, reset;

    public Port() {
        miso = openInput(MISO);
        irq = openInput(IRQ);

        nss = openOutput(NSS);
        sck = openOutput(SCK);
        mosi = openOutput(MOSI);
        reset = openOutput(RESET);
    }

    private RandomAccessFile openInput(String path) {
        try {
            return new RandomAccessFile(path, "r");
        }
        catch (IOException e) {
            Log.e(TAG, "OPen pin failed: " + path);
        }
        return null;
    }

    private FileOutputStream openOutput(String path) {
        try {
            return new FileOutputStream(path);
        }
        catch (IOException e) {
            Log.e(TAG, "OPen pin failed: " + path);
        }
        return null;
    }

    public void close() {
        close("miso", miso);
        close("irq", irq);
        close("nss", nss);
        close("sck", sck);
        close("mosi", mosi);
        close("reset", reset);
    }

    private void close(String name, Closeable f) {
        if(f == null)
            return;
        try {
            f.close();
        }
        catch (IOException e) {
            Log.e(TAG, "Close pin failed: " + name);
        }
    }

    public int readMiso() {
        try {
            miso.seek(0);
            return miso.read();
        }
        catch (IOException e) {
            return -1;
        }
    }

    public int readIrq() {
        try {
            irq.seek(0);
            return irq.read();
        }
        catch (IOException e) {
            return -1;
        }
    }

    public void setNss(int value) {
        try {
            nss.write('0' + value);
        }
        catch (IOException e) {
            //ignore
        }
    }

    public void setSck(int value) {
        try {
            sck.write('0' + value);
        }
        catch (IOException e) {
            //ignore
        }
    }

    public void setMosi(int value) {
        try {
            mosi.write('0' + value);
        }
        catch (IOException e) {
            //ignore
        }
    }

    public void setReset(int value) {
        try {
            reset.write('0' + value);
        }
        catch (IOException e) {
            //ignore
        }
    }
}
