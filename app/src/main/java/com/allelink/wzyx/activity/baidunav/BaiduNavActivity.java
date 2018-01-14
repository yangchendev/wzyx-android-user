package com.allelink.wzyx.activity.baidunav;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.allelink.wzyx.R;
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
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.navisdk.adapter.BNCommonSettingParam;
import com.baidu.navisdk.adapter.BNOuterTTSPlayerCallback;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNaviSettingManager;
import com.baidu.navisdk.adapter.BaiduNaviManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BaiduNavActivity extends AppCompatActivity {

    private Context context;
    //地图相关
    private TextureMapView mMapView;
    private BaiduMap mBaiduMap;
    //定位相关
    private LocationClient mLocationClient;
    private boolean isFirstIn = true;
    private LatLng mLastLocationData;
    private LatLng mDestLocationData;
    private MyLocationListener myLocationListener;
    private BitmapDescriptor mIconLocation;
    private MyOrientationListener myOrientationListener;
    private float mCurrentx;

    //导航相关
    private final static String authBaseArr[] =
            {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION};
    private final static String authComArr[] = {Manifest.permission.READ_PHONE_STATE};
    private final static int authBaseRequestCode = 1;
    private String mSDCardPath = null;
    private static final String APP_FOLDER_NAME = "WZYX";
    public static final String ROUTE_PLAN_NODE = "routePlanNode";
    private final static int authComRequestCode = 2;
    private boolean hasRequestComAuth = false;
    private boolean hasInitSuccess = false;
    private BDLocation myLocation;

    private Button baidunav1;
    private FloatingActionButton navBtn = null;
    private static final String TAG = "BaiduNavActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getApplicationContext();
        SDKInitializer.initialize(context);
        setContentView(R.layout.activity_baidu_nav);
        //隐藏actionBar
        android.app.ActionBar actionBar = getActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        mMapView = findViewById(R.id.tmp_mTexturemap);
        mBaiduMap = mMapView.getMap();
        //开启定位
        mBaiduMap.setMyLocationEnabled(true);
        navBtn = findViewById(R.id.fab_activity_baidu_nav);
        initLocation();
        initLatLong();
//        mBaiduMap.setOnMapLongClickListener(new BaiduMap.OnMapLongClickListener() {
//
//            @Override
//            public void onMapLongClick(LatLng latLng) {
//                // Toast.makeText(MainActivity.this,bdLocation1.getAddrStr(), Toast.LENGTH_SHORT).show();
//                mDestLocationData = latLng;
//                addDestInfoOverlay(latLng);
//            }
//        });

        if (initDirs()) {
            initNavi();

        }
        //开始导航
        baidunav1=findViewById(R.id.btn_baidu_nav);
        baidunav1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDestLocationData == null) {
                    Toast.makeText(BaiduNavActivity.this, "请设置目标地点", Toast.LENGTH_SHORT).show();
                }
                else {
                    routeplanToNavi(false);}
            }
        });
        navBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDestLocationData == null) {
                    Toast.makeText(BaiduNavActivity.this, "请设置目标地点", Toast.LENGTH_SHORT).show();
                }
                else {
                    routeplanToNavi(true);}
            }
        });

    }

    /**
     * 获得活动经度纬度
     */
    private void initLatLong() {
        Intent intent=getIntent();
       // mDestLocationData.longitude= Double.parseDouble(intent.getStringExtra("longtitude"));
        mDestLocationData=new LatLng(intent.getDoubleExtra("latitude",29.895100),intent.getDoubleExtra("longitude",121.647300));
        addDestInfoOverlay(mDestLocationData);
    }

    private BDLocation GCJ2BD(BDLocation mlocation) {
        return LocationClient.getBDLocationInCoorType(mlocation, BDLocation.BDLOCATION_GCJ02_TO_BD09LL);
    }



    //基础地图的显示
    @Override
    protected void onStart() {
        super.onStart();
        mLocationClient.start();
        myOrientationListener.start();
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
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mLocationClient.stop();
        mMapView.onDestroy();
        mBaiduMap.setMyLocationEnabled(false);
        mLocationClient.stop();
        myOrientationListener.stop();
    }


    private void addDestInfoOverlay(LatLng latLng1) {
        mBaiduMap.clear();
        OverlayOptions options = new MarkerOptions().position(latLng1)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_place))
                .zIndex(5);
        mBaiduMap.addOverlay(options);
    }

    //定位相关
    private void initLocation() {
        mLocationClient = new LocationClient(getApplicationContext());
        myLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myLocationListener);
        mIconLocation = BitmapDescriptorFactory.fromResource(R.drawable.ic_navigation);
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("BD09LL");
        option.setIsNeedAddress(true);
        //option.setScanSpan(10000);
        mLocationClient.setLocOption(option);
        MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, mIconLocation);
        mBaiduMap.setMyLocationConfiguration(config);
        myOrientationListener = new MyOrientationListener(context);
        myOrientationListener.setMonOrientationListener(new MyOrientationListener.onOrientationListener() {
            @Override
            public void onOrientationChanged(float x) {
                mCurrentx = x;
            }
        });
    }

    private class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {

            myLocation = bdLocation;
            mLastLocationData = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
            MyLocationData myLocationData = new MyLocationData.Builder()
                    .direction(mCurrentx)
                    .accuracy(bdLocation.getRadius())
                    .latitude(bdLocation.getLatitude())
                    .longitude(bdLocation.getLongitude())
                    .build();
            mBaiduMap.setMyLocationData(myLocationData);
            MapStatusUpdate msu1 = MapStatusUpdateFactory.zoomTo(15.0f);
            mBaiduMap.setMapStatus(msu1);

            if (isFirstIn) {
                LatLng ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.setMapStatus(msu);
                isFirstIn = false;
                Toast.makeText(BaiduNavActivity.this, bdLocation.getAddrStr(), Toast.LENGTH_SHORT).show();
            }
        }
    }



    //导航相关
    private boolean hasBasePhoneAuth() {
        // TODO Auto-generated method stub
        PackageManager pm = this.getPackageManager();
        for (String auth : authBaseArr) {
            if (pm.checkPermission(auth, this.getPackageName()) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private boolean hasCompletePhoneAuth() {
        // TODO Auto-generated method stub
        PackageManager pm = this.getPackageManager();
        for (String auth : authComArr) {
            if (pm.checkPermission(auth, this.getPackageName()) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    String authinfo = null;
    /**
     * 内部TTS播报状态回传handler
     */
    private  Handler ttsHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int type = msg.what;
            switch (type) {
                case BaiduNaviManager.TTSPlayMsgType.PLAY_START_MSG: {
                    // showToastMsg("Handler : TTS play start");
                    break;
                }
                case BaiduNaviManager.TTSPlayMsgType.PLAY_END_MSG: {
                    // showToastMsg("Handler : TTS play end");
                    break;
                }
                default:
                    break;
            }
        }
    };

    /**
     * 内部TTS播报状态回调接口
     */
    private BaiduNaviManager.TTSPlayStateListener ttsPlayStateListener = new BaiduNaviManager.TTSPlayStateListener() {

        @Override
        public void playEnd() {
            // showToastMsg("TTSPlayStateListener : TTS play end");
        }

        @Override
        public void playStart() {
            // showToastMsg("TTSPlayStateListener : TTS play start");
        }
    };

    private void initNavi() {
        BNOuterTTSPlayerCallback ttsCallback = null;
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            if (!hasBasePhoneAuth()) {
                this.requestPermissions(authBaseArr, authBaseRequestCode);
                return;
            }
        }
        BaiduNaviManager.getInstance().init(this, mSDCardPath, APP_FOLDER_NAME,
                new BaiduNaviManager.NaviInitListener() {
                    @Override
                    public void onAuthResult(int status, String msg) {
                        if (0 == status) {
                            authinfo = "key校验成功!";
                        } else {
                            authinfo = "key校验失败, " + msg;
                        }
                        Log.d(TAG, "onAuthResult: " + authinfo);
                        BaiduNavActivity.this.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                Toast.makeText(BaiduNavActivity.this, authinfo, Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    @Override
                    public void initSuccess() {
                        //Toast.makeText(BaiduNavActivity.this, "百度导航引擎初始化成功", Toast.LENGTH_SHORT).show();
                        hasInitSuccess = true;
                        initSetting();
                    }

                    @Override
                    public void initStart() {
                        //Toast.makeText(BaiduNavActivity.this, "百度导航引擎初始化开始", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void initFailed() {
                        Toast.makeText(BaiduNavActivity.this, "百度导航引擎初始化失败", Toast.LENGTH_SHORT).show();
                    }
                    //  }, null /*mTTSCallback*/);
                }, ttsCallback, ttsHandler, ttsPlayStateListener);
    }

    private void initSetting() {
        // BNaviSettingManager.setDayNightMode(BNaviSettingManager.DayNightMode.DAY_NIGHT_MODE_DAY);
        BNaviSettingManager
                .setShowTotalRoadConditionBar(BNaviSettingManager.PreViewRoadCondition.ROAD_CONDITION_BAR_SHOW_ON);
        BNaviSettingManager.setVoiceMode(BNaviSettingManager.VoiceMode.Veteran);
        // BNaviSettingManager.setPowerSaveMode(BNaviSettingManager.PowerSaveMode.DISABLE_MODE);
        BNaviSettingManager.setRealRoadCondition(BNaviSettingManager.RealRoadCondition.NAVI_ITS_ON);
        BNaviSettingManager.setIsAutoQuitWhenArrived(true);
        Bundle bundle = new Bundle();
        // 必须设置APPID，否则会静音
        bundle.putString(BNCommonSettingParam.TTS_APP_ID, "10483259");
        BNaviSettingManager.setNaviSdkParam(bundle);
    }

    private boolean initDirs() {
        mSDCardPath = getSdcardDir();
        if (mSDCardPath == null) {
            return false;
        }
        File f = new File(mSDCardPath, APP_FOLDER_NAME);
        if (!f.exists()) {
            try {
                f.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    private String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }

    private void routeplanToNavi(Boolean type1) {
        if (!hasInitSuccess) {
            Toast.makeText(BaiduNavActivity.this, "还未初始化!", Toast.LENGTH_SHORT).show();
        }
        // 权限申请
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            // 保证导航功能完备
            if (!hasCompletePhoneAuth()) {
                if (!hasRequestComAuth) {
                    hasRequestComAuth = true;
                    this.requestPermissions(authComArr, authComRequestCode);
                    return;
                } else {
                    Toast.makeText(BaiduNavActivity.this, "没有完备的权限!", Toast.LENGTH_SHORT).show();
                }
            }
        }
        BNRoutePlanNode.CoordinateType coType = BNRoutePlanNode.CoordinateType.GCJ02;
        BNRoutePlanNode sNode = null;
        BNRoutePlanNode eNode = null;

        BDLocation myLastLocation = new BDLocation();
        myLastLocation.setLongitude(mLastLocationData.longitude);
        myLastLocation.setLatitude(mLastLocationData.latitude);
        myLastLocation = bd2gcj(myLastLocation);
        BDLocation myDestLocation = new BDLocation();
        myDestLocation.setLongitude(mDestLocationData.longitude);
        myDestLocation.setLatitude(mDestLocationData.latitude);
        myDestLocation = bd2gcj(myDestLocation);
        sNode = new BNRoutePlanNode(myLastLocation.getLongitude(), myLastLocation.getLatitude(),
                "起始地点", null, coType);
        eNode = new BNRoutePlanNode(myDestLocation.getLongitude(), myDestLocation.getLatitude(),
                "目的地", null, coType);
        //  sNode = new BNRoutePlanNode(116.30142, 40.05087, "百度大厦", null, coType);
        //   eNode = new BNRoutePlanNode(116.39750, 39.90882, "北京天安门", null, coType);
        if (sNode != null && eNode != null) {
            List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
            list.add(sNode);
            list.add(eNode);
            BaiduNaviManager.getInstance().launchNavigator(this, list, 1, type1, new DemoRoutePlanListener(sNode));
        }

    }

    private BDLocation bd2gcj(BDLocation mlocation) {
        return LocationClient.getBDLocationInCoorType(mlocation, BDLocation.BDLOCATION_BD09LL_TO_GCJ02);
    }

    public class DemoRoutePlanListener implements BaiduNaviManager.RoutePlanListener {

        private BNRoutePlanNode mBNRoutePlanNode = null;

        public DemoRoutePlanListener(BNRoutePlanNode node) {
            mBNRoutePlanNode = node;
        }

        @Override
        public void onJumpToNavigator() {

            Intent intent = new Intent(BaiduNavActivity.this, BNDemoGuideActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ROUTE_PLAN_NODE, (BNRoutePlanNode) mBNRoutePlanNode);
            intent.putExtras(bundle);
            startActivity(intent);
        }

        @Override
        public void onRoutePlanFailed() {
            // TODO Auto-generated method stub
            //Toast.makeText(MainActivity.this, "算路失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // TODO Auto-generated method stub
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == authBaseRequestCode) {
            for (int ret : grantResults) {
                if (ret == 0) {
                    continue;
                } else {
                    Toast.makeText(BaiduNavActivity.this, "缺少导航基本的权限!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            initNavi();
        } else if (requestCode == authComRequestCode) {
            for (int ret : grantResults) {
                if (ret == 0) {
                    continue;
                }
            }
            //routeplanToNavi(mCoordinateType);
        }

    }


}
