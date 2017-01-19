package com.tingchung.life.electric;

/**
 * Created by Administrator on 2017/1/13.
 */

public class ElectricObject implements java.io.Serializable {
    private long id = 0;
    private String date = "";
    private String time = "";
    private double  number =0;
    private boolean selected;

    public ElectricObject(){
    }
    public ElectricObject(long id,String date,String time,double  number){
        this.id = id;
        this.date=date;
        this.time = time;
        this.number = number;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getNumber() {
        return number;
    }

    public void setNumber(double number) {
        this.number = number;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}
