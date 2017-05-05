package com.crystal.administrator.scanner;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;

public interface HttpResponse {
	public static final int STATUS_NULL = -1;

	/**
	 * 响应码
	 * 
	 * @return
	 */
	int getStatusCode();

	boolean isSuccessful();

	String header(String key);

	String header(String key, String defaultValue);

	byte[] bodyBytes() throws IOException;

	Reader bodyCharStream();

	String bodyString() throws IOException;

	InputStream bodyByteStream();

	<T> T bodyJson(Class<T> clazz) throws IOException;

	Charset charset();

	String contentType();

	long contentLength();

	HttpResponse NULL = new HttpResponse() {
		@Override
		public boolean isSuccessful() {
			return false;
		}

		@Override
		public String header(String key, String defaultValue) {
			return null;
		}

		@Override
		public String header(String key) {
			return null;
		}

		@Override
		public int getStatusCode() {
			return STATUS_NULL;
		}

		@Override
		public String contentType() {
			return null;
		}

		@Override
		public long contentLength() {
			return 0;
		}

		@Override
		public Charset charset() {
			return Charset.defaultCharset();
		}

		@Override
		public String bodyString() throws IOException {
			return null;
		}

		@Override
		public Reader bodyCharStream() {
			return null;
		}

		@Override
		public byte[] bodyBytes() throws IOException {
			return null;
		}

		@Override
		public InputStream bodyByteStream() {
			return null;
		}

		@Override
		public <T> T bodyJson(Class<T> clazz) throws IOException {
			return null;
		}
	};

	HttpResponse CACHE = new HttpResponse() {

		@Override
		public boolean isSuccessful() {
			return true;
		}

		@Override
		public String header(String key, String defaultValue) {
			return null;
		}

		@Override
		public String header(String key) {
			return null;
		}

		@Override
		public int getStatusCode() {
			return 200;
		}

		@Override
		public String contentType() {
			return null;
		}

		@Override
		public long contentLength() {
			return 0;
		}

		@Override
		public Charset charset() {
			return null;
		}

		@Override
		public String bodyString() throws IOException {
			return null;
		}

		@Override
		public <T> T bodyJson(Class<T> clazz) throws IOException {
			return null;
		}

		@Override
		public Reader bodyCharStream() {
			return null;
		}

		@Override
		public byte[] bodyBytes() throws IOException {
			return null;
		}

		@Override
		public InputStream bodyByteStream() {
			return null;
		}
	};
}
