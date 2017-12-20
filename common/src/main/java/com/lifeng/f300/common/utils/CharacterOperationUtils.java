package com.lifeng.f300.common.utils;

import java.math.BigDecimal;

/**
 * 常用字符串的计算封装类
 * @author wangkeze
 *
 */
public class CharacterOperationUtils {
//	  ROUND_CEILING     
//	  如果   BigDecimal   是正的，则做   ROUND_UP   操作；如果为负，则做   ROUND_DOWN   操作。     
//	  ROUND_DOWN     
//	  从不在舍弃(即截断)的小数之前增加数字。     
//	  ROUND_FLOOR     
//	  如果   BigDecimal   为正，则作   ROUND_UP   ；如果为负，则作   ROUND_DOWN   。     
//	  ROUND_HALF_DOWN     
//	  若舍弃部分>   .5，则作   ROUND_UP；否则，作   ROUND_DOWN   。     
//	  ROUND_HALF_EVEN     
//	  如果舍弃部分左边的数字为奇数，则作   ROUND_HALF_UP   ；如果它为偶数，则作   ROUND_HALF_DOWN   。     
//	  ROUND_HALF_UP     
//	  若舍弃部分>=.5，则作   ROUND_UP   ；否则，作   ROUND_DOWN   。     
//	  ROUND_UNNECESSARY     
//	  该“伪舍入模式”实际是指明所要求的操作必须是精确的，，因此不需要舍入操作。     
//	  ROUND_UP     
//	  总是在非   0   舍弃小数(即截断)之前增加数字。  
	/**
	 * 减法
	 * @param num1
	 * @param num2
	 * @return
	 */
	public static double getSubtract(String num1, String num2) {
		if (StringUtil.isNotEmpty(num1) && StringUtil.isDigit(num2)) {
			BigDecimal bignum1 = new BigDecimal(num1);
			BigDecimal bignum2 = new BigDecimal(num2);
			return bignum1.subtract(bignum2).doubleValue();
		}
		return 0;
	}

	/**
	 * 乘法
	 * @param rmb
	 * @param pointRate
	 * @return
	 */
	public static double getRmbToPoint(String rmb, String pointRate) {
		if (StringUtil.isNotEmpty(rmb) && StringUtil.isDigit(rmb)) {
			BigDecimal bignRmb = new BigDecimal(rmb);
			BigDecimal bignPointRate = new BigDecimal(pointRate);
			return bignRmb.multiply(bignPointRate).doubleValue();
		}
		return 0;
	}

	/**
	 * 字符串转化为数字int
	 * @param rmb
	 * @return
	 */
	public static int geIntNumber(String rmb) {
		if (StringUtil.isNotEmpty(rmb) && StringUtil.isDigit(rmb)) {
			BigDecimal bignRmb = new BigDecimal(rmb);
			return bignRmb.intValue();
		}
		return 0;
	}

	/**
	 * 字符串转化为数字double
	 * @param rmb
	 * @return
	 */
	public static double getDoubleumber(String rmb) {
		if (StringUtil.isNotEmpty(rmb) && StringUtil.isDigit(rmb)) {
			BigDecimal bignRmb = new BigDecimal(rmb);
			return bignRmb.doubleValue();
		}
		return 0;
	}

	/**
	 * 字符串除法
	 * @param rmb
	 * @param pointRate
	 * @return
	 */
	public static double getRmbDivide(String rmb, String pointRate) {
		if (StringUtil.isNotEmpty(rmb) && StringUtil.isDigit(rmb)
				&& StringUtil.isNotEmpty(pointRate)
				&& StringUtil.isDigit(pointRate) && !"0".equals(pointRate)) {
			BigDecimal bignRmb = new BigDecimal(rmb);
			BigDecimal bignPointRate = new BigDecimal(pointRate);
			return bignRmb.divide(bignPointRate, 2, BigDecimal.ROUND_DOWN).doubleValue();
		}
		return 0;
	}

	/**
	 * 乘法保留2位小数点
	 * @param rmb
	 * @param pointRate
	 * @return
	 */
	public static double getRmbMultiply(String rmb, String pointRate) {
		if (StringUtil.isNotEmpty(rmb) && StringUtil.isDigit(rmb)
				&& StringUtil.isNotEmpty(pointRate)
				&& StringUtil.isDigit(pointRate) && !"0".equals(pointRate)) {
			BigDecimal bignRmb = new BigDecimal(rmb);
			BigDecimal bignPointRate = new BigDecimal(pointRate);
			return round(bignRmb.multiply(bignPointRate).doubleValue(),2);
		}
		return 0;
	}

	/**
	 * 多个数字相加
	 * @param rmb
	 * @return
	 */
	public static double getRmbAdd(String... rmb) {
		BigDecimal bignRmb = new BigDecimal("0");
		if (rmb != null) {
			for (String str : rmb) {
				if (StringUtil.isNotEmpty(str) && StringUtil.isDigit(str)) {
					BigDecimal bignPointRate = new BigDecimal(str);
					bignRmb = bignRmb.add(bignPointRate);
				}

			}
		}
		return bignRmb.doubleValue();
	}

	/**
	 * 获取绝对值
	 * @param str
	 * @return
	 */
	public static double getAbs(String str) {
		BigDecimal bignRmb = new BigDecimal(str);
		return bignRmb.abs().doubleValue();
	}

	/**
	 * 提供精确的小数位四舍五入处理
	 * @param v
	 *            需要四舍五入的数字
	 * @param scale
	 *            小数点后保留几位
	 * @return 四舍五入后的结果
	 */
	public static double round(double v, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

}
