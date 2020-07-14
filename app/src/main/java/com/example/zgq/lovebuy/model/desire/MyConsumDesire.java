package com.example.zgq.lovebuy.model.desire;

import android.content.SharedPreferences;

import com.example.zgq.lovebuy.data.Constants;

/**
 * Created by 37902 on 2016/1/19.
 */
public class MyConsumDesire extends ConsumDesire{
    private Double number;
    private String detail;
    private String date;
    //状态 1是完成 0是未完成
    private Integer status;

    private String user;

    public Double getNumber() {
        return number;
    }

    public void setNumber(Double number) {
        this.number = number;
    }

    public String getDetail() {
        return detail;
    }
    
    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public MyConsumDesire( Double number, String detail, String date,Integer status) {
        this.number = number;
        this.detail = detail;
        this.date = date;
        this.status = status;
        this.user= Constants.userName;
    }


}
