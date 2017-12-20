package com.idean.s600.pay.manage;

import android.os.Build;


public class IntelPosPowerManager {
	
	static {
		if (Build.MODEL.contains("F300") || Build.MODEL.contains("G100")) {
			System.loadLibrary("poscontrol");
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
