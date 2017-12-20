package com.lifeng.f300.common.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import com.google.gson.JsonObject;
/**
 * 
 * @author think
 * 传输之间的加密方式
 */
public class DesUtils {

	private final static String DES = "DES";
	private final static String CIPHER_ALGORITHM = "DES";

	/**
	 * 加密String明文输入,String密文输出
	 * 
	 * @param strMing
	 *            String明文
	 * @return String密文
	 * @throws DesException
	 */
	public static String getEncString(String strMing, byte[] byteKey) {
		byte[] byteMi = null;
		byte[] byteMing = null;
		byte[] buf=null;
		
		if (byteKey!=null && byteKey.length!=0) {
			try {
				buf = strMing.getBytes("utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			int len = 8 - buf.length % 8;
			byteMing = new byte[buf.length + len];
			System.arraycopy(buf, 0, byteMing, 0, buf.length);
			byteMi = encryptDes(byteMing, byteKey);
			return Base64Helper.encode(byteMi);
		}
		return "";
	}
	

	/**
	 * 解密 以String密文输入,String明文输出
	 * 
	 * @param strMi
	 *            String密文
	 * @return String明文
	 */
	public static String getDesString(String strMi, byte[] byteKey) {
		byte[] bytebase64 = null;
		String strMing = null;
		try {
			bytebase64 = Base64Helper.decode(strMi);
			strMing = new String(decryptDes(bytebase64, byteKey));
		} catch (Exception e) {
		}
		return StringUtil.isNotEmpty(strMing)?strMing.trim():strMing;
	}

	/**
	 * 加密
	 * 
	 * @param src
	 *            数据源
	 * @param key
	 *            密钥，长度必须是8的倍数
	 * @return 返回加密后的数据
	 * @throws DesException
	 */
	public static byte[] encryptDes(byte[] src, byte[] key) {
		// DES算法要求有一个可信任的随机数源
		SecureRandom sr = new SecureRandom();
		try {
			// 从原始密匙数据创建DESKeySpec对象
			DESKeySpec dks = new DESKeySpec(key);
			// 创建一个密匙工厂，然后用它把DESKeySpec转换成
			// 一个SecretKey对象
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
			SecretKey securekey = keyFactory.generateSecret(dks);
			// Cipher对象实际完成加密操作,NoPadding为填充方式 默认为PKCS5Padding
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			// 用密匙初始化Cipher对象
			cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
			// 现在，获取数据并加密
			// 正式执行加密操作
			return cipher.doFinal(src);
		} catch (NoSuchAlgorithmException e) {
			// LOG.error("算数错误", e);
		} catch (InvalidKeyException e) {
			// LOG.error("无效key错误", e);
		} catch (InvalidKeySpecException e) {
			// LOG.error("无效key戳无", e);
		} catch (NoSuchPaddingException e) {
			// LOG.error("填充错误", e);
		} catch (IllegalBlockSizeException e) {
			// LOG.error("非法数据块", e);
		} catch (BadPaddingException e) {
			// LOG.error("错误的填充", e);
		}
		return null;
	}

	/**
	 * 生成密钥
	 * 
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static byte[] initKey() throws NoSuchAlgorithmException {
		KeyGenerator kg = KeyGenerator.getInstance(DES);
		kg.init(56);
		SecretKey secretKey = kg.generateKey();
		return secretKey.getEncoded();
	}

	/**
	 * 解密
	 * 
	 * @param src
	 *            数据源
	 * @param key
	 *            密钥，长度必须是8的倍数
	 * @return 返回解密后的原始数据
	 * @throws DesException
	 * @throws Exception
	 */
	public static byte[] decryptDes(byte[] src, byte[] key) {
		// DES算法要求有一个可信任的随机数源
		SecureRandom sr = new SecureRandom();
		try {
			// 从原始密匙数据创建一个DESKeySpec对象
			DESKeySpec dks = new DESKeySpec(key);
			// 创建一个密匙工厂，然后用它把DESKeySpec对象转换成
			// 一个SecretKey对象
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
			SecretKey securekey = keyFactory.generateSecret(dks);
			// Cipher对象实际完成解密操作
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			// 用密匙初始化Cipher对象
			cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
			// 现在，获取数据并解密
			// 正式执行解密操作
			return cipher.doFinal(src);
		} catch (NoSuchAlgorithmException e) {
		} catch (InvalidKeyException e) {
		} catch (InvalidKeySpecException e) {
		} catch (NoSuchPaddingException e) {
		} catch (IllegalBlockSizeException e) {
		} catch (BadPaddingException e) {
		}
		return null;
	}

	public static byte[] encrypt(byte[] src, byte[] key) {
		if (key.length == 8) {
			//DES
			return encryptDes(src, key);
		} else if (key.length == 16) {
			//2DES
			byte[] key1= new byte[8];
			System.arraycopy(key, 0, key1, 0, 8);
			byte[] key2= new byte[8];
			System.arraycopy(key, 8, key2, 0, 8);
			src = encryptDes(src, key1);
			src = decryptDes(src, key2);
			return encryptDes(src, key1);
		} else if (key.length == 24){
			// 3DES
			byte[] key1= new byte[8];
			System.arraycopy(key, 0, key1, 0, 8);
			byte[] key2= new byte[8];
			System.arraycopy(key, 8, key2, 0, 8);
			byte[] key3= new byte[8];
			System.arraycopy(key, 16, key3, 0, 8);
			src = encryptDes(src, key1);
			src = decryptDes(src, key2);
			return encryptDes(src, key3);
		} else {
		}
		return null;		
	}
	
	public static byte[] decrypt(byte[] src, byte[] key) {
		if (key.length == 8) {
			//DES
			return decryptDes(src, key);
		} else if (key.length == 16) {
			//2DES
			byte[] key1= new byte[8];
			System.arraycopy(key, 0, key1, 0, 8);
			byte[] key2= new byte[8];
			System.arraycopy(key, 8, key2, 0, 8);
			src = decryptDes(src, key1);
			src = encryptDes(src, key2);
			return decryptDes(src, key1);
		} else if (key.length == 24){
			// 3DES
			byte[] key1= new byte[8];
			System.arraycopy(key, 0, key1, 0, 8);
			byte[] key2= new byte[8];
			System.arraycopy(key, 8, key2, 0, 8);
			byte[] key3= new byte[8];
			System.arraycopy(key, 16, key3, 0, 8);
			src = decryptDes(src, key3);
			src = encryptDes(src, key2);
			return decryptDes(src, key1);
		} else {
		}
		return null;
	}
	
	public static void main(String[] args) throws Exception {

		String key = "cloudskycloudskycloudsky";
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("userId", "800000001");
		jsonObject.addProperty("password", "e10adc3949ba59abbe56e057f20f883e");
		jsonObject.addProperty("chanel", "M");
		jsonObject.addProperty("deviceNo", "SN123123");
		System.out.println("加密前："+jsonObject.toString());
		String miwen = DesUtils.getEncString(jsonObject.toString(), key.getBytes());
		System.out.println("加密后："+miwen);
		System.out.println("解密后："+DesUtils.getDesString(miwen, key.getBytes()));
	
	}
}
