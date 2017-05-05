package com.crystal.administrator.scanner;

import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

final class OkHttpCallbackWrapper implements Callback {
	HttpCallback callback;

	public OkHttpCallbackWrapper(HttpCallback callback) {
		this.callback = callback;
	}

	@Override
	public void onFailure(Call call, IOException e) {
		Log.w("HTTP", "error", e);
		callback.onCallback(HttpResponse.NULL);
	}

	@Override
	public void onResponse(Call call, Response response) throws IOException {
		callback.onCallback(new OkHttpResponseWrapper(response));
		response.close();
	}

}
