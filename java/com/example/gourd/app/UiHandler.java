package com.example.gourd.app;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by ruzeya on 2014/09/17.
 */
public abstract class UiHandler extends Handler implements Runnable {

    public UiHandler() {
        super(Looper.getMainLooper());
    }

    public boolean post() {
        return post(this);
    }

    @Override
    public abstract void run();

}
