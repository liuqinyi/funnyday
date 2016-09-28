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
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
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
    private LocationClient mLocationClient; //定位客户端对象
    private LocationClientOption mLocationClientOption; //定位设置对象
    private MyLocationListener mLocationListener; //地位监听器，回调location
    private boolean isFirstLocation = true; //判断是否是第一次初始化地图，用于寻找自己的位置
    private MyDirectionSensorListener myDirectionSensorListener; //方向传感器监听器对象
    private float mCurrentX; //现在的X轴坐标
    private BitmapDescriptor mIconLocation; //定位图标

    //申明两个描叙现在位置经纬度的double变量
    private double mlatitude;
    private double mlongitude;

    /**
     * 添加覆盖物相关
     */
    private BitmapDescriptor mMarker;

    private String addressStr;

    public String getAddressStr() {
        return addressStr;
    }

    public void setAddressStr(String addressStr) {

        this.addressStr = addressStr;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_baidu_map);
        this.context = BaiduMapActivity.this;
        /**
         * 初始化地图
         */
        mMapView = (MapView) findViewById(R.id.baimapView);
        baiduMap = mMapView.getMap();


/*
        //用来控制地图现实比例
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.zoomTo(15.0f);
        baiduMap.setMapStatus(mapStatusUpdate);
*/

        getPersimmions();
        initLocation();
    }

    /**
     * 初始化定位
     */
    private void initLocation() {
        if (mLocationClient == null) {
            mLocationClient = new LocationClient(this);
        }
        mLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mLocationListener); //注册监听器
        //Location设置
        mLocationClientOption = new LocationClientOption();
        mLocationClientOption.setCoorType("bd09ll"); //设置坐标格式
        mLocationClientOption.setIsNeedAddress(true); //设置是否需要获取地址
        mLocationClientOption.setOpenGps(true); //设置打开GPS
        mLocationClientOption.setScanSpan(1000); //设置请求时间间隔
        mLocationClient.setLocOption(mLocationClientOption);

        /**
         * 定位及方向传感器
         */
        mIconLocation = BitmapDescriptorFactory.fromResource(R.drawable.icon_openmap_focuse_mark);
        myDirectionSensorListener = new MyDirectionSensorListener(this);
        myDirectionSensorListener.setOrientation(new MyDirectionSensorListener.OnOrientationListener() {
            @Override
            public void onOrientationChanged(float x) {
                mCurrentX = x;
            }
        });

        initMarker();


    }

    private void initMarker() {
        mMarker = BitmapDescriptorFactory.fromResource(R.drawable.icon_openmap_mark);
    }

    /**
     * android 23专属获取权限
     * very nice
     */
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
            if (shouldShowRequestPermissionRationale(permission)) {
                return true;
            } else {
                permissionsList.add(permission);
                return false;
            }

        } else {
            return true;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //允许定位
        baiduMap.setMyLocationEnabled(true);
        //开启定位
        if (!mLocationClient.isStarted()) {
            mLocationClient.start();
        }
        //开启方向传感器
        myDirectionSensorListener.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //不允许定位
        baiduMap.setMyLocationEnabled(false);
        //停止定位
        mLocationClient.stop();
        //关闭方向传感器
        myDirectionSensorListener.stop();
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
        getMenuInflater().inflate(R.menu.map_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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
                if (baiduMap.isTrafficEnabled()) {
                    baiduMap.setTrafficEnabled(false);
                    item.setTitle("实时交通（OFF）");
                } else {
                    baiduMap.setTrafficEnabled(true);
                    item.setTitle("实时交通(ON)");
                }
                break;
            case R.id.map_toMyLocation:
                centerToMyLocation();
                break;
            case R.id.map_addOverlay:
                addOverlays();
        }
        return true;
    }

    /**
     * 添加覆盖物
     */
    private void addOverlays() {
        baiduMap.clear();
        LatLng latLng;
        Marker marker;
        OverlayOptions overlayOptions;

    }

    private class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //通过回调获取定位信息
            MyLocationData locationData = new MyLocationData.Builder()
                    .direction(mCurrentX) // .direction(100) // 此处设置开发者获取到的方向信息，顺时针0-360
                    .accuracy(location.getRadius()) //设置准确性，半径
                    .latitude(location.getLatitude()) //获取纬度
                    .longitude(location.getLongitude()) //获取经度
                    .build();
            //添加方向覆盖物
            MyLocationConfiguration myLocationConfig = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, mIconLocation);
            baiduMap.setMyLocationConfigeration(myLocationConfig);

            //向地图添加定位信息
            baiduMap.setMyLocationData(locationData);
            //获取经纬度
            mlatitude = location.getLatitude();
            mlongitude = location.getLongitude();

            if (isFirstLocation) {
                centerToMyLocation();
                isFirstLocation = false;

                Toast.makeText(context, "我的位置：" + location.getAddrStr(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void centerToMyLocation() {
        LatLng latLng = new LatLng(mlatitude, mlongitude); //获取当前经纬度并封装在latLng对象中
        Log.d(TAG, "onReceiveLocation: latLng = " + latLng);
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(latLng); //地图状态更新
        baiduMap.animateMapStatus(mapStatusUpdate); //跟踪到当前位置
    }
}
