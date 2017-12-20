package com.lifeng.f300.common.entites;

import java.io.Serializable;

/**
 * Created by happen on 2017/12/5.
 */

public class picName implements Serializable {
    private String scene;
    private String paid;
    private String shakes ;

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String getShakes() {
        return shakes;
    }

    public void setShakes(String shakes) {
        this.shakes = shakes;
    }


    public picName(String scene, String paid, String shakes  ){
        scene = scene;
        paid= paid;
        shakes= shakes;
    }
}
