package com.crystal.administrator.scanner.util;

import android.annotation.SuppressLint;

import com.crystal.administrator.scanner.HttpResponse;


@SuppressLint("InlinedApi")
public abstract class ApiCallback<T>{

	public void onApiCallback(HttpResponse response, T data, boolean destroy){
		if (!destroy)
			onApiCallback(response, data);
	}

	protected void onApiCallback(HttpResponse response, T data){};
}
