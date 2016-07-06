package com.fangstar.spi.reader;

import android.os.Process;
import android.util.Log;

/**
 * Created at 2016/7/6.
 *
 * @author YinLanShan
 */
public class ReadPinThread extends Thread {
    private Port mPort;
    private boolean halt;
    private long count;

    public ReadPinThread(Port port) {
        super();
        mPort = port;
    }

    @Override
    public void run() {
        setPriority(Process.THREAD_PRIORITY_BACKGROUND);
        while (!halt) {
            if(mPort.readMiso() == '1') {
                count++;
                Log.d("RT", "Count:" + count);
            }
            try {
                sleep(10, 0);
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    public void halt() {
        halt = true;
    }
}
