package com.crystal.administrator.scanner;

public interface HttpCancel {
	void cancel();
	boolean isExecuted();
	boolean isCanceled();
}
