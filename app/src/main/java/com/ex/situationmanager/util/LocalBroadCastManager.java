package com.ex.situationmanager.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

public class LocalBroadCastManager extends BroadcastReceiver {

    public Context context;
    private static LocalBroadcastManager manager = null;

    public LocalBroadCastManager() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {

    }

    @Override
    public IBinder peekService(Context myContext, Intent service) {
        return super.peekService(myContext, service);
    }

    public static LocalBroadcastManager getInstance(Context context){
        manager= LocalBroadcastManager.getInstance(context);
        return manager;
    }

    public void registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        manager.registerReceiver(receiver, filter);
    }

    public void unregisterReceiver(BroadcastReceiver receiver) {
        manager.unregisterReceiver(receiver);
    }

    public boolean sendBroadcast(Intent intent) {
        return manager.sendBroadcast(intent);
    }

    public void sendBroadcastSync(Intent intent) {
        manager.sendBroadcastSync(intent);
    }
}
