package android.serialport;

import java.io.IOException;

import android.content.Context;
import android.widget.Toast;
//import android.witsi.arqII.DataTransmit;
import android.witsi.arqII.api.DataTransmit;

import com.jude.utils.JUtils;

public class SerialDev {
	private String TAG = "SerialDev";
	private Dev serialDev = null;
	private Context mContext;
	byte[] buff = new byte[3072];

	private boolean stopDataTransmit = false;

	public SerialDev(){}
	private volatile static SerialDev instance = null;
	public static SerialDev getInstance() {
		if (instance == null) {
			synchronized (SerialDev.class) {
				if (instance == null) {
					instance = new SerialDev();
				}
			}
		}
		return instance;
	}

	public void close() {
		if (serialDev != null)
			serialDev.closePort();
	}

	public void setDataTransmitState(boolean state) {
		stopDataTransmit = state;
	}

	public DataTransmit initSerialDev(Context context, String devName, int bandrate) {
		mContext = context;
		DataTransmit dataTransmit = null;

		serialDev = new Dev();
		try {

			if (!serialDev.openPort(devName, bandrate)) {
				JUtils.Log("serialDev is null");
				serialDev = null;
				return null;
			}
			JUtils.Log("serialDev is not null");
			dataTransmit = mSerialDataTransmit;
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return dataTransmit;
	}

	public void serialSendData(byte[] data) {
		if (serialDev != null) {
			serialDev.writePort(data);
		} else {
			Toast.makeText(mContext, "串口异常", Toast.LENGTH_SHORT).show();
		}
	}

	public void serialSendData(byte[] data, int offset, int len) {
		if (serialDev != null) {
			serialDev.writePort(data, offset, len);
		} else {
			Toast.makeText(mContext, "串口异常", Toast.LENGTH_SHORT).show();
		}
	}

	public byte[] serialrecvData() {
		int len = 0;
		if (serialDev != null) {
			len = serialDev.readPort(buff);
		} else {
			Toast.makeText(mContext, "串口异常", Toast.LENGTH_SHORT).show();
			return null;
		}

		if (len <= 0)
			return null;

		byte[] recv = new byte[len];

		System.arraycopy(buff, 0, recv, 0, len);
		return recv;

	}

	DataTransmit mSerialDataTransmit = new DataTransmit() {
		@Override
		public byte[] _arq_receive_frame() {
			if (stopDataTransmit)
				return null;
			else
				return serialrecvData();
		}

		@Override
		public int _arq_send_frame(byte[] arg0) {
			if (!stopDataTransmit)
				serialSendData(arg0);
			return 0;
		}
	};

}