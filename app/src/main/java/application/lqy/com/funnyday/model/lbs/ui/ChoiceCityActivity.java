package application.lqy.com.funnyday.model.lbs.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.lqy.greendao.City;
import com.lqy.greendao.Country;
import com.lqy.greendao.Province;

import java.util.List;

import application.lqy.com.funnyday.R;
import application.lqy.com.funnyday.db.WeatherDB;

/**
 * Created by mrliu on 16-7-23.
 */
public class ChoiceCityActivity extends AppCompatActivity{

    private ListView listView;

    private TextView tvProvince,tvCity,tvCountry;

    private List<Province> provinceList;

    private List<City> cityList;

    private List<Country> countryList;

    private List<String> regionNameList;

    private WeatherDB weatherDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_city);

        initView();
    }

    private void initView() {
        listView = (ListView)findViewById(R.id.lv_city);
        weatherDB = new WeatherDB(this);
        provinceList = weatherDB.loadProvince();
        for (Province province : provinceList){
            String provinceName = province.getProvince_name();
            regionNameList.add(provinceName);
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,regionNameList);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

    }

}
