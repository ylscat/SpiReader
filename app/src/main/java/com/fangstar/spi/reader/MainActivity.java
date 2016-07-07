package com.fangstar.spi.reader;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends Activity implements
        View.OnClickListener,
        TextView.OnEditorActionListener, RegCallback, WriteCallback {
    private Port mPort;
    private TransceiverThread mThread;
    private EditText mEditText, mValue;
    private ArrayAdapter<String> mAdapter;
    private ArrayList<String> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mPort = new Port();
        mThread = new TransceiverThread(mPort);
        mThread.start();
        mEditText = (EditText)findViewById(R.id.reg);
        mEditText.setOnEditorActionListener(this);
        mValue = (EditText)findViewById(R.id.value);
        mValue.setOnEditorActionListener(this);
        findViewById(R.id.read).setOnClickListener(this);
        findViewById(R.id.write).setOnClickListener(this);
        ListView lv = (ListView)findViewById(R.id.list);
        mAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                mList);
        lv.setAdapter(mAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mThread.halt();
        mPort.close();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.read:
                read();
                break;
            case R.id.write:
                write();
                break;
        }
    }

    private void read() {
        String text = mEditText.getText().toString().trim();
        int addr;
        try {
            addr = Integer.parseInt(text, 16);
        }
        catch (NumberFormatException e) {
            Toast.makeText(this, "Bad address", Toast.LENGTH_SHORT).show();
            return;
        }

        mThread.readReg(addr, this);
    }

    private void write() {
        String text = mEditText.getText().toString().trim();
        int addr;
        try {
            addr = Integer.parseInt(text, 16);
        }
        catch (NumberFormatException e) {
            Toast.makeText(this, "Bad address", Toast.LENGTH_SHORT).show();
            return;
        }

        text = mValue.getText().toString().trim();
        int value;
        try {
            value = Integer.parseInt(text, 16);
        }
        catch (NumberFormatException e) {
            Toast.makeText(this, "Bad address", Toast.LENGTH_SHORT).show();
            return;
        }

        mThread.writeReg(addr, value, this);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        switch (v.getId()) {
            case R.id.reg:
                read();
                break;
            case R.id.value:
                write();
                break;
        }
        return true;
    }

    @Override
    public void reg(int address, int data) {
        if(mList.size() == 10)
            mList.remove(9);
        mList.add(0, String.format("0x%X  %X", address, data));
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void reg(int address, byte[] data) {
        Toast.makeText(this,
                String.format("Written, %s",
                        Arrays.toString(data)),
                Toast.LENGTH_LONG).show();
    }
}
