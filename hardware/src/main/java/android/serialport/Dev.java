package android.serialport;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;

public class Dev {

	static final String TAG = "Dev";
	private SerialPort mSerialPort;
	private InputStream mInputStream;
	private OutputStream mOutputStream;

	/*
	 * Do not remove or rename the field mFd: it is used by native method
	 * close();
	 */
	public boolean openPort(String portName, int bandrate) throws SecurityException, IOException {
		boolean bret = false;
		try {
			// "/dev/ttyS2"
			mSerialPort = new SerialPort(new File("/dev/" + portName), bandrate);
			mOutputStream = mSerialPort.getOutputStream();
			mInputStream = mSerialPort.getInputStream();
			bret = true;
		} catch (SecurityException e) {
			// DisplayError("R.string.error_security");
		} catch (IOException e) {
			// DisplayError(R.string.error_unknown);
		} catch (InvalidParameterException e) {
			// DisplayError(R.string.error_configuration);
		}
		return bret;
	}

	public void closePort() {

		if (mSerialPort != null) {
			try {
				mInputStream.skip(1024);
				mOutputStream.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			mSerialPort.close();
			mSerialPort = null;
		}
	}

	public void writePort(byte[] byOrder) {
		try {
			mOutputStream.write(byOrder);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writePort(byte[] byOrder, int offset, int len) {
		try {
			mOutputStream.write(byOrder, offset, len);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int readPort(byte[] buff) {
		int nRet = 0;
		try {
			nRet = mInputStream.read(buff);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nRet;
	}
}
