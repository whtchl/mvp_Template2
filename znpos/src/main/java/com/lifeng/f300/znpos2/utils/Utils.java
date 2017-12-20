/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lifeng.f300.znpos2.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.lifeng.f300.common.utils.ISaftyDialog;
import com.lifeng.f300.common.utils.LogUtils;
import com.lifeng.f300.common.utils.OnCusDialogInterface;
import com.lifeng.f300.common.utils.StringUtil;
import com.lifeng.f300.znpos2.R;


/**
 * Class containing some static utility methods.
 */
public class Utils {
	public static final int IO_BUFFER_SIZE = 8 * 1024;

	public static int NUM_LENGTH = 7;// 金额长度

	private Utils() {

	};

	/**
	 * 设置输入金额
	 * 
	 * @param textView
	 */
	public static void setNumber(TextView textView, TextView tvPayMoney,
			StringBuilder strBuilder) {
		boolean isAppend = false;
		String money = textView.getText().toString();
		String payMonye = tvPayMoney.getText().toString();
		if (StringUtil.isNotEmpty(payMonye)) {
			String subMoney = "";
			if (payMonye.contains(".")) {
				int index = payMonye.indexOf(".");
				index = payMonye.indexOf(".");
				if (payMonye.substring(index + 1, payMonye.length()).length() < 2) {
					isAppend = true;
				}
				subMoney = payMonye.substring(0, index);
			} else {
				isAppend = true;
				subMoney = payMonye;
			}
/*			if(BuildModle.isF300OrS600()){
				NUM_LENGTH = 6;
			}else{*/
				NUM_LENGTH = 7;
			//}
			if (subMoney.length() < NUM_LENGTH || isAppend) {
				strBuilder.append(money);
				tvPayMoney.setText(strBuilder.toString());
			}
		}
	}

	public static String calculator(String strValue, TextView tvPayMoney,
			String number) {
		boolean isAppend = false;
		if (StringUtil.isNotEmpty(strValue)) {
			String subStrValue = "";
			if (strValue.contains(".")) {
				int index = strValue.indexOf(".");
				subStrValue = strValue.substring(0, index);
				if (strValue.substring(index + 1, strValue.length()).length() < 2) {
					isAppend = true;
				} else {
					tvPayMoney
							.setText(strValue.contains(".") ? getSpannableString(strValue)
									: strValue);
					return strValue;
				}
			} else {
				subStrValue = strValue;
			}
			/*if(BuildModle.isF300OrS600()){
				NUM_LENGTH = 6;
			}else{*/
				NUM_LENGTH = 7;
			//}
			if (subStrValue.length() < NUM_LENGTH || isAppend) {
			} else {
				tvPayMoney
						.setText(strValue.contains(".") ? getSpannableString(strValue)
								: strValue);
				return strValue;
			}
		}
		if ("0".equals(strValue)) {
			if("00".equals(number)){
				strValue = "0";
			}else{
				strValue = number;
			}
		}else{
			if ("0.0".equals(strValue) && ("0".equals(number) || "00".equals(number))) {
				return strValue;
			}
			strValue += number;
		}
		tvPayMoney
				.setText(strValue.contains(".") ? getSpannableString(strValue)
						: strValue);
		return strValue;
	}

	/***
	 * 设置小数点后字体
	 * 
	 * @param strValue
	 * @return
	 */
	public static SpannableString getSpannableString(String strValue) {
		SpannableString spanString = new SpannableString(strValue);
		AbsoluteSizeSpan span = new AbsoluteSizeSpan(66);
		spanString.setSpan(span, strValue.indexOf(".") + 1, strValue.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return spanString;
	}

	/***
	 * 添加小数点
	 * 
	 * @param string
	 * @return
	 */
	public static String addPoint(String string) {
		int p = string.length() - 1;
		boolean flag = true;
		Character tmp = string.charAt(p);
		if (0 == p)
			string += ".";
		if (Character.isDigit(tmp) && 0 != p) {
			while (flag) {
				if (!Character.isDigit(tmp)) {
					flag = false;
					if (tmp != '.')
						string += ".";
				}
				if (0 == --p && (tmp != '.')) {
					string += ".";
					break;
				}
				tmp = string.charAt(p);
			}
		}
		return string;
	}

	/***
	 * 添加小数点
	 * 
	 * @param  strNumber
	 * @return
	 */
	public static String deleteNumber(String strNumber) {
		if (!"0".equals(strNumber)) {
			strNumber = strNumber.substring(0, strNumber.length() - 1);
			if (0 == strNumber.length())
				strNumber = "0";
		}
		return strNumber;
	}

	public static String subtract(String fristMoney, String lastMoney) {
		DecimalFormat df = new DecimalFormat("#0.00");
		if (StringUtil.isNotEmpty(fristMoney)
				&& StringUtil.isNotEmpty(lastMoney)) {
			BigDecimal frist = new BigDecimal(fristMoney);
			BigDecimal last = new BigDecimal(lastMoney);
			return df.format(frist.subtract(last)).toString();
		}
		return "0.00";
	}

	public static Dialog setDialog(Activity activity, int layout, double width,
			double height) {
		View view = LayoutInflater.from(activity).inflate(layout, null);
		Dialog dialog = new Dialog(activity, R.style.CustomProgressDialog);
		dialog.setCancelable(false);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(view);
		
		WindowManager m = activity.getWindowManager();
		Window dialogWindow = dialog.getWindow();
		Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
		Display display = m.getDefaultDisplay();
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = (int) (display.getWidth()); // 设置宽度
		WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
		if (width > 0) {
			p.width = (int) (d.getWidth() * width); // 宽度设置为屏幕的0.65
		}
		if (height > 0) {
			p.height = (int) (d.getHeight() * height); // 宽度设置为屏幕的0.65
		}
		dialogWindow.setAttributes(p);
		return dialog;
	}

	public static void setDialogWindows(Activity activity, Dialog dialog,
			double width, double height) {

		WindowManager m = activity.getWindowManager();
		Window dialogWindow = dialog.getWindow();
		Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
		WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
		if (width > 0) {
			p.width = (int) (d.getWidth() * width); // 宽度设置为屏幕的0.65
		}
		if (height > 0) {
			p.height = (int) (d.getHeight() * height); // 宽度设置为屏幕的0.65
		}
		dialogWindow.setAttributes(p);
	}

	private static Integer _screenWidth;
	private static Integer _screenHeight;

	/**
	 * @Description: 得到设备高度
	 * @param context
	 * @return
	 * @return float
	 */
	public static int getScreenHeight(Activity context) {
		if (_screenHeight == null) {
			DisplayMetrics metric = new DisplayMetrics();
			context.getWindowManager().getDefaultDisplay().getMetrics(metric);
			_screenHeight = metric.heightPixels - getStatusBarHeight(context);
		}
		return _screenHeight;
	}

	/**
	 * @Description: 获取Status Bar高度
	 * @param context
	 * @return
	 * @return int
	 */
	public static int getStatusBarHeight(Context context) {
		int result = 0;
		int resourceId = context.getResources().getIdentifier(
				"status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = context.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	/**
	 * 设置光标位置
	 * 
	 * @param et
	 */
	public static void setEditTextFocus(EditText et) {
		et.setFocusable(true);
		et.setFocusableInTouchMode(true);
		et.requestFocus();
		et.findFocus();
		et.setSelection(et.getText().toString().trim().length());
	}

	/**
	 * @Description: TODO
	 * @param context
	 * @param title
	 * @param msg
	 * @param dialogInterface
	 * @return void
	 */
	public static void showDialog(final Context context, String title,
			String msg, String confirm, String cancel,
			final OnCusDialogInterface dialogInterface) {
		try {
			if (context != null && context instanceof ISaftyDialog) {
				ISaftyDialog activity = (ISaftyDialog) context;
				if (activity.verifyDialogFlag()) {
					return;
				}
			}
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			if (title != null) {
				builder.setTitle(title);
			}
			builder.setCancelable(false);
			builder.setMessage(msg);
			if (confirm != null) {
				builder.setPositiveButton(confirm,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if (context != null
										&& context instanceof ISaftyDialog) {
									ISaftyDialog activity = (ISaftyDialog) context;
									activity.hideDialogFlag();
								}
								LogUtils.d("wang", "自定义的dialog：" + dialog);
								if (null != dialog) {
									dialog.dismiss();
									dialog = null;
								}
								if (dialogInterface != null) {
									dialogInterface.onConfirmClick();
								}
							}
						});
			}
			if (cancel != null) {
				builder.setNegativeButton(cancel,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if (context != null
										&& context instanceof ISaftyDialog) {
									ISaftyDialog activity = (ISaftyDialog) context;
									activity.hideDialogFlag();
								}
								LogUtils.d("wang", "自定义的dialog：" + dialog);
								if (null != dialog) {
									dialog.dismiss();
									dialog = null;
								}
								if (dialogInterface != null) {
									dialogInterface.onCancelClick();
								}
							}
						});
			}
			builder.create().show();
			if (context != null && context instanceof ISaftyDialog) {
				ISaftyDialog activity = (ISaftyDialog) context;
				activity.showDialogFlag();
			}
		} catch (Exception e) {
		}
	}

}
