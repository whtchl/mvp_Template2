package com.lifeng.f300.znpos2.scan.decode;

import com.lifeng.f300.znpos2.module.user.CaptureScanActivity;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
public class DecodeHandler extends Handler {

	public final static int DECODE_OK = 0x9000;
	public final static int DECODE_FAIL = 0x9001;
	public static final int DECODE = 0x9999;
	public static final int QUIT = 0x9998;
	private CaptureScanActivity activity;
	private ImageScanner mImageScanner = null;
	private Rect mCropRect = null;

	DecodeHandler(CaptureScanActivity activity, Rect mCropRect) {
		this.activity = activity;
		this.mCropRect = mCropRect;
		mImageScanner = new ImageScanner();
		mImageScanner.setConfig(0, Config.X_DENSITY, 3);
		mImageScanner.setConfig(0, Config.Y_DENSITY, 3);
	}

	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
		case DECODE:
			decode((byte[]) msg.obj, msg.arg1, msg.arg2);
			break;
		case QUIT:
			Looper.myLooper().quit();
			break;
		default:
			break;
		}
	}

	private void decode(byte[] data, int width, int height) {

		if (data == null) {
			return;
		}
		// 这里需要将获取的data翻转一下，因为相机默认拿的的横屏的数据
		byte[] rotatedData = new byte[data.length];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++)
				rotatedData[x * height + height - y - 1] = data[x + y * width];
		}

		Image barcode = new Image(height, width, "Y800");
		barcode.setData(rotatedData);
		barcode.setCrop(mCropRect.left, mCropRect.top, mCropRect.width(), mCropRect.height());

		int result = mImageScanner.scanImage(barcode);
		String resultStr = null;

		if (result != 0) {
			SymbolSet syms = mImageScanner.getResults();
			for (Symbol sym : syms) {
				resultStr = sym.getData();
			}
		}

		if (!TextUtils.isEmpty(resultStr)) {
			activity.getHandler().obtainMessage(DECODE_OK, resultStr).sendToTarget();
		} else {
			activity.getHandler().obtainMessage(DECODE_FAIL).sendToTarget();
		}

	}

}
