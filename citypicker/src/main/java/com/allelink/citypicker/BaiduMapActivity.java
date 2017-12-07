package com.allelink.citypicker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;

import static com.baidu.mapapi.map.BitmapDescriptorFactory.*;

public class BaiduMapActivity extends AppCompatActivity {

    private TextureMapView mMapView;
    private BaiduMap mBaiduMap;

    //定位相关变量定义
    private LocationClient mLocationClient;
    private boolean isFirstIn=true;
    private BDLocation mLastLocationData=new BDLocation();
    private MyLocationListener myLocationListener;
    private BitmapDescriptor mIconLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_baidu_map);
        mMapView=findViewById(R.id.mTexturemap);
        mBaiduMap=mMapView.getMap();
        //定位
        initLocation();
    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onPause();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    //定位相关函数
    private void initLocation() {
        mLocationClient=new LocationClient(getApplicationContext());
        myLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myLocationListener);
        mIconLocation= fromResource(R.drawable.ic_place);
        LocationClientOption option=new LocationClientOption();
        option.setCoorType("BD09LL");
        option.setIsNeedAddress(true);
        option.setScanSpan(1000);
        mLocationClient.setLocOption(option);
        MyLocationConfiguration config=new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL,true,mIconLocation);
        mBaiduMap.setMyLocationConfiguration(config);
    }
    private class  MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            mLastLocationData=bdLocation;
            MyLocationData myLocationData=new MyLocationData.Builder()
                    .accuracy(bdLocation.getRadius())
                    .latitude(bdLocation.getLatitude())
                    .longitude(bdLocation.getLongitude())
                    .build();
            mBaiduMap.setMyLocationData(myLocationData);
            MapStatusUpdate msu1 = MapStatusUpdateFactory.zoomTo(15.0f);
            mBaiduMap.setMapStatus(msu1);

            if(isFirstIn){
                LatLng ll=new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude());
                MapStatusUpdate msu=MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.setMapStatus(msu);
                isFirstIn=false;
            }
        }
    }
}
