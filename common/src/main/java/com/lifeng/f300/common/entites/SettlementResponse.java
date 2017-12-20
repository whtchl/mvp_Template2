package com.lifeng.f300.common.entites;

import java.io.Serializable;
import java.util.List;

/**
 * Created by happen on 2017/10/16.
 * 结算时服务器返回数据的bean
 */

public class SettlementResponse implements Serializable {

    private static final long serialVersionUID = -3939952013606895558L;

    public String MERCHANTCODE;                                     // 商户号
    public String POSCODE;                                          // POS
    public String BATCHNO;                                          // 批次号
    public String NEW_POS_BATCH;                                    // 下次签到批次号
    public String IS_SUCCESS;                                       // 结算是否成功
    public String CONSUME_COUNT_TRADE;                              // 交易总笔数
    public String CONSUME_DEPOSIT_ADD;                              // 充值金额（不包括赠送金额）
    public String CONSUME_DEPOSIT_SUB;                              // 储值消费金额
    public String CONSUME_POINT_SUB;                                // 积分消费（积分点数）
    public String CONSUME_CASH_AMOUNT;                              // 现金消费金额
    public String CONSUME_BANK_AMOUNT;                              // 银行卡消费金额
    public String CONSUME_COUPON_AMOUNT;                            // 优惠券消费金额
    public String STATUS;                                           // 成功标志
    public String MESSAGE;                                          // 错误信息
    public List<ErrorTransInfo> POS_MISS_LIST;                      // POS缺失的交易信息
    public List<ErrorTransInfo> POS_MORE_LIST;                      // POS多出的交易信息

    public class ErrorTransInfo {
        public String BATCHNO;                                      // 000161
        public String INVOICE_TYPE;                                 // 3，交易类型
        public String PAID;                                         // 22.00 实际交易金额
        public String TRACE_NO;                                     // 000001
    }
}
