package com.fangstar.spi.reader;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

public class TestPin extends Activity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    private Port mPort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_pin);
        mPort = new Port();
        ToggleButton tb = (ToggleButton) findViewById(R.id.nss);
        tb.setOnCheckedChangeListener(this);
        tb = (ToggleButton) findViewById(R.id.sck);
        tb.setOnCheckedChangeListener(this);
        tb = (ToggleButton) findViewById(R.id.mosi);
        tb.setOnCheckedChangeListener(this);
        tb = (ToggleButton) findViewById(R.id.reset);
        tb.setOnCheckedChangeListener(this);

        findViewById(R.id.bt_miso).setOnClickListener(this);
        findViewById(R.id.bt_irq).setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPort.close();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.nss:
                mPort.setNss(isChecked ? 1 : 0);
                break;
            case R.id.sck:
                mPort.setSck(isChecked ? 1 : 0);
                break;
            case R.id.mosi:
                mPort.setMosi(isChecked ? 1 : 0);
                break;
            case R.id.reset:
                mPort.setReset(isChecked ? 1 : 0);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_irq:
                TextView tv = (TextView) findViewById(R.id.tv_irq);
                tv.setText(String.valueOf((char)mPort.readIrq()));
                break;
            case R.id.bt_miso:
                tv = (TextView) findViewById(R.id.tv_miso);
                tv.setText(String.valueOf((char)mPort.readMiso()));
                break;
        }
    }
}
