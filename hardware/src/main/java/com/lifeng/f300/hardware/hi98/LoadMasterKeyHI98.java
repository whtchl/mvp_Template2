package com.lifeng.f300.hardware.hi98;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.lifeng.f300.common.entites.TransResponse;

/**
 * Decripe : 导入morefun设备的主密钥
 * Date : 2017年2月10日
 * Version : 1.0 <br/>
 *
 * @author : wangkeze
 */
public class LoadMasterKeyHI98 {
	private TransResponse transResponse;
	private String masterKey = "";
	private String masterKeyMin = "";
	private String masterKeyValbuf = "";
	protected static final int DELAY_SHENGBEN = 0x0D;
	protected static final int SUCCESS_SHENGBEN = 0;
	protected static final int FAILS_SHENGBEN = -1;
	private Bundle data;
	private Message msg;
	
	/**
	 * 导入主密钥
	 * @param "wtDevContrl" 芯片对象
	 * @param transResponse 签到的数据
	 */
	public LoadMasterKeyHI98(TransResponse transResponse) {
		this.transResponse = transResponse;
	}
	
/*	public void loadMasterHI98SecretInSystem() {
		data = new Bundle();
		msg = Message.obtain();
		//开启线程导入aid
		loadCleanData = new Thread(new Runnable() {
			@Override
			public void run() {
				int result = PayApplication.pedHI98.Ped_formatKeyArea();
				if(null != PayApplication.pedHI98){
					if(result == PayApplication.pedHI98.PED_OK){
						handlerDelay.sendEmptyMessage(DELAY_SHENGBEN); 
					}else{
						if(result ==PayApplication.pedHI98.PED_REQUEST_ERROR){
							LogUtils.d("wang"," ** 请求错误 ");
						}else if(result ==PayApplication.pedHI98.PED_KEYNOTEXIST_ERROR){
							LogUtils.d("wang","*** 密钥不存在 ");
						}else if(result == PayApplication.pedHI98.PED_KEYLENMISMATCH_ERROR){
							LogUtils.d("wang","**** 密钥长度算法不匹配 ");
						}
						msg.what=FAILS_SHENGBEN;
						data.putString(Constants.TOAST, "格式化密钥失败");
						msg.setData(data);
						handlerHI98.sendMessage(msg);
					}
				}else{
					msg.what=FAILS_SHENGBEN;
					data.putString(Constants.TOAST, "设备底层加载失败");
					msg.setData(data);
					handlerHI98.sendMessage(msg);
				}
			}
		});
		loadCleanData.start();
	}*/
	
	
	
	public static byte hex2byte(char hex)
	{
		if ((hex <= '9') && (hex >= '0')) {
			return (byte)(hex - '0');
		}
		
		if ((hex <= 'f') && (hex >= 'a')) {
			return (byte)(hex - 'a' + 10);
		}

		if ((hex <= 'F') && (hex >= 'A')) {
			return (byte)(hex - 'A' + 10);
		}

		return 0;
	}
	/**
	 * 十六进制字符串转换为二进制byte数据.若不是偶数个右补'0'
	 */
	public static byte[] hexString2Bytes(String data)
	{		
		StringBuilder buffer = new StringBuilder(data);
		
		byte[] result = new byte[(buffer.length() + 1) / 2];
		if ((buffer.length() & 0x01) == 1) {
			buffer.append( '0' );
		}
		
		for (int i = 0; i < result.length; i++) {
			result[i] = ((byte)(hex2byte(buffer.charAt(i * 2 + 1)) 
								| hex2byte(buffer.charAt(i * 2)) << 4));
		}
		return result;
	}
	
	
	public static byte[] parseHexStr2Byte(String hexStr) {

		if (hexStr.length() < 1){
			return null;
		}
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}
	
	/**
	 * 导入主密钥
	 * @param transResponse
	 */
	public void saveMasterHI98Key(TransResponse transResponse) {
/*		if (!TextUtils.isEmpty(transResponse.MASTERKEY) && transResponse.MASTERKEY.length() >= 32) {
			// 主密钥：下发下来是明文的：先通过32个F对明文的主密钥加密
			masterKey= transResponse.MASTERKEY.substring(0, 32).toUpperCase();
			// 校验码: 用明文的主密钥对16个0加密，取前8位为校验码
			masterKeyValbuf= transResponse.MASTERKEY.substring(32).toUpperCase();
			LogUtils.d("wang", "主密钥密文:" + masterKey +  "主密钥的校验码：" + masterKeyValbuf);
			//以后用：60641374722960456737953136489547
			masterKeyMin = TripleDES.decrypt(Constants.transKey, masterKey);//主密钥明文
			int result = PayApplication.pedHI98.Ped_WriteMKey((byte)1,parseHexStr2Byte(masterKeyMin),16);
			if(result == PayApplication.pedHI98.PED_OK){
				msg.what=SUCCESS_SHENGBEN;
				data.putString(Constants.TOAST, "初始化成功");
				msg.setData(data);
				handlerHI98.sendMessage(msg);
			}else{
				msg.what=FAILS_SHENGBEN;
				data.putString(Constants.TOAST, "主密钥错误");
				msg.setData(data);
				handlerHI98.sendMessage(msg);
			}
		}else{
			msg.what=FAILS_SHENGBEN;
			data.putString(Constants.TOAST, "服务器下发密钥长度有误");
			msg.setData(data);
			handlerHI98.sendMessage(msg);
		}*/
	}
	
	//接口的回调
	public interface LoadMasterListenearHI98{
		public void getMasterHI98State(boolean flag, String toastInfo);
	}
	public LoadMasterListenearHI98 loadMasterListenearHI98;
	public void setMasterHI98CallBack(LoadMasterListenearHI98 loadMasterListenear){
		this.loadMasterListenearHI98=loadMasterListenear;
	}
	
	private Handler handlerHI98 = new Handler() {
		@Override
		public void handleMessage(Message msg) {
/*			data = msg.getData();
			switch (msg.what) {
			case SUCCESS_SHENGBEN:
				loadMasterListenearHI98.getMasterHI98State(true,data.getString(Constants.TOAST));   //导入密钥成功
				break;
			case FAILS_SHENGBEN:
				loadMasterListenearHI98.getMasterHI98State(false,data.getString(Constants.TOAST));  //导入密钥失败
				break;
			default:
				break;
			}
			super.handleMessage(msg);*/
		}
	};
	
	// 延迟操作
	private Handler handlerDelay = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DELAY_SHENGBEN:
				saveMasterHI98Key(transResponse);
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};
	private Thread loadCleanData;
}
