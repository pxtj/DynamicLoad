package com.example.porify.broadcastdynamicload;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private static final String PLUGIN_ACTION = "com.example.porify.Receiver.PLUGIN_ACTION";
    private static final String MAIN_RECEIVER = "com.example.porify.MAIN_RECEIVER";


    private Button mBtnLoad;
    private Button mSendBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtnLoad = findViewById(R.id.btn_load);
        mSendBtn = findViewById(R.id.btn_replay);
        mBtnLoad.setOnClickListener(this);
        mSendBtn.setOnClickListener(this);
        registerReceiver(mReceiver, new IntentFilter(MAIN_RECEIVER));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_load:
                load();
                break;
            case R.id.btn_replay:
                sendBroadcast(new Intent(PLUGIN_ACTION));
                break;
        }
    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "I am main, i have received your broadcast, " +
                    "and i will send a broadcast to you", Toast.LENGTH_LONG).show();

            Log.d(TAG, "onReceive: send plugin broadcast");
            context.sendBroadcast(new Intent(PLUGIN_ACTION));
        }
    };

    public void load() {
        File file = new File("/sdcard/receiver.apk");
        Log.d(TAG, "load: file:" + file);
        PluginManager.getInstance().load(this, file);
    }
}
