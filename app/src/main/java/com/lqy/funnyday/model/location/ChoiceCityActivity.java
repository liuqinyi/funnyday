package com.lqy.funnyday.model.location;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lqy.funnyday.R;
import com.lqy.funnyday.db.LocationDB;
import com.lqy.funnyday.util.HttpUtil;
import com.lqy.funnyday.ui.activity.MainActivity;
import com.lqy.funnyday.util.OkHttpResponseUtil;
import com.lqy.greendao.City;
import com.lqy.greendao.Country;
import com.lqy.greendao.Province;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

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
    private static final String TAG = "ChoiceActivity";

    /**
     * 组件
     */
    private ListView listView;
    private TextView tvProvince, tvCity, tvCountry; //地名显示
    private TextView toolbarTitle;
    private ProgressDialog progressDialog;

    /**
     * List
     */
    private List<Province> provinceList; //省列表
    private List<City> cityList; //市列表
    private List<Country> countryList; //县列表
    private List<String> dataList; //创建LIstView显示数据集合
    /**
     * Instance
     */
    private Province selectedProvince; //选中的省份
    private City selectedCity; //选中的城市
    private Country selectedCountry; //选中县区


    private int currentLevel; //当前选中的级别

    /**
     * 工具类对象
     */
    private ArrayAdapter arrayAdapter; //创建一个基本数组适配器
    private LocationDB locationDB;


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
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        listView = (ListView) findViewById(R.id.lv_city);
        tvProvince = (TextView) findViewById(R.id.tv_province_name);
        tvCity = (TextView) findViewById(R.id.tv_city_name);
        tvCountry = (TextView) findViewById(R.id.tv_country_name);
        /**
         * 实例化工具对象
         * */
        locationDB = LocationDB.getInstance(this);
        dataList = new ArrayList<>();

        arrayAdapter = new ArrayAdapter(ChoiceCityActivity.this,android.R.layout.simple_list_item_1,dataList);
        listView.setAdapter(arrayAdapter);
        queryProvinces();
        /**
         * listView设置监听器，更具同的等级响应不同事件
         * */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel == LEVEL_PROVINCE) {
                    selectedProvince = provinceList.get(position);
                    queryCities();
                } else if (currentLevel == LEVEL_CITY) {
                    selectedCity = cityList.get(position);
                    queryCounties();
                } else if (currentLevel == LEVEL_COUNTRY) {
                    selectedCountry = countryList.get(position);
                    tvCountry.setText(selectedCountry.getCountry_name());

                    //将县级代码存至SharedPreferences中
                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(ChoiceCityActivity.this).edit();
                    editor.putString("county_code",selectedCountry.getCountry_code());
                    Intent intent = new Intent(ChoiceCityActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    /**
     * 查询全国所有省，的数据，先从数据库查询，如果没有再从服务器上查询
     */
    private void queryProvinces() {
        this.provinceList = this.locationDB.loadProvince();
        if (provinceList.size() > 0) {
            this.dataList.clear();
            for (Province province : provinceList) {
                dataList.add(province.getProvince_name());
            }
            arrayAdapter.notifyDataSetChanged();
            listView.setSelection(0);
            toolbarTitle.setText("中国");
            currentLevel = LEVEL_PROVINCE;
        } else {
            queryFromServer(null, "province");
        }
    }

    /**
     * 查询全国所有市的数据，先从数据库查询，如果没有再从服务器上查询
     */
    private void queryCities() {
        this.cityList = locationDB.loadCity(Integer.parseInt(selectedProvince.getProvince_code()));
        if (cityList.size() > 0) {
            this.dataList.clear();
            for (City city : cityList) {
                dataList.add(city.getCity_name());
            }
            arrayAdapter.notifyDataSetChanged();
            listView.setSelection(0);
            tvProvince.setText(selectedProvince.getProvince_name());
            currentLevel = LEVEL_CITY;
        } else {
            queryFromServer(selectedProvince.getProvince_code(), "city");
        }
    }

    /**
     * 查询全国所有县的数据，先从数据库查询，如果没有再从服务器上查询
     */
    private void queryCounties() {
        this.countryList = locationDB.loadCountry(Integer.parseInt(selectedCity.getCity_code()));
        if (countryList.size() > 0) {
            dataList.clear();
            for (Country country : countryList) {
                dataList.add(country.getCountry_name());
            }
            arrayAdapter.notifyDataSetChanged();
            listView.setSelection(0);
            tvCity.setText(selectedCity.getCity_name());
            currentLevel = LEVEL_COUNTRY;
        } else {
            queryFromServer(selectedCity.getCity_code(), "country");
        }
    }

    /**
     * 如果数据没有导入数据库则从weather端口添加数据至数据库再回到主线程执行queryXXX()方法
     */
    private void queryFromServer(String code, final String type) {
        final String address;
        boolean result = false;
        if (!TextUtils.isEmpty(code)) {
            address = "http://www.weather.com.cn/data/list3/city" + code + ".xml";
        } else {
            address = "http://www.weather.com.cn/data/list3/city.xml";
        }
        //下载进度显示
        showProgressDialog();
        //开始加载数据,并通过callback回调在UI线程显示出来
        HttpUtil.doAsynGet(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ChoiceCityActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException{
                boolean result = false;
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response); //判断响应
                String myResponse = null;
                try {
                    myResponse = response.body().string();  //获取响应体
                    Log.e(TAG, "onResponse: " + myResponse);
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if (response != null){
                        response.close();
                    }
                }
                //将加载的数据保存至数据库
                if ("province".equals(type)) {
                    result = OkHttpResponseUtil.handleProvincesResponse(locationDB, myResponse);
                } else if ("city".equals(type)) {
                    result = OkHttpResponseUtil.handlerCityResponse(locationDB, myResponse, Integer.parseInt(selectedProvince.getProvince_code()));
                } else if ("country".equals(type)) {
                    result = OkHttpResponseUtil.handlerCountiesResponse(locationDB, myResponse, Integer.parseInt(selectedCity.getCity_code()));
                }
                if (result) {
                    //回到主线程处理逻辑
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type)) {
                                queryProvinces();
                            } else if ("city".equals(type)) {
                                queryCities();
                            } else if ("country".equals(type)) {
                                queryCounties();
                            }
                        }
                    });
                }
            }
        });


    }

    /**
     * 显示进度条
     */
    private void showProgressDialog() {
        if (this.progressDialog == null) {
            this.progressDialog = new ProgressDialog(ChoiceCityActivity.this);
            this.progressDialog.setMessage("正在加载...");
            this.progressDialog.setCanceledOnTouchOutside(false);
        }
        this.progressDialog.show();
    }

    /**
     * 关闭进度条
     */
    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    /**
     * 捕获BACK键，根据当前等级判断，此时该返回省列表，市列表，还是直接退出
     */
    @Override
    public void onBackPressed() {
        if (currentLevel == LEVEL_COUNTRY) {
            tvCountry.setText("县");
            queryCities();
        } else if (currentLevel == LEVEL_CITY) {
            tvCity.setText("市");
            queryProvinces();
        } else if (currentLevel == LEVEL_PROVINCE) {
            finish();
        }
    }
}
