package com.lqy.funnyday.model.map;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.lqy.funnyday.R;

import java.util.ArrayList;

public class BaiduMapActivity extends AppCompatActivity {

    private final int SDK_PERMISSION_REQUEST = 127;
    private static final String TAG = "BaiduMapActivity";
    private Context context;
    private MapView mMapView; //获取地图控件

    private BaiduMap baiduMap; //获取地图对象
    private String permissionInfo;

    /**
     * 定位相关
     */
    private LocationClient mLocationClient;
    private LocationClientOption mLocationClientOption;
    private MyLocationListener mLocationListener;
    private boolean isFirstLocation = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_baidu_map);
        this.context = BaiduMapActivity.this;
        mMapView = (MapView)findViewById(R.id.baimapView);
        baiduMap = mMapView.getMap();
        baiduMap.setMyLocationEnabled(true);

/*
        //用来控制地图现实比例
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.zoomTo(15.0f);
        baiduMap.setMapStatus(mapStatusUpdate);
*/

        getPersimmions();
        initLocation();
    }

    private void initLocation() {
        mLocationClient = new LocationClient(this);
        if (mLocationClient == null){
            mLocationListener = new MyLocationListener();
        }
        mLocationClient.registerLocationListener(mLocationListener); //注册监听器
        //Location设置
        mLocationClientOption = new LocationClientOption();
        mLocationClientOption.setCoorType("bd09ll"); //设置坐标格式
        mLocationClientOption.setIsNeedAddress(true); //设置是否需要获取地址
        mLocationClientOption.setOpenGps(true); //设置打开GPS
        mLocationClientOption.setScanSpan(1000); //设置请求时间间隔
        mLocationClient.setLocOption(mLocationClientOption);

    }

    private void getPersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            /***
             * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
             */
            // 定位精确位置
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
			/*
			 * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
			 */
            // 读写权限
            if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
            }
            // 读取电话状态权限
            if (addPermission(permissions, Manifest.permission.READ_PHONE_STATE)) {
                permissionInfo += "Manifest.permission.READ_PHONE_STATE Deny \n";
            }

            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean addPermission(ArrayList<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) { // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
            if (shouldShowRequestPermissionRationale(permission)){
                return true;
            }else{
                permissionsList.add(permission);
                return false;
            }

        }else{
            return true;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //允许定位
        baiduMap.setMyLocationEnabled(true);
        //开启定位
        if (!mLocationClient.isStarted()){
            mLocationClient.start();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //不允许定位
        baiduMap.setMyLocationEnabled(false);
        //停止定位
        mLocationClient.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.map_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.map_commmon:
                //普通地图
                baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                break;
            case R.id.map_site:
                //卫星地图
                baiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.map_traffic:
                //实时交通
                if (baiduMap.isTrafficEnabled()){
                    baiduMap.setTrafficEnabled(false);
                    item.setTitle("实时交通（OFF）");
                }else{
                    baiduMap.setTrafficEnabled(true);
                    item.setTitle("实时交通(ON)");
                }
                break;
        }
        return true;
    }

    private class MyLocationListener implements BDLocationListener{

        @Override
        public void onReceiveLocation(BDLocation location) {
            //通过回调获取定位信息
            MyLocationData locationData = new MyLocationData.Builder()
                    .accuracy(location.getRadius()) //设置准确性，半径
                   // .direction(100) // 此处设置开发者获取到的方向信息，顺时针0-360
                    .latitude(location.getLatitude()) //获取纬度
                    .longitude(location.getLongitude()) //获取经度
                    .build();
            //MyLocationConfiguration myLocationConfig = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL,);

            //向地图添加定位信息
            baiduMap.setMyLocationData(locationData);

            if (isFirstLocation){
                LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude()); //获取当前经纬度并封装在latLng对象中
                Log.d(TAG, "onReceiveLocation: latLng = " + latLng );
                MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(latLng); //地图状态更新
                baiduMap.setMapStatus(mapStatusUpdate); //跟踪到当前位置
                isFirstLocation = false;

                Toast.makeText(context,"我的位置："+ location.getAddrStr(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
