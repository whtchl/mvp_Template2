package com.lifeng.f300.common.entites;

import java.io.Serializable;

/**
 * Created by happen on 2017/9/8.
 */

public class TransFlowEntity implements Serializable {
    public TransFlowEntity(String status,String money){
        this.status =status;
        this.money = money ;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    String status;
    String money;

}
