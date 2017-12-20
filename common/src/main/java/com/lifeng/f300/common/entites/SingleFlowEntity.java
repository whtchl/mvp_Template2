package com.lifeng.f300.common.entites;

import com.jude.utils.JUtils;
import com.lifeng.f300.common.utils.PosDataUtils;

import java.io.Serializable;

/**
 * 单笔流水的查询实体
 * Created by happen on 2017/9/21.
 */

public class SingleFlowEntity implements Serializable {
    private String batchno;
    private String serialNumber;
    private String operatorId;
    private String merchantCode;
    private String posCode;
    private String secondaryKey;

    public SingleFlowEntity(String batchno,
                            String serialNumber,
                            String operatorId,
                            String merchantCode,
                            String posCode,
                            String secondaryKey){
        this.batchno = batchno;
        this.secondaryKey = secondaryKey;
        this.serialNumber = serialNumber;
        this.operatorId = operatorId;
        this.merchantCode = merchantCode;
        this.posCode = posCode;
    }

    public String getBatchno() {
        return batchno;
    }

    public void setBatchno(String batchno) {
        this.batchno = batchno;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getMerchantCode() {
        return merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    public String getPosCode() {
        return posCode;
    }

    public void setPosCode(String posCode) {
        this.posCode = posCode;
    }

    public String getSecondaryKey() {
        return secondaryKey;
    }

    public void setSecondaryKey(String secondaryKey) {
        this.secondaryKey = secondaryKey;
    }

    public String toString(){
        JUtils.Log("batchno:"+batchno+" operatorId:"+operatorId+"  merchantCode:"+merchantCode+" posCode:"+posCode+" secondaryKey:"+secondaryKey);
        return "";
    }



}
