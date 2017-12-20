package com.lifeng.f300.common.entites;

public class PosData {

	/** POS终端流水号 */
	public static final String POS_SERIAL_NUMBER = "posSerialNumber";
	/** 批次号 */
	public static final String BATCH_NUMBER = "batchNumber";
	/** 打印张数 */
	public static final String TRADE_PRINT_COPYS = "tradePrintCopys";
	/** 重发次数 */
	public static final String TRADE_SEND_RETRY_TIME = "tradeSendRetryTime";
	
	/** 凭条默认打印张数 */
	private final int DEFAULT_PRINT_COPYS = 2;
	/** 默认重发次数 */
	private final int DEFAULT_SEND_RETRY_TIME = 3;
	/** 流水号初始值 */
	public static final int SERIAL_NUMBER_INITIAL_VALUE = 0; 

	// POS流水号
	private int posSerialNumber = SERIAL_NUMBER_INITIAL_VALUE;
	// 批次号
	private int batchNumber = -1;
	// 凭条打印张数
	private int tradePrintCopys = DEFAULT_PRINT_COPYS;
	// POS交易失败重发次数
	private int tradeSendRetryTime = DEFAULT_SEND_RETRY_TIME;

	public int getPosSerialNumber() {
		return posSerialNumber;
	}

	public void setPosSerialNumber(int posSerialNumber) {
		this.posSerialNumber = posSerialNumber;
	}

	public int getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(int batchNumber) {
		this.batchNumber = batchNumber;
	}

	public int getTradePrintCopys() {
		return tradePrintCopys;
	}

	public void setTradePrintCopys(int tradePrintCopys) {
		this.tradePrintCopys = tradePrintCopys;
	}

	public int getTradeSendRetryTime() {
		return tradeSendRetryTime;
	}

	public void setTradeSendRetryTime(int tradeSendRetryTime) {
		this.tradeSendRetryTime = tradeSendRetryTime;
	}

}
