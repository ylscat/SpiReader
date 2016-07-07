package com.fangstar.spi.reader;

/**
 * Created at 2016/7/7.
 *
 * @author YinLanShan
 */
public class SpiDriver {
    private Port mPort;

    public SpiDriver(Port port) {
        this.mPort = port;
        port.setNss(1);
        port.setSck(0);
    }

    public void setNss(int bit) {
        mPort.setNss(bit);
    }

    public byte transmit(byte tx) {
        Port port = mPort;
        byte r = 0;
        for(int i = 8; i > 0; i--) {

            int v = (tx&0x80) == 0 ? 0 : 1;
            port.setSck(0);
            port.setMosi(v);
            delay(10);

            port.setSck(1);
            int miso = port.readMiso();
            r = (byte)(r<<1);
            if(miso != '0') {
                r |= 1;
            }

            tx = (byte)(tx<<1);
            delay(10);
        }

        port.setSck(0);

        return r;
    }

    private void delay(int milliSeconds) {
//        try {
//            Thread.sleep(milliSeconds, 0);
//        }
//        catch (InterruptedException e) {
//            //ignore
//        }
    }
}
