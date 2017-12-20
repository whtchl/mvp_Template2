package com.lifeng.f300.common.utils;

import com.lifeng.f300.common.exception.HwException;
import com.lifeng.f300.config.ConnectURL;

import java.io.File;
import java.io.FileOutputStream;


/**
 * Decripe : 
 * Date : 2016年3月5日
 * Version : 1.0 <br/>
 *
 * @author : wangkeze
 */
public class WriteInfoToFile {
	private Process p;
	/**
	 * @param path 路径
	 * @param fileName 文件名称，全程
	 * @param info  写入的内容
	 * @param isEncrypt 是否需要加密,true加密之后再保存
	 */
	public void WriteSomeThingInfoToFile(String path, String fileName, String info,boolean isEncrypt) throws HwException {
		LogUtils.d("wang","path:"+path+" filename:"+fileName+" info:"+info);
		if (isEncrypt) {
			info = DesUtils.getEncString(info, ConnectURL.DESUTILS_KEY.getBytes());//加密的数据
		}
		File file = new File(path);
		if (!file.exists()) {
			file.mkdir();
		}
		try {
			p = Runtime.getRuntime().exec("chmod 777 " + file);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			File fileTxt = new File(path + fileName);
			try {
				p = Runtime.getRuntime().exec("chmod 777 " + fileTxt);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			FileOutputStream writeFile = new FileOutputStream(fileTxt);
			writeFile.write(info.getBytes());
			writeFile.flush(); // 刷新写入文件中
			writeFile.close();
			wol.writeOverCallBack(true);   //写入成功
			LogUtils.d("wang","写入成功");
		} catch (Exception e) {
			LogUtils.d("wang","写入失败");
			wol.writeOverCallBack(false);  //写入失败
			e.printStackTrace();
			throw new HwException(HwException.CODE.ERR_WIRTE_FILE,HwException.MSG_CODE.ERR_MSG_WIRTE_FILE);

		}
	}
	
	public WriteOverListener wol;
	
	public void setWriteOverCallBack(WriteOverListener wol){
		this.wol=wol;
	}
	
	public interface WriteOverListener {
		public void writeOverCallBack(boolean writeOK) ;
	}
}
