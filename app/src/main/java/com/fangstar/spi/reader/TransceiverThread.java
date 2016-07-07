package com.fangstar.spi.reader;

import android.os.Handler;
import android.os.Process;

import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created at 2016/7/6.
 *
 * @author YinLanShan
 */
public class TransceiverThread extends Thread {
    private SpiDriver mDriver;
    private boolean halt;
    private static final Handler HANDLER = new Handler();

    private LinkedBlockingDeque<Command> mTransmittingQueue = new LinkedBlockingDeque<>();

    public TransceiverThread(Port port) {
        super();
        mDriver = new SpiDriver(port);
    }

    @Override
    public void run() {
        setPriority(Process.THREAD_PRIORITY_BACKGROUND);
        while (!halt) {
            try {
                Command cmd = mTransmittingQueue.take();
                mDriver.setNss(0);
                for(int i = 0; i < cmd.mTx.length; i++) {
                    cmd.mRx[i] = mDriver.transmit(cmd.mTx[i]);
                }
                mDriver.setNss(1);
                HANDLER.post(cmd);
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    public void readReg(int address, RegCallback callback) {
        mTransmittingQueue.add(new ReadReg(address, callback));
    }

    public void writeReg(int address, int value, WriteCallback callback) {
        mTransmittingQueue.add(new WriteReg(address, value, callback));
    }

    public void halt() {
        halt = true;
        interrupt();
    }

    abstract class Command implements Runnable {
        public byte[] mTx;
        public byte[] mRx;
    }

    class ReadReg extends Command {
        private RegCallback mCallback;

        public ReadReg(int address, RegCallback callback) {
            mCallback = callback;
            byte a = (byte)(0x80 | ((address&0x3F) << 1));
            mTx = new byte[]{a, 0};
            mRx = new byte[2];
        }

        @Override
        public void run() {
            int address = (mTx[0] >> 1) & 0x3F;
            mCallback.reg(address, mRx[1]&0xFF);
        }
    }

    class WriteReg extends Command {
        private WriteCallback mCallback;

        public WriteReg(int address, int value, WriteCallback callback) {
            byte a = (byte)((address&0x3F) << 1);
            mTx = new byte[]{a, (byte)value};
            mRx = new byte[2];
            mCallback = callback;
        }

        @Override
        public void run() {
            int address = (mTx[0] >> 1) & 0x3F;
            mCallback.reg(address, mRx);
        }
    }
}
