package com.crystal.administrator.scanner.util;


import com.crystal.administrator.scanner.HttpResponse;

public interface HttpUIDelegate {
	void showDialog(String message);
    void hideDialog(HttpResponse response, String message);
    void end(HttpResponse response);
    BaseHandler getResultHandler();
}
