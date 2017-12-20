package com.lifeng.f300.common.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * Describe : 字符串工具处理类<br/>
 * Date : 2015年9月5日上午11:10:55 <br/>
 * Version : 1.0 <br/>
 * 
 * @author XiongWei
 */
/**
 * Decripe : Date : 2015年9月14日 Version : 1.0 <br/>
 * 
 * @author : wangkeze
 */
public class StringUtil {

	//电话号码的匹配
	private final static String[] PHONENUMBER_PREFIX = { "130", "131", "132", "145", "155", "156", "185", "186", "134", "135", "136", "137", "138",
        "139", "147", "150", "151", "152", "157", "158", "159", "182", "183", "187", "188", "133", "153", "189", "180" };
	
	/**
	 * 格式化卡号(每隔4位数字中间添加空格)
	 * 
	 * @param cardNumber
	 * @return
	 */
	public static String formatCardNumber(String cardNumber) {
		if(TextUtils.isEmpty(cardNumber)){
			return "";
		}
		cardNumber = cardNumber.replace(" ", "");
		StringBuffer buffer = new StringBuffer();
		char[] chars = cardNumber.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			buffer.append(chars[i]);
			if ((i + 1) % 4 == 0)
				buffer.append(" ");
		}

		return buffer.toString();
	}

	/**
	 * 格式化卡号(中间替换为*号，两头为数字)
	 * 
	 * @param cardNumber
	 * @return
	 */

	public static String formatCardNumbers(String cardNumber) {
		if(TextUtils.isEmpty(cardNumber)){
			return "";
		}
		cardNumber = cardNumber.replace(" ", "");
		StringBuffer buffer = new StringBuffer();
		char[] chars = cardNumber.toCharArray();
		for (int i = 0; i < chars.length; i++) {

			if (i == 3 || i == chars.length - 4) {
				buffer.append(chars[i] + " ");
			} else if (i > 5 && i < chars.length - 4) {
				buffer.append("*");
			} else {
				buffer.append(chars[i]);
			}
		}

		return buffer.toString();
	}

	/**
	 * 解析数据,包含错误信息
	 * 
	 * @param outData
	 * @return
	 */
	public static String analysisOutData(byte[] outData) {
		String temp = new String(outData);
		String[] arrays = temp.split("\\|");
		if (1 == arrays.length)
			return arrays[0];
		else if (2 == arrays.length)
			return arrays[1] + "(" + arrays[0] + ")";

		return "";
	}

	/**
	 * 解析卡号
	 * 
	 * @param data
	 * @return
	 */
	public static String analysisBankCardNumber(byte[] data) {
		return analysisBankCardNumber(new String(data));
	}

	public static String analysisBankCardNumber(byte[] data, int length) {
		return analysisBankCardNumber(new String(data, 0, length));
	}

	/**
	 * 解析卡号
	 * 
	 * @param outData
	 * @return
	 */
	public static String analysisBankCardNumber(String outData) {
		String[] arrays = outData.split("\\|");
		if (arrays.length > 0)
			return arrays[0];
		return null;
	}

	/**
	 * 格式化金额处理,格式:200.00
	 * 
	 * @param amount
	 * @return
	 */
	public static String formatterAmount(String amount) {
		if (TextUtils.isEmpty(amount)) {
			return "0.00";
		}
		BigDecimal decimal = new BigDecimal(amount);
		DecimalFormat formatter = new DecimalFormat("0.00");
		if (amount != null) {
			return formatter.format(decimal.abs().doubleValue());
		}
		return "0.00";
	}

	/**
	 * 格式化金额处理,格式:200
	 * 
	 * @param amount
	 * @return
	 */
	public static String formatterAmount1(String amount) {
		DecimalFormat formatter = new DecimalFormat("0");
		return formatter.format(Double.valueOf(amount));
	}

	/**
	 * 格式化金额,在实际金额后补2位0.如实际输入200.5,转换后返回200500
	 * 
	 * @param amount
	 * @return
	 */
	public static String formatterAmount2(String amount) {
		if (TextUtils.isEmpty(amount))
			return "0";
		amount = formatterAmount(amount);
		return amount.replace(".", "");
	}

	/**
	 * 格式化金额处理,将000040000转换为400.00
	 * 
	 * @param amount
	 * @return
	 */
	public static String formatterAmount3(String amount) {
		String result = Long.valueOf(amount).toString();
		int length = result.length();
		if (length < 2) {
			return "0.00";
		}

		result = result.substring(0, length - 2) + "."
				+ result.substring(length - 2, length);
		return result;
	}

	public static boolean isNotEmpty(CharSequence cs) {
		return !isEmpty(cs);
	}

	public static boolean isEmpty(CharSequence cs) {
		return cs == null || cs.length() == 0;
	}

	/**
	 * 判断是否为空，不为空直接返回，为空返回暂无数据
	 * 
	 * @param str
	 * @return
	 */
	public static String isEmptyString(String str) {
		if (str != null && str.length() != 0) {
			return str;
		}
		return "";
	}

	/** 产生4位随机验证码 wangkeze */
	public static String getRandom() {
		double a = Math.random() * 9000 + 1000;
		a = Math.ceil(a);
		int randomNum = new Double(a).intValue();
		return randomNum + "";
	}

	/** 判断是不是正确的电话号码格式，正确为true，反之为false wangkeze */
	public static boolean phoneIsRight(String userPhone) {
		String regex = "^(1[0-9][0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(userPhone);
		return m.matches();
	}

	/**
	 * 获取手机IMEI唯一标识
	 * 
	 * @param mContext
	 * @return
	 */
	public static String getImei(Context mContext) {
		TelephonyManager telephonyManager = (TelephonyManager) mContext
				.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getDeviceId();
	}

	/**
	 * 计算两个日期之间相差的天数 wangkeze
	 * 
	 * @param smdate
	 *            较小的时间
	 * @param bdate
	 *            较大的时间
	 * @return 相差天数
	 * @throws ParseException
	 */
	public static int daysBetween(Date smdate, Date bdate)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		smdate = sdf.parse(sdf.format(smdate));
		bdate = sdf.parse(sdf.format(bdate));
		Calendar cal = Calendar.getInstance();
		cal.setTime(smdate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(bdate);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}

	public static String getOutSerialNumber() {
		SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmmss",
				Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		key = key.replace("-", "");
		return key;
	}

	/**
	 * 判断字符串是否是整数
	 */
	public static boolean isInteger(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * 判断字符串是否是浮点数
	 */
	public static boolean isDouble(String value) {
		try {
			Double.parseDouble(value);
			if (value.contains("."))
				return true;
			return false;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static boolean isValidMathNumber(String val) {
		Pattern pattern = Pattern.compile("((\\d+)?(\\.\\d+)?)");
		Matcher matcher = pattern.matcher(val);
		return matcher.matches();
	}

	/**
	 * 判断字符串是否是数字
	 */
	public static boolean isDigit(String value) {
		return isInteger(value) || isDouble(value);
	}

	/**
	 * 获取16位十六进制字符串格式的随机码
	 * 
	 * @return
	 */
	public static String randomHex() {
		StringBuffer sb = new StringBuffer();
		String[] array = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
				"A", "B", "C", "D", "E", "F" };
		Random random = new Random();
		for (int i = 0; i < array.length; i++) {
			sb.append(array[random.nextInt(16)]);
		}

		return sb.toString();
	}

	/**
	 * 判断是否是手机号码
	 * 
	 * @param mobile
	 * @return
	 */
	public static boolean isMobileNo(String mobile) {
		if (mobile != null && mobile.length() == 11) {
//			if (mobile.startsWith("13") || mobile.startsWith("15")
//					|| mobile.startsWith("18") || mobile.startsWith("147")
//					|| mobile.startsWith("170")) {
//				return true;
//			}
			return true;
		}
		return false;
	}

	/**
     * 匹配手机号码 
     * <p>
     * 新联通</br>
     *   （中国联通+中国网通）手机号码开头数字 130,131,132,145,155,156,185,186</br> 
     * 新移动</br>
     * （中国移动+中国铁通）手机号码开头数字</br>
     * 134,135,136,137,138,139,147,150,151,152,157,158,159,182,183,187,188</br> 
     * 新电信</br>
     * （中国电信+中国卫通）手机号码开头数字 133,153,189,180</br>
     * </p>
     * @param 手机号码
     * @return  参数为null和不合法时返回false，否则返回true
     */
    public static boolean patternPhoneNumber(String number) {
        int len = PHONENUMBER_PREFIX.length;
        if (number != null) {
            for (int i = 0; i < len; i++) {
                Pattern p = Pattern.compile(PHONENUMBER_PREFIX[i] + "\\d{8}");
                if (p.matcher(number).matches()) {
                    return true;
                }
            }
        }
        return false;
    }

	
	/**
	 * 格式化会员卡号
	 * 
	 * @param cardNumber
	 * @return
	 */
	public static String getFormatNumber(String cardNumber) {
		String formatCardNumber = "";
		if (!TextUtils.isEmpty(cardNumber) && cardNumber.length() >= 16) {
			formatCardNumber = cardNumber.substring(0, 6) + "***"
					+ cardNumber.substring(15);
		} else {
			formatCardNumber = "";
		}
		return formatCardNumber;
	}

	/**
	 * 包含前缀就直接截取掉，返回真实卡号
	 * @param cardNumber
	 * @param cardPrefix
	 * @return
	 */
	public static String getRealCardNumber(String cardNumber,String cardPrefix) {
		String formatCardNumber = "";
		if(!TextUtils.isEmpty(cardNumber)) {
			if(!TextUtils.isEmpty(cardPrefix)){
				if(cardNumber.startsWith(cardPrefix)){
					formatCardNumber = cardNumber.substring(cardPrefix.length());
				}else{
					formatCardNumber = cardNumber;
				}
			}else{
				formatCardNumber = cardNumber;
			}
		}else{
			formatCardNumber = "";
		}
		return formatCardNumber;
	}
	
	/**
	 * 格式化身份证号码
	 * 
	 * @param cardNumber
	 * @return
	 */
	public static String formatNumberIdCard(String cardNumber) {
		String idCardNumber = "";
		if (!TextUtils.isEmpty(cardNumber) && cardNumber.length() >= 15) {
			if (cardNumber.length() == 15) {
				idCardNumber = cardNumber.substring(0, 6) + "****"+ cardNumber.substring(11);
			} else {
				idCardNumber = cardNumber.substring(0, 6) + "********"+ cardNumber.substring(14);
			}
		} else {
			idCardNumber = cardNumber;//小于15位就直接显示
		}
		return idCardNumber;
	}

	/**
	 * 截取最后几位字符串
	 * @param str
	 * @param number
	 * @return
	 */
	public static String getLastStr(String str, int number) {
		if (str.length() >= number) {
			return str.substring(str.length() - number, str.length());
		} else {
			return "";
		}
	}
	
    /**
     * 验证实名身份证
     * @param id
     * @return
     */
    public static boolean checkRealNameID(String id){
    	//身份证不能为空，且必须
    	if(id == null || !id.matches("^[0-9]{17}[0-9Xx]$")){
    		return false;
    	}
    	String birthday = id.substring(6, 14);
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    	Date date = null;
    	try{
    		date = sdf.parse(birthday);
    	}catch(Exception e){
    		return false;
    	}
    	Date curr = new Date();
    	if(date.getTime() >= curr.getTime()){
    		return false;
    	}
    	long l = 120L*366*24*60*60*1000;
    	if((curr.getTime() - date.getTime()) > l){
    		return false;
    	}
    	
    	int[] p = new int[]{7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2,1};
    	String[] c = {"1","0","X","9","8","7","6","5","4","3","2"};
    	int t = 0;
    	for(int i=0; i<17; i++){
    		int n = id.charAt(i)-48;
    		t += n*p[i];
    	}
    	t = t%11;
    	String check = c[t];
    	if(!id.toUpperCase().endsWith(check)){
    		return false;
    	}
    	
//    	name
//    	if(name == null || name.length() < 2 || name.length() > 10){
//    		return false;
//    	}
//		必须是汉字（少数民族可以有点）
//		if(!name.matches("^[\u4e00-\u9fa5]{2,20}([·][\u4e00-\u9fa5]{2,20})*$")){
//			return false;
//		}
    	return true;
    }
    
    /**
     * 从身份证中获取生日信息
     * @param birthday
     * @return
     */
    public static Date getIdCardBirthday(String idCard){
    	String birthday = idCard.substring(6, 14);
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    	Date date = null;
    	try{
    		date = sdf.parse(birthday);
    	}catch(Exception e){
    		return null;
    	}
    	return date;
    }
    
    /**
     * 是否是男性
     * @param idCard
     * @return
     */
    public static boolean checkSex(String idCard){
    	if (idCard.length() == 18) {
    		String garder = idCard.substring(16, 17);
    		if (isInteger(garder)) {
    			LogUtils.d("wang", Integer.parseInt(garder)+"性别");
				if (Integer.parseInt(garder) % 2 == 0) {
					return false;//偶数女性
				}else{
					return true; //奇数男性
				}
			}else{
				return false;
			}
		}
    	return false;
    }
    
    /**
     * 获取json中对应的字段的内容
     * @param json json对象
     * @param jsonFiled json中的字段
     * @return
     */
    public static String getJsonInfo(JSONObject json,String jsonFiled){
    	if(!json.isNull(jsonFiled)){
    		try {
				return TextUtils.isEmpty(json.getString(jsonFiled)) ? "" : json.getString(jsonFiled);
			} catch (JSONException e) {
				return "";
			}
    	}
    	return "";
    }
    
    /**
     * 自动补全为6位
     * @param number
     * @return
     */
    public static String toSixNumberStr(String number){
    	String var = "";
    	for(int i = 0;i<6-number.length();i++){
    		var+="0";
    	}
    	return var+number;
    }
}
