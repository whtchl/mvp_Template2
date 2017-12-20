package com.lifeng.f300.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

import com.lifeng.f300.common.utils.LogUtils;

/**
 * Decripe :文件读工具类 Date : 2016年3月4日 Version : 1.0 <br/>
 * 
 * @author : wangkeze
 */
public class FileUtils {
	/**
	 * 读取文件
	 * 
	 * @param path
	 *            完整路径
	 * @param charset
	 *            编码格式
	 * @return
	 */
	public static String readFile(String path, String charset) {
		try {
			byte[] buffer = readFile(path);
			if (null == buffer) {
				return null;
			}
			return new String(buffer, charset);
		} catch (Exception e) {
			LogUtils.d("wang", e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 读取文件内容
	 * 
	 * @param path
	 * @return buffer
	 */
	private static byte[] readFile(String path) {
		InputStream input = null;
		byte[] buffer = null;
		File file = new File(path);
		if (file.isDirectory() || !file.exists()) {
			LogUtils.d("wang", path + " is not exist");
			return buffer;
		}
		try {
			input = new FileInputStream(file);
			buffer = new byte[input.available()];
			input.read(buffer);
		} catch (Exception e) {
			LogUtils.d("wang", e.getMessage(), e);
		} finally {
			try {
				if (null != input) {
					input.close();
					input = null;
				}
			} catch (Exception e2) {
				LogUtils.d("wang", e2.getMessage(), e2);
			}
		}
		return buffer;
	}

	/**
	 * 文件的复制
	 * 
	 * @param src
	 *            原文件路径
	 * @param des
	 *            目的文件路径
	 * @return
	 */
	public static boolean copyFile(String src, String des) {
		FileInputStream in = null;
		FileOutputStream out = null;
		int readNum = 0;
		try {
			in = new FileInputStream(src);
			out = new FileOutputStream(des);
			byte[] bt = new byte[1024];
			while ((readNum = in.read(bt)) != -1) {
				out.write(bt, 0, bt.length);
			}
			if (null != in) {
				in.close();
				in = null;
			}
			if (null != out) {
				out.close();
				out = null;
			}
			return true;
		} catch (Exception e) {
			try {
				if (null != in) {
					in.close();
					in = null;
				}
				if (null != out) {
					out.close();
					out = null;
				}
			} catch (IOException ioe) {
			}
			return false;
		} finally {
			try {
				if (null != in) {
					in.close();
					in = null;
				}
				if (null != out) {
					out.close();
					out = null;
				}
			} catch (IOException ioe) {
			}
		}
	}

	/**
	 * 读取文件的大小
	 * @param filePath
	 * @return
	 */
	public static long getFileSize(String filePath) {
		FileInputStream fis = null;
		FileChannel fc = null;
		long fileSize = 0;
		try {
			File f = new File(filePath);
			if (f.exists() && f.isFile()) {
				fis = new FileInputStream(f);
				fc = fis.getChannel();
				fileSize = fc.size();
				if(null != fis){
					fis.close();
					fis = null;
				}
				if(null != fc){
					fc.close();
					fc = null;
				}
			} else {
				LogUtils.d("wang", "file doesn't exist or is not a file");
			}
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		} finally {
			try {
				if(null != fis){
					fis.close();
					fis = null;
				}
				if(null != fc){
					fc.close();
					fc = null;
				}
			} catch (IOException e) {
			}
		}
		
		return fileSize;
	}

	/**
	 * 写文件
	 *
	 * @param path
	 * @param content
	 * @return
	 */
	public static boolean writeFile(String path, String content) {
		OutputStream out = null;
		try {
			File file = new File(path);
			try {
				Process p = Runtime.getRuntime().exec("chmod 666 " +  file );
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			out = new FileOutputStream(file);
			out.write(content.getBytes());
			out.flush();
			return true;
		} catch (Exception e1) {
		} finally {
			try {
				if (null != out) {
					out.close();
					out = null;
				}
			} catch (Exception e2) {
				LogUtils.d("wang", e2.getMessage(), e2);
			}
		}
		return false;
	}
}
