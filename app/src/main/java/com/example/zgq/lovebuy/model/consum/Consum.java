package com.example.zgq.lovebuy.model.consum;

import android.content.Context;
import android.util.Log;

import com.example.zgq.lovebuy.data.Constants;

import java.io.Serializable;

import cn.bmob.v3.BmobUser;

/**
 * Created by 37902 on 2015/12/28.
 */
public class Consum extends AllConsum implements Serializable {
    public static final int ISCOST = 1;
    public static final int ISEARNING = 0;
    private Double number = new Double(0);
    private String lable = "无";
    private String date = "0000-00-00";
    private Integer happiness = new Integer(-1);
    private String detail = "";
    private Integer property = new Integer(-1);

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    private String user = "null";

    public Consum(){
//        this.setTableName("1111111");

    }
    public Consum(Double number, String lable, String date, Integer happiness, String detail, Integer property) {
        this.user = Constants.userName;
        this.number = number;
        this.lable = lable;
        this.date = date;
        this.happiness = happiness;
        this.detail = detail;
        this.property = property;
    }
//    public Consum(Double number, String lable, String date, Integer happiness, String detail, Integer property) {
//        this.number = number;
//        this.lable = lable;
//        this.date = date;
//        this.happiness = happiness;
//        this.detail = detail;
//        this.property = property;
//        this.setTableName(ContantsUtil.userName);
//    }


    public Double getNumber() {
        return number;
    }

    public void setNumber(Double number) {
        this.number = number;
    }

    public String getLable() {
        return lable;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getHappiness() {
        return happiness;
    }

    public void setHappiness(Integer happiness) {
        this.happiness = happiness;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Integer getProperty() {
        return property;
    }

    public void setProperty(Integer property) {
        this.property = property;
    }

    @Override
    public String toString() {
        return date;
    }
}
