package com.ex.gongsa.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Looper;
import android.util.Log;

public class HandlerThreadInGongsa extends android.os.HandlerThread {
    ProgressDialog dialog;
    Context context;
    String userInfo;
    public HandlerThreadInGongsa(String name, int priority, Context context,String userInfo) {

        super(name, priority);

        this.context = context;
    }
    public HandlerThreadInGongsa(String name) {
        super(name);
    }

    public HandlerThreadInGongsa(String name, int priority) {
        super(name, priority);
    }

    @Override
    protected void onLooperPrepared() {
        dialog = ProgressDialog.show(context,"","조회중...",true);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        super.onLooperPrepared();
    }

    @Override
    public void run() {
        super.run();
    }

    @Override
    public Looper getLooper() {
        return super.getLooper();
    }

    @Override
    public boolean quit() {
        return super.quit();
    }

    @Override
    public boolean quitSafely() {
        return super.quitSafely();
    }

    @Override
    public int getThreadId() {
        return super.getThreadId();
    }

    public void interruptHandler(){
        dialog.dismiss();;
        this.interrupt();
    }
}
