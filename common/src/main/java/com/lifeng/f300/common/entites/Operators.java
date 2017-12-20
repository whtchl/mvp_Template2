package com.lifeng.f300.common.entites;

/**
 * Created by happen on 2017/8/28.
 * 操作员信息
 */

public class Operators {
    private String id;
    private String operatorId;
    private String password;
    public Operators(String id,String operatorId,String password){
        this.id = id;
        this.operatorId = operatorId;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
