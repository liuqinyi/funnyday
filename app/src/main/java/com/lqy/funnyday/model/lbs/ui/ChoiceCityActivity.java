package com.lqy.funnyday.model.lbs.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.lqy.funnyday.db.WeatherDB;
import com.lqy.funnyday.model.lbs.adapter.LocateListViewAdapter;
import com.lqy.greendao.City;
import com.lqy.greendao.Country;
import com.lqy.greendao.Province;

import java.util.ArrayList;
import java.util.List;

import com.lqy.com.funnyday.R;

/**
 * Created by mrliu on 16-7-23.
 */
public class ChoiceCityActivity extends AppCompatActivity {

    /**
     * 城市级别
     */
    private static final int LEVEL_PROVINCE = 0;
    private static final int LEVEL_CITY = 1;
    private static final int LEVEL_COUNTRY = 2;

    private ListView listView;

    private TextView tvProvince, tvCity, tvCountry; //地名显示

    private TextView toolbarTitle;

    private List<Province> provinceList; //省列表

    private List<City> cityList; //市列表

    private List<Country> countryList; //县列表

    private Province selectedProvince; //选中的省份

    private City selectedCity; //选中的城市

    private Country selectedCountry; //选中县区

    private List<String> dataList; //创建LIstView显示数据集合

    private int currentLevel; //当前选中的级别

    private LocateListViewAdapter locateListViewAdapter; //创建本地

    private ArrayAdapter<String> arrayAdapter; //创建一个基本数组适配器



    private WeatherDB weatherDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_city);
        initView();
    }

    private void initView() {
        /**
         * 实例化UI对象
         **/
        toolbarTitle = (TextView)findViewById(R.id.toolbar_title);
        listView = (ListView) findViewById(R.id.lv_city);
        tvProvince = (TextView)findViewById(R.id.tv_province_name);
        tvCity = (TextView)findViewById(R.id.tv_city_name);
        tvCountry = (TextView)findViewById(R.id.tv_country_name);
        /**
         * 实例化工具对象
         * */
        weatherDB = new WeatherDB(this);
        /**
         * 实例化List对象
         * */
        dataList = new ArrayList<>();
        provinceList = weatherDB.loadProvince();
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, dataList);
        /**
         * ListView 添加*/
        for (Province province : provinceList) {
            String provinceName = province.getProvince_name();
            dataList.add(provinceName);
        }
        listView.setAdapter(arrayAdapter);
        queryProvinces();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(currentLevel == LEVEL_PROVINCE){
                    selectedProvince = provinceList.get(position);
                    queryCities();
                }else if(currentLevel == LEVEL_CITY){
                    selectedCity = cityList.get(position);
                    queryCounties();
                }else if(currentLevel == LEVEL_COUNTRY){
                    selectedCountry = countryList.get(position);
                    tvCountry.setText(selectedCountry.getCountry_name());

                }
            }
        });
    }

    /**
     * */
    public void queryProvinces() {
        this.provinceList = weatherDB.loadProvince();
        if (provinceList.size() > 0) {
            this.dataList.clear();
            for (Province province : provinceList) {
                dataList.add(province.getProvince_name());
            }
            arrayAdapter.notifyDataSetChanged();
            listView.setSelection(0);
            toolbarTitle.setText("中国");
            currentLevel = LEVEL_PROVINCE;
        }else{
            queryFromServer(null,"province");
        }
    }

    public void queryCities(){
        this.cityList = weatherDB.loadCity(Integer.parseInt(selectedProvince.getProvince_code()));
        if(cityList.size() > 0){
            this.dataList.clear();
            for (City city : cityList){
                dataList.add(city.getCity_name());
            }
            arrayAdapter.notifyDataSetChanged();
            listView.setSelection(0);
            tvProvince.setText(selectedProvince.getProvince_name());
            currentLevel = LEVEL_CITY;
        }else{
            queryFromServer(selectedProvince.getProvince_code(),"city");
        }
    }

    public void queryCounties(){
        this.countryList = weatherDB.loadCountry(Integer.parseInt(selectedCity.getCity_code()));
        if (countryList.size() > 0){
            dataList.clear();
            for(Country country : countryList){
                dataList.add(country.getCountry_name());
            }
            arrayAdapter.notifyDataSetChanged();
            listView.setSelection(0);
            tvCity.setText(selectedCity.getCity_name());
            currentLevel = LEVEL_COUNTRY;
        }else{
            queryFromServer(selectedCity.getCity_code(),"city");
        }
    }


    private void queryFromServer(String code, String type) {

    }

    @Override
    public void onBackPressed() {
        if(currentLevel == LEVEL_COUNTRY){
            tvCountry.setText("县");
            queryCities();
        }else if(currentLevel ==LEVEL_CITY){
            tvCity.setText("市");
            queryProvinces();
        }else if(currentLevel == LEVEL_PROVINCE){
            tvProvince.setText("省");
        }else {
            finish();
        }
    }
}
