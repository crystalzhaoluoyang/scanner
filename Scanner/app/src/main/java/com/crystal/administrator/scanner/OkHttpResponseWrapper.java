package com.crystal.administrator.scanner;

import android.util.Log;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.Response;

final class OkHttpResponseWrapper implements HttpResponse {
	Response response;

	public OkHttpResponseWrapper(Response response) {
		this.response = response;
	}

	@Override
	public int getStatusCode() {
		return response.code();
	}

	@Override
	public boolean isSuccessful() {
		return response.isSuccessful();
	}

	@Override
	public String header(String key) {
		return response.header(key);
	}

	@Override
	public String header(String key, String defaultValue) {
		return response.header(key, defaultValue);
	}

	@Override
	public byte[] bodyBytes() throws IOException {
		return response.body().bytes();
	}

	@Override
	public Reader bodyCharStream() {
		return response.body().charStream();
	}

	@Override
	public String bodyString() throws IOException {
		return response.body().string();
	}

	@Override
	public InputStream bodyByteStream() {
		return response.body().byteStream();
	}

	@Override
	public Charset charset() {
		return response.body().contentType().charset();
	}

	@Override
	public String contentType() {
		MediaType mediaType = response.body().contentType();
		return new StringBuilder(mediaType.type()).append("/").append(mediaType.subtype()).toString();
	}

	@Override
	public long contentLength() {
		return response.body().contentLength();
	}

	@Override
	public <T> T bodyJson(Class<T> clazz) throws IOException {
		String tempResponse =  response.body().string();
//		String.format(tempResponse, u)
		Log.i("Debug","response:"+tempResponse);
		return JSON.parseObject(tempResponse, clazz);
	}
}
