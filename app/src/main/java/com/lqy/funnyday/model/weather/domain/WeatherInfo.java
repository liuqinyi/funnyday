package com.lqy.funnyday.model.weather.domain;

/**
 * Created by mrliu on 16-8-3.
 */
public class WeatherInfo {

    private String cityName;
    private String weatherCode;
    private String temp1;
    private String temp2;
    private String weatherDesp;
    private String ptTime;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getPtTime() {
        return ptTime;
    }

    public void setPtTime(String ptTime) {
        this.ptTime = ptTime;
    }

    public String getTemp1() {
        return temp1;
    }

    public void setTemp1(String temp1) {
        this.temp1 = temp1;
    }

    public String getTemp2() {
        return temp2;
    }

    public void setTemp2(String temp2) {
        this.temp2 = temp2;
    }

    public String getWeatherCode() {
        return weatherCode;
    }

    public void setWeatherCode(String weatherCode) {
        this.weatherCode = weatherCode;
    }

    public String getWeatherDesp() {
        return weatherDesp;
    }

    public void setWeatherDesp(String weatherDesp) {
        this.weatherDesp = weatherDesp;
    }
}
