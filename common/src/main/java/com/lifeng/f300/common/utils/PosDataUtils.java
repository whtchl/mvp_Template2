package com.lifeng.f300.common.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.os.Environment;
import android.text.TextUtils;

import com.google.gson.JsonObject;
import com.jude.utils.JUtils;
import com.lifeng.f300.common.entites.PosData;


/**
 * Describe : POS数据工具类<br/>
 * Date : 2015年10月24日下午2:59:42 <br/>
 * Version : 1.0 <br/>
 * 
 * @author XiongWei
 */
public class PosDataUtils {
	/** SDCard路径 */
	public static final String SD_CARD_PATH = Environment.getExternalStorageDirectory().getPath();
	/** 中文字符集 UTF-8 */
	public static final String CHARSET_UTF_8 = "UTF-8";
	/** POS数据保存路径 */
//	public static final String POS_DATA_FILE_PATH = Constants.SPECIAL_PATH +"lifeng_pos_data.txt";
	public static final String POS_DATA_FILE_PATH = SD_CARD_PATH + "/lifeng";
	
	public static final String POS_DATA_NAME = "pos_data.txt";
	public static final String POS_DATA_NAME_BEFORE = "posdata_before.txt"; //保存交易异常的交易

	/** POS流水号最大值 */
	private static final int POS_SERIAL_NUMBER_MAX = 999999;

	/**
	 * 获取POS交易流水号
	 * 
	 * @return
	 */
	public static String getPosSerialNumber() {
		PosData data = getPosData();
		data.setPosSerialNumber(data.getPosSerialNumber()+1);
		if (data.getPosSerialNumber() > POS_SERIAL_NUMBER_MAX) {
			data.setPosSerialNumber(PosData.SERIAL_NUMBER_INITIAL_VALUE+1);
		}
		// 流水号增加之后需要重新保存到文件
		writePosData(data);
		return toStringSerialNumber(data.getPosSerialNumber());
	}

	/**
	 * 获取已经交易的最大流水号
	 * @return
	 */
	public static String getYJYMAXPosSerialNumber(){
		PosData data = getPosData();
		return  toStringSerialNumber(data.getPosSerialNumber());
	}

	/**
	 * 获取已交易的流水号list
	 * @return
	 */
	public static List<String> getPosSerialNumberList(){
		PosData data = getPosData();
		ArrayList serialNumList = new ArrayList();
		//List<String> serialNumList = new ArrayList();;
		for(int i=data.getPosSerialNumber(); i>0; i--){
			serialNumList.add(toStringSerialNumber(i));
			//JUtils.Log("serialNumList:");
		}
		//test begin
		Iterator it= serialNumList.iterator();
		while (it.hasNext()){
			JUtils.Log("serailNumList:"+it.next().toString());
		}
		//test end

		return  serialNumList;
	}

    public static String toStringSerialNumber(int serialNumber){
    	String serialNum = serialNumber +"";
    	String var = "";
    	for(int i = 0;i<6-serialNum.length();i++){
    		var+="0";
    	}
		JUtils.Log("var:"+var+"  serialNum:"+serialNum);
    	return var+serialNumber+"";
    }

	/**
	 * 获取批次号
	 * 
	 * @return 当返回为-1时,表示没有获取到批次号,需要有一次提示
	 */
	public static int getBatchNumber() {
		return getPosData().getBatchNumber();
	}

	/**
	 * 获取交易重发次数
	 * 
	 * @return
	 */
	public static int getTradeSendRetryTime() {
		return getPosData().getTradeSendRetryTime();
	}

	/**
	 * 获取交易凭条打印张数
	 * 
	 * @return
	 */
	public static int getTradePrintCopys() {
		return getPosData().getTradePrintCopys();
	}

	/**
	 * 保存POS交易批次号
	 * 
	 * @param batchNumber
	 *            批次号,签到完成后由后台返回
	 * @return
	 */
	public static boolean saveBatchNumber(int batchNumber) {
		String content = readPosData();
		PosData data = JSONUtils.fromJson(content, PosData.class);
		data.setBatchNumber(batchNumber);
		writePosData(data);
		return false;
	}

	/**
	 * 保存POS配置
	 * 
	 * @param data
	 * @return
	 */
	public static boolean savePosConfig(PosData data) {
		return writePosData(data);
	}

	/**
	 * 获取PosData对象
	 * 
	 * @return
	 */
	private static PosData getPosData() {
		String content = readPosData();
		PosData data = null;
		if (TextUtils.isEmpty(content) || content.equals("{}")) {
			data = new PosData();
		} else {
			data = JSONUtils.fromJson(content, PosData.class);
		}
		return data;
	}

	/**
	 * 读取文件数据
	 * 
	 * @return
	 */
	private static String readPosData() {
		JUtils.Log("readPosData path:"+POS_DATA_FILE_PATH+"/"+POS_DATA_NAME);
		File file = new File(POS_DATA_FILE_PATH+"/"+POS_DATA_NAME);
		if (!file.exists()) {
			return null;
		}

		return FileUtils.readFile(file.getPath(), CHARSET_UTF_8);
	}

	/**
	 * 保存数据
	 * 
	 * @param content
	 * @return
	 */
	// private static boolean writePosData(String content) {
	// return FileUtils.writeFile(POS_DATA_FILE_PATH, content);
	// }

	/**
	 * 保存数据
	 * 
	 * @param data
	 * @return
	 */
	private static boolean writePosData(PosData data) {
		File file = new File(POS_DATA_FILE_PATH);
		if(!file.exists()){
			file.mkdir();
		}
		return FileUtils.writeFile(POS_DATA_FILE_PATH+"/"+POS_DATA_NAME, toJson(data));
	}

	/**
	 * 生成JSON格式
	 * 
	 * @param data
	 * @return
	 */
	private static String toJson(PosData data) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(PosData.POS_SERIAL_NUMBER, data.getPosSerialNumber());
		jsonObject.addProperty(PosData.BATCH_NUMBER, data.getBatchNumber());
		jsonObject.addProperty(PosData.TRADE_PRINT_COPYS, data.getTradePrintCopys());
		jsonObject.addProperty(PosData.TRADE_SEND_RETRY_TIME, data.getTradeSendRetryTime());
		return jsonObject.toString();
	}
	 //删除文件
    public static void delFile(String fileName){
        File file = new File(fileName);
        if(file.exists() && file.isFile()){
            file.delete();
        }
    }
}
