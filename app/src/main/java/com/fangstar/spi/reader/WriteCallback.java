package com.fangstar.spi.reader;

/**
 * Created at 2016/7/7.
 *
 * @author YinLanShan
 */
public interface WriteCallback {
    void reg(int address, byte[] data);
}
