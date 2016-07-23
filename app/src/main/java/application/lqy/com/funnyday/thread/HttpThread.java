package application.lqy.com.funnyday.thread;

import android.content.Context;

import com.lqy.greendao.City;
import com.lqy.greendao.Province;

import java.util.List;

import application.lqy.com.funnyday.db.WeatherDB;
import application.lqy.com.funnyday.http.HttpUtil;
import application.lqy.com.funnyday.util.AnalysisUtil;

/**
 * Created by mrliu on 16-7-23.
 */
public class HttpThread implements Runnable {

    private HttpUtil httpUtil;
    private WeatherDB weatherDB;
    private AnalysisUtil analysisUtil;
    private Context context;

    public HttpThread(Context context){
        this.context = context;
    }

    @Override
    public void run() {

        saveRegionCodeToDb(this.context);

    }

    private void saveRegionCodeToDb(Context context) {
        httpUtil = new HttpUtil();
        weatherDB = new WeatherDB(context);
        analysisUtil = new AnalysisUtil();
        final String headerAddress = "http://www.weather.com.cn/data/list3/city";
        final String provinceAddress = "http://www.weather.com.cn/data/list3/city.xml";
        final String tailAddress = ".xml";
        String provinceCode;
        String cityCode;

        String provinceResponse = httpUtil.get(provinceAddress);
        analysisUtil.handleProvincesResponse(weatherDB,provinceResponse);

        List<Province> provinces = weatherDB.loadProvince();
        for (Province province : provinces){
            provinceCode = province.getProvince_code();
            String cityAddress = headerAddress+provinceCode+tailAddress;
            String cityResponse = httpUtil.get(cityAddress);
            analysisUtil.handlerCityResponse(weatherDB,cityResponse, Integer.parseInt(provinceCode));

            List<City> cityList = weatherDB.loadCity(Integer.parseInt(provinceCode));
            for(City city : cityList){
                cityCode = city.getCity_code();
                String countyAddress = headerAddress+cityCode+tailAddress;
                String countyResponse = httpUtil.get(countyAddress);
                analysisUtil.handlerCountiesRespose(weatherDB,countyResponse, Integer.parseInt(cityCode));
            }
        }
    }
}
