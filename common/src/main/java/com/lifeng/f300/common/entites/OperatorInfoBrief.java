package com.lifeng.f300.common.entites;

import java.io.Serializable;

/**
 * Created by happen on 2017/8/22.
 */

public class OperatorInfoBrief implements Serializable {
    private int id;
    private String operatorId;
    private String password;

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

    public OperatorInfoBrief(int id, String operatorId, String password) {
        this.id = id;
        this.operatorId = operatorId;
        this.password = password;
    }

    public OperatorInfoBrief(String operatorId, String password) {
        this.id = id;
        this.operatorId = operatorId;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
