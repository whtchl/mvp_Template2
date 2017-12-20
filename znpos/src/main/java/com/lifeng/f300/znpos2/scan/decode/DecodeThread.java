package com.lifeng.f300.znpos2.scan.decode;

import java.util.concurrent.CountDownLatch;

import com.lifeng.f300.znpos2.module.user.CaptureScanActivity;

import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;

public class DecodeThread extends Thread {

	private CaptureScanActivity activity;

	private Handler handler;
	private final CountDownLatch handlerInitLatch;

	private Rect mRect;

	public DecodeThread(CaptureScanActivity activity, Rect mRect) {
		this.activity = activity;
		this.mRect = mRect;
		handlerInitLatch = new CountDownLatch(1);
	}

	public Handler getHandler() {
		try {
			handlerInitLatch.await();
		} catch (InterruptedException ie) {
			// continue?
		}
		return handler;
	}

	@Override
	public void run() {
		Looper.prepare();
		handler = new DecodeHandler(activity, mRect);
		handlerInitLatch.countDown();
		Looper.loop();
	}

}
