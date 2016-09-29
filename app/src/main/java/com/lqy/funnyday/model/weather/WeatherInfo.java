package com.lqy.funnyday.model.weather;

/**
 * Created by mrliu on 16-8-3.
 */
public class WeatherInfo {

    private String city;
    private String cityid;
    private String temp1;
    private String temp2;
    private String weather;
    private String img1;
    private String img2;
    private String ptime;

    public void setCity(String city) {
        this.city = city;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    public void setPtime(String ptime) {
        this.ptime = ptime;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    public void setTemp1(String temp1) {
        this.temp1 = temp1;
    }

    public void setTemp2(String temp2) {
        this.temp2 = temp2;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }
}
