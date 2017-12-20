package com.lifeng.f300.znpos2.scan.zbar;

import java.io.IOException;

import android.hardware.Camera.AutoFocusCallback;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.lifeng.f300.znpos2.module.user.CaptureScanActivity;
import com.lifeng.f300.znpos2.scan.decode.DecodeThread;

/** A basic Camera preview class */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
	private SurfaceHolder mHolder;
	private CaptureScanActivity mActivity;

	private CameraManager mCameraManager;
	private CaptureScanActivity.DecodePreviewCallback previewCallback;
	private AutoFocusCallback autoFocusCallback;

	private DecodeThread decodeThread;

	@SuppressWarnings("deprecation")
	public CameraPreview(CaptureScanActivity activity, CameraManager cameraManager, CaptureScanActivity.DecodePreviewCallback previewCb,
						 AutoFocusCallback autoFocusCb) {
		super(activity);
		mActivity = activity;
		mCameraManager = cameraManager;
		previewCallback = previewCb;
		autoFocusCallback = autoFocusCb;

		/*
		 * Set camera to continuous focus if supported, otherwise use software
		 * auto-focus. Only works for API level >=9.
		 */
		/*
		 * Camera.Parameters parameters = camera.getParameters(); for (String f
		 * : parameters.getSupportedFocusModes()) { if (f ==
		 * Parameters.FOCUS_MODE_CONTINUOUS_PICTURE) {
		 * mCamera.setFocusMode(Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
		 * autoFocusCallback = null; break; } }
		 */

		// Install a SurfaceHolder.Callback so we get notified when the
		// underlying surface is created and destroyed.
		mHolder = getHolder();
		mHolder.addCallback(this);

		// deprecated setting, but required on Android versions prior to 3.0
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	public void surfaceCreated(SurfaceHolder holder) {
		// The Surface has been created, now tell the camera where to draw the
		// preview.
		try {
			mCameraManager.openDriver(holder);
			mCameraManager.getCamera().startPreview();
		} catch (IOException e) {
			Log.d("DBG", "Error setting camera preview: " + e.getMessage());
		}
		Log.e("hcm", "start");
		mActivity.initCrop();
		Log.e("hcm", "stop");
		decodeThread = new DecodeThread(mActivity, mActivity.getCopRect());
		decodeThread.start();
		mCameraManager.setDecodeThread(decodeThread);
		previewCallback.setDecodeHandler(decodeThread.getHandler());

	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// Camera preview released in activity
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		/*
		 * If your preview can change or rotate, take care of those events here.
		 * Make sure to stop the preview before resizing or reformatting it.
		 */
		if (mHolder.getSurface() == null) {
			// preview surface does not exist
			return;
		}
		mCameraManager.setCameraCallbak(previewCallback, autoFocusCallback);

	}
}
