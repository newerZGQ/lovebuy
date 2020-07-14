package com.example.zgq.lovebuy.model.consum;

import java.io.Serializable;

/**
 * Created by 37902 on 2016/1/11.
 */
public class DayConsum extends AllConsum implements Serializable {
    public static boolean isSingleCon = false;
    private double dayConsum;
    private double dayEarning;
    private String dayDate;

    public double getDayConsum() {
        return dayConsum;
    }

    public void setDayConsum(double dayConsum) {
        this.dayConsum = dayConsum;
    }

    public double getDayEarning() {
        return dayEarning;
    }

    public void setDayEarning(double dayEarning) {
        this.dayEarning = dayEarning;
    }

    public String getDayDate() {
        return dayDate;
    }

    public void setDayDate(String dayDate) {
        this.dayDate = dayDate;
    }
    public DayConsum(double dayConsum, double dayEarning, String dayDate) {

        this.dayConsum = dayConsum;
        this.dayEarning = dayEarning;
        this.dayDate = dayDate;
    }
}
