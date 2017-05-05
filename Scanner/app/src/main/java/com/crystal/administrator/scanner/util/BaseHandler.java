package com.crystal.administrator.scanner.util;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public final class BaseHandler extends Handler {
    public static Callback NULL = new Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            return false;
        }
    };

    public boolean isDestroy;
    public final Callback callback;


    public BaseHandler(Callback callback) {
        this(Looper.myLooper(), callback);
    }

    public BaseHandler(Looper looper, Callback callback) {
        super(looper != null ? looper : Looper.myLooper() != null ? Looper.myLooper() : Looper.getMainLooper(), callback);
        this.callback = callback;
    }
}
