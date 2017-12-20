package com.lifeng.f300.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.text.TextUtils;


/**
 * Describe : 时间工具类<br/>
 * Date : 2015年9月5日上午10:55:23 <br/>
 * Version : 1.0 <br/>
 * 
 * @author wangkeze
 */
@SuppressLint("SimpleDateFormat")
public class DateUtil {
	public static String formatterDate(String strDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate, pos);
		return formatter.format(strtodate);
	}

	public static String formatterDate(String strDate, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate, pos);
		return formatter.format(strtodate);
	}

	/**
	 * @description:通过匹配符获取日期
	 * @param formatStr
	 *            匹配字符
	 * @return
	 */
	@SuppressLint("NewApi")
	public static Date getDateByDateFormat(String formatStr) {
		DateFormat df = new SimpleDateFormat(Constants.DATE_FORMAT1);
		if (formatStr == null || formatStr.isEmpty()) {
			return new Date();
		}
		try {
			Date date = df.parse(formatStr);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new Date();
	}

	public static String formatterDate(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		return formatter.format(date);
	}
	/**
	 * @description:获取一个相对当前的日期对象
	 * @param spaceDay
	 *            相对时间间隔 如明天为1 后天为2 昨天为-1
	 * @return
	 */
	public static Date getDate(int spaceDay) {
		Calendar calendar = Calendar.getInstance();// 当前时间
		int day = calendar.get(Calendar.DATE);
		calendar.set(Calendar.DATE, day + spaceDay);
		return calendar.getTime();
	}

	/**
	 * @description:格式化日期
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String formatterDate(Date date, String pattern) {
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		return formatter.format(date);
	}

	/**
	 * 日期转为年月日
	 * 
	 * @param date
	 * @return
	 */
	public static String formatterDate4(String date) {
		SimpleDateFormat formatter1 = new SimpleDateFormat(
				Constants.DATE_FORMAT1);
		SimpleDateFormat formatter = new SimpleDateFormat(
				Constants.DATE_FORMAT4);
		Date dateNew;
		String str = "";
		try {
			if (!TextUtils.isEmpty(date)) {
				dateNew = formatter1.parse(date);
				Date addDateOneDay = addDateOneDay(dateNew);
				str = formatter.format(addDateOneDay);
			} else {
				str = "";
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return str;
	}

	/**
	 * @description:格式化日期
	 * @param time
	 * @return
	 */
	public static String formatterDate(long time) {
		SimpleDateFormat formatter = new SimpleDateFormat(
				Constants.DATE_FORMAT_MS);
		return formatter.format(new Date(time));
	}

	/**
	 * yyyy-MM-dd HH:mm:ss
	 * @description:格式化日期
	 * @param time
	 * @return
	 */
	public static String formatterDate1(long time) {
		SimpleDateFormat formatter = new SimpleDateFormat(
				Constants.DATE_FORMAT1);
		return formatter.format(new Date(time));
	}
	
	/**
	 * yyyyMMddHHmmss
	 * @param time
	 * @return
	 */
	public static String formatterDate5(long time) {
		SimpleDateFormat formatter = new SimpleDateFormat(
				Constants.DATE_FORMAT5);
		return formatter.format(new Date(time));
	}

	/***
	 * 
	 * @param time
	 * @param formater
	 * @return
	 */
	public static String timeFormatFromLong(long time, String formater) {
		SimpleDateFormat format = new SimpleDateFormat(formater);
		Date d1 = new Date(time);
		String t1 = format.format(d1);
		return t1;
	}

	/**
	 * 和系统当前的时间比较大小
	 * 
	 * @param date
	 * @return 返回为-1，说明已经过期了
	 */
	public static int compareDate(String date) {
		if (TextUtils.isEmpty(date)) {
			return 1;
		}

		DateFormat df = new SimpleDateFormat(Constants.DATE_FORMAT1);
		int num = 0;
		try {
			Date dt1 = df.parse(DateUtil.formatterDate(new Date())); // 当前系统的时间
			Date dt2 = df.parse(date);
			if (dt1.getTime() < dt2.getTime()) {
				num = 1;
			} else {
				num = -1;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return num;
	}

	/**
	 * 将日期信息转换成今天、明天、后天、星期
	 * 
	 * @param date
	 * @return
	 */
	public static String getDateDetail(String date) {
		if(TextUtils.isEmpty(date)){
			return "";
		}
		String formmat = "yyyy-MM-dd hh:mm:ss";
		int xcts = -9;
		Calendar today = Calendar.getInstance();
		Calendar target = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat(formmat);
		try {
			today.setTime(df.parse(DateUtil.formatterDate(new Date(),
					formmat)));
			today.set(Calendar.HOUR, 0);
			today.set(Calendar.MINUTE, 0);
			today.set(Calendar.SECOND, 0);
			target.setTime(df.parse(date));
			target.set(Calendar.HOUR, 0);
			target.set(Calendar.MINUTE, 0);
			target.set(Calendar.SECOND, 0);
			long intervalMilli = target.getTimeInMillis()
					- today.getTimeInMillis();
//			LogUtils.d("jsh","target:"+timeFormatFromLong(target.getTimeInMillis(),formmat));
//			LogUtils.d("jsh","today:"+timeFormatFromLong(today.getTimeInMillis(),formmat));
			double dxcts = CharacterOperationUtils.getRmbDivide(intervalMilli
					+ "", (24 * 60 * 60 * 1000) + "");
			if (dxcts < 0 && dxcts >= -1) {
				xcts = -1;
			} else if (dxcts == 0||dxcts == 0.5) {
				xcts = 0;
			}

		} catch (ParseException e) {

		}
		
		return showDateDetail(xcts, target);
	}

	/**
	 * 将日期差显示为日期或者星期
	 * 
	 * @param xcts
	 * @param target
	 * @return
	 */
	private static String showDateDetail(int xcts, Calendar target) {
		switch (xcts) {
		case 0:
			return Constants.TODAY;
			// case 1:
			// return Constants.TOMORROW;
			// case 2:
			// return Constants.AFTER_TOMORROW;
		case -1:
			return Constants.YESTERDAY;
			// case -2:
			// return Constants.BEFORE_YESTERDAY;
		default:
			int dayForWeek = 0;
			dayForWeek = target.get(Calendar.DAY_OF_WEEK);
			switch (dayForWeek) {
			case 1:
				return Constants.SUNDAY;
			case 2:
				return Constants.MONDAY;
			case 3:
				return Constants.TUESDAY;
			case 4:
				return Constants.WEDNESDAY;
			case 5:
				return Constants.THURSDAY;
			case 6:
				return Constants.FRIDAY;
			case 7:
				return Constants.SATURDAY;
			default:
				return null;
			}
		}
	}

	/**
	 * 添加一天的结果
	 * @param date
	 * @return
	 */
	public static Date addDateOneDay(Date date) {
		if (null == date) {
			return date;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date); // 设置当前日期
//		c.add(Calendar.DATE, 1); // 日期加1天
		c.add(Calendar.DATE, 0); // 就是当天的日期
//		c.add(Calendar.DATE, -1); // 日期减1天
		date = c.getTime();
		return date;
	}

	/**
	 * 截取为标准的时间
	 * @param errorTime
	 * @return
	 */
	public static String spliteDataAppend(String errorTime){
		if (!TextUtils.isEmpty(errorTime) && errorTime.length()==14) {
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer=stringBuffer.append(errorTime.substring(0, 4)+"-");
			stringBuffer=stringBuffer.append(errorTime.substring(4, 6)+"-");
			stringBuffer=stringBuffer.append(errorTime.substring(6, 8)+" ");
			stringBuffer=stringBuffer.append(errorTime.substring(8, 10)+":");
			stringBuffer=stringBuffer.append(errorTime.substring(10, 12)+":");
			stringBuffer=stringBuffer.append(errorTime.substring(12, 14));
			return stringBuffer.toString();
		}
		return "";
	}
	public static int getDateDays (String date1, String date2){ 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd"); 
		long betweenTime = 0;
		try { 
			Date date = sdf.parse(date1);// 通过日期格式的parse()方法将字符串转换成日期 
			Date  dateBegin = sdf.parse(date2);
			betweenTime = date.getTime() - dateBegin.getTime(); 
		    betweenTime = betweenTime / 1000 / 60 / 60 / 24; 
		} catch(Exception e){
		}
		return (int)betweenTime; 
	}
	public static long getlongTime (String date1, String format){ 
		SimpleDateFormat sdf = new SimpleDateFormat(format); 
		long betweenTime = 0;
		try { 
			Date date = sdf.parse(date1);
			return  date.getTime();
		} catch(Exception e){
		}
		return (int)betweenTime; 
	}
}
