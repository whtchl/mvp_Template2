package com.lifeng.f300.znpos2.scan.zbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;

import com.lifeng.f300.common.utils.LogUtils;
import com.lifeng.f300.znpos2.scan.decode.DecodeHandler;
import com.lifeng.f300.znpos2.scan.decode.DecodeThread;

import java.io.IOException;

/**
 * Created by happen on 2017/9/4.
 */

public class CameraManager {

    private static final String TAG = CameraManager.class.getSimpleName();

    private final CameraConfigurationManager configManager;

    private static final int MIN_FRAME_WIDTH = 240;
    private static final int MIN_FRAME_HEIGHT = 240;
    private static final int MAX_FRAME_WIDTH = 480;
    private static final int MID_FRAME_HEIGHT = 238;
    private static final int MAX_FRAME_HEIGHT = 360;

    private Camera camera;
    private boolean initialized;
    private Rect framingRect;

    private DecodeThread decodeThread;

    public CameraManager(Context context) {
        this.configManager = new CameraConfigurationManager(context);
    }

    /**
     * Opens the camera driver and initializes the hardware parameters.
     *
     * @param holder
     *            The surface object which the camera will draw preview frames
     *            into.
     * @throws IOException
     *             Indicates the camera driver failed to open.
     */
    @SuppressLint("NewApi") public synchronized void openDriver(SurfaceHolder holder) throws IOException {
        Camera theCamera = camera;
        if (theCamera == null) {
            theCamera = Camera.open();
            if (theCamera == null) {
                LogUtils.d("wang", "theCamera"+theCamera);
                throw new IOException();
            }
            camera = theCamera;
        }
        camera.setPreviewDisplay(holder);
        if (!initialized) {
            initialized = true;
            configManager.initFromCameraParameters(theCamera);
        }

        Camera.Parameters parameters = theCamera.getParameters();
        String parametersFlattened = parameters == null ? null : parameters.flatten(); // Save
        // these,
        // temporarily
        try {
            configManager.setDesiredCameraParameters(theCamera, false);
        } catch (RuntimeException re) {
            // Driver failed
            Log.w(TAG, "Camera rejected parameters. Setting only minimal safe-mode parameters");
            Log.i(TAG, "Resetting to saved camera params: " + parametersFlattened);
            // Reset:
            if (parametersFlattened != null) {
                parameters = theCamera.getParameters();
                parameters.unflatten(parametersFlattened);
                try {
                    theCamera.setParameters(parameters);
                    configManager.setDesiredCameraParameters(theCamera, true);
                } catch (RuntimeException re2) {
                    // Well, darn. Give up
                    Log.w(TAG, "Camera rejected even safe-mode parameters! No configuration");
                }
            }
        }

        camera.setDisplayOrientation(90);
    }

    public synchronized boolean isOpen() {
        return camera != null;
    }

    public Camera getCamera() {
        return camera;
    }

    /**
     * Closes the camera driver if still in use.
     */
    public synchronized void closeDriver() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

    /**
     * 获取相机分辨率
     *
     * @return
     */
    public Point getCameraResolution() {
        return configManager.getCameraResolution();
    }

    /**
     * Calculates the framing rect which the UI should draw to show the user
     * where to place the barcode. This target helps with alignment as well as
     * forces the user to hold the device far enough away to ensure the image
     * will be in focus.
     *
     * @return The rectangle to draw on screen in window coordinates.
     */
    public Rect getFramingRect() {
        Point screenResolution = configManager.getScreenResolution();
        if (framingRect == null) {
            if (camera == null) {
                return null;
            }
            int width = screenResolution.x * 3 / 4;
            if (width < MIN_FRAME_WIDTH) {
                width = MIN_FRAME_WIDTH;
            } else if (width > MAX_FRAME_WIDTH) {
                width = MAX_FRAME_WIDTH;
            } else {
                width = screenResolution.x;
            }
            int height = screenResolution.y * 3 / 4;
            if (height < MIN_FRAME_HEIGHT) {
                height = MIN_FRAME_HEIGHT;
            } else if (height > MAX_FRAME_HEIGHT) {
                height = MID_FRAME_HEIGHT;
            } else {
                height = MAX_FRAME_HEIGHT;
            }
            int leftOffset = (screenResolution.x - width) / 2;
            int topOffset = (screenResolution.y - height) / 2;
            framingRect = new Rect(leftOffset, topOffset, leftOffset + width, topOffset + height);
            Log.d(TAG, "Calculated framing rect: " + framingRect);
        }
        return framingRect;
    }

    public void setCameraCallbak(Camera.PreviewCallback pb, Camera.AutoFocusCallback ac) {
        camera.setPreviewCallback(pb);
        camera.autoFocus(ac);
    }

    public void releaseCamera() {
        if (camera != null) {
            Message.obtain(decodeThread.getHandler(), DecodeHandler.QUIT).sendToTarget();
            camera.setPreviewCallback(null);
            camera.release();
            camera = null;
        }
    }

    public void startPreview(Camera.PreviewCallback pb, Camera.AutoFocusCallback ac) {
        if (camera != null) {
            camera.startPreview();
            setCameraCallbak(pb, ac);
        }
    }

    public void stopPreview() {
        if (camera != null) {
            camera.setPreviewCallback(null);
            camera.stopPreview();
        }
    }

    public void setDecodeThread(DecodeThread decodeThread) {
        this.decodeThread = decodeThread;
    }

    public DecodeThread getDecodeThread() {
        return decodeThread;
    }

}

