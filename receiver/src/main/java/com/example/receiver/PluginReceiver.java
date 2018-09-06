package com.example.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class PluginReceiver extends BroadcastReceiver {

    private static final String MAIN_RECEIVER = "com.example.porify.MAIN_RECEIVER";

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "I am plugin, i received you, " +
                "and i will send a broadcast to you", Toast.LENGTH_LONG).show();

        context.sendBroadcast(new Intent(MAIN_RECEIVER));
    }
}
