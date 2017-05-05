package com.crystal.administrator.scanner;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.crystal.administrator.scanner.util.ApiCallback;
import com.crystal.administrator.scanner.util.BaseDTOModel;
import com.crystal.administrator.scanner.util.BaseHandler;
import com.crystal.administrator.scanner.util.HttpUIDelegate;

import java.io.IOException;

public class ApiHttpCallbackWarpper<T> implements HttpCallback {
    Class<T> clazz;
    Context context;
    HttpUIDelegate delegate;
    String message;
    BaseHandler handler;
    ApiCallback<T> callback;


    public ApiHttpCallbackWarpper(Class<T> clazz, Context context, HttpUIDelegate delegate, String message, ApiCallback<T> callback) {
        this.clazz = clazz;
        this.context = context;
        this.delegate = delegate;
        this.message = message;
        this.handler = delegate.getResultHandler();
        this.callback = callback;
    }

    @Override
    public void onCallback(final HttpResponse response) {
        T data = null;
        try {
            if (response.isSuccessful()) {
                data = response.bodyJson(clazz);
            }
        } catch (IOException e) {
            Log.w("ApiCallback", "Parse Class Error", e);
        }
        callApiCallBack(response, data);
    }

    public void callApiCallBack(final HttpResponse response, final T data) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (data != null && data instanceof BaseDTOModel) {
                    if ("10001".equals(((BaseDTOModel) data).code)) {

                    }
                }
                callback.onApiCallback(response, data, handler.isDestroy);
                if (delegate != null && !handler.isDestroy) {
                    if (!TextUtils.isEmpty(message))
                        delegate.hideDialog(response, message);
                    delegate.end(response);
                }
            }
        });
    }
}
