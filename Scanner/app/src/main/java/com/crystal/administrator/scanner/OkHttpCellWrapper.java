package com.crystal.administrator.scanner;

import okhttp3.Call;

final class OkHttpCellWrapper implements HttpCancel {
	Call call;
	
	public OkHttpCellWrapper(Call call) {
		this.call = call;
	}

	@Override
	public void cancel() {
		call.cancel();
	}

	@Override
	public boolean isExecuted() {
		return call.isExecuted();
	}

	@Override
	public boolean isCanceled() {
		return call.isCanceled();
	}

}
