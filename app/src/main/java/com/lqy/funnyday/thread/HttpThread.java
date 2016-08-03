package com.lqy.funnyday.thread;

import android.content.Context;

import com.lqy.funnyday.http.HttpUtil;
import com.lqy.funnyday.util.OkHttpResponseUtil;

/**
 * Created by mrliu on 16-7-23.
 */
public class HttpThread implements Runnable {

    private HttpUtil httpUtil;
    private OkHttpResponseUtil okHttpResponseUtil;
    private Context context;

    public HttpThread(Context context){
        this.context = context;
    }

    @Override
    public void run() {


    }

    /*private void saveRegionCodeToDb(Context context) {
        httpUtil = new HttpUtil();
        locationDB = new LocationDB(context);
        okHttpResponseUtil = new OkHttpResponseUtil();
        final String headerAddress = "http://www.weather.com.cn/data/list3/city";
        final String provinceAddress = "http://www.weather.com.cn/data/list3/city.xml";
        final String tailAddress = ".xml";
        String provinceCode;
        String cityCode;

        String provinceResponse = httpUtil.doSynGet(provinceAddress);
        okHttpResponseUtil.handleProvincesResponse(locationDB,provinceResponse);

        List<Province> provinces = locationDB.loadProvince();
        for (Province province : provinces){
            provinceCode = province.getProvince_code();
            String cityAddress = headerAddress+provinceCode+tailAddress;
            String cityResponse = httpUtil.doSynGet(cityAddress);
            okHttpResponseUtil.handlerCityResponse(locationDB,cityResponse, Integer.parseInt(provinceCode));

            List<City> cityList = locationDB.loadCity(Integer.parseInt(provinceCode));
            for(City city : cityList){
                cityCode = city.getCity_code();
                String countyAddress = headerAddress+cityCode+tailAddress;
                String countyResponse = httpUtil.doSynGet(countyAddress);
                okHttpResponseUtil.handlerCountiesResponse(locationDB,countyResponse, Integer.parseInt(cityCode));
            }
        }
    }*/
}
