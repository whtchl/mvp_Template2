package com.idean.s600.pay.manage;

import android.os.Build;


/**
 * Describe : 控制睿矽微芯片上电和下电JNI调用管理类<br/>
 * Date : 2015年9月23日上午11:30:57 <br/>
 * Version : 1.0 <br/>
 * 
 * @author XiongWei
 */
public final class FinanicalPowerManager {

	static {
		if (Build.MODEL.contains("F300") || Build.MODEL.contains("G100")) {
			System.loadLibrary("posInterface_jni");
		}
	}

	/**
	 * 控制金融芯片上电
	 */
	public native static void posPowerOn();

	/**
	 * 控制金融芯片下电
	 */
	public native static void posPowerOff();

}
