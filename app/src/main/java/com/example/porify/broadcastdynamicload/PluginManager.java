package com.example.porify.broadcastdynamicload;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import dalvik.system.DexClassLoader;

public class PluginManager {
    private static final String TAG = PluginManager.class.getSimpleName();
    private static final PluginManager sInstance = new PluginManager();

    private PluginManager() {

    }

    public static PluginManager getInstance() {
        return sInstance;
    }

    public void load(Context context, File inFile) {
        Log.d(TAG, "load: ");

        try {
            Class<?> packageParserClass = Class.forName("android.content.pm.PackageParser");
            Object packageParser = packageParserClass.newInstance();
            Method method = packageParserClass.getDeclaredMethod("parsePackage", File.class, Integer.class);
            Object packageInfo = method.invoke(packageParser, inFile, PackageManager.GET_RECEIVERS);

            // receivers;
            Field receiverField = packageInfo.getClass().getDeclaredField("receivers");
            List receivers = (List) receiverField.get(packageInfo);
            DexClassLoader dexClassLoader = new DexClassLoader(inFile.getAbsolutePath(),
                    context.getDir("plugin", Context.MODE_PRIVATE).getAbsolutePath(),
                    null, context.getClassLoader());
            Class<?> componentClass = Class.forName("android.content.pm.PackageParser$Component");
            Field intentField = componentClass.getClass().getDeclaredField("intents");
            for (Object receiver : receivers) {
                //class name
                Field nameField = receiver.getClass().getDeclaredField("className");
                String name = (String) nameField.get(receiver);
                Class broadcastReceiverClass = dexClassLoader.loadClass(name);
                BroadcastReceiver broadcastReceiver = (BroadcastReceiver) broadcastReceiverClass.newInstance();

                // get intent filter
                List<? extends  IntentFilter> filters = (List<? extends IntentFilter>) intentField.get(receiver);
                for (IntentFilter intentFilter: filters) {
                    context.registerReceiver(broadcastReceiver, intentFilter);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
