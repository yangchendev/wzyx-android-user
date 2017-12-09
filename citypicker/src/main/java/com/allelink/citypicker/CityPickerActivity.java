package com.allelink.citypicker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.allelink.citypicker.adapter.CityListAdapter;
import com.allelink.citypicker.adapter.ResultListAdapter;
import com.allelink.citypicker.db.DBManager;
import com.allelink.citypicker.model.City;
import com.allelink.citypicker.model.LocateState;
import com.allelink.citypicker.view.SideLetterBar;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;

import java.util.List;

import qiu.niorgai.StatusBarCompat;

/**
 * 城市选择
 * @author yangc
 * @date 2017/12/8
 * @version 1.0
 * @email 1048027353@qq.com
 */

public class CityPickerActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String KEY_PICKED_CITY = "picked_city";
    public static final String KEY_LAT = "lat";
    public static final String KEY_LNG = "lng";
    private static final String TAG = "CityPickerActivity";
    /**
    * TODO
    */
    private boolean locateSuccess = false;
    private ListView mListView;
    private ListView mResultListView;
    private SideLetterBar mLetterBar;
    private EditText searchBox;
    private ImageView clearBtn;
    private TextView cancelBtn;
    private ImageView mBack;
    private TextView mLocationTips;
    private ViewGroup emptyView;

    private CityListAdapter mCityAdapter;
    private ResultListAdapter mResultAdapter;
    private List<City> mAllCities;
    private List<City> mResultCities;
    private DBManager dbManager;


    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();
    private double latitude;
    private double longitude;
    private String city = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化百度地图SDK
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.cp_activity_city_list);
        StatusBarCompat.setStatusBarColor(this,getResources().getColor(R.color.white));
        //隐藏actionBar
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        initData();
        initView();
        //声明LocationClient类
        mLocationClient = new LocationClient(getApplicationContext());
        //注册监听函数
        mLocationClient.registerLocationListener(myListener);
        initLocation();

    }
    private void initData() {
        dbManager = new DBManager(this);
        dbManager.copyDBFile();
        mAllCities = dbManager.getAllCities();
        mCityAdapter = new CityListAdapter(this, mAllCities);
        mCityAdapter.setOnCityClickListener(new CityListAdapter.OnCityClickListener() {
            @Override
            public void onCityClick(String name) {
                backWithData(name);
            }

            @Override
            public void onLocateClick() {
                mCityAdapter.updateLocateState(LocateState.SUCCESS, city);
            }
        });

        mResultAdapter = new ResultListAdapter(this, null);
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.listview_all_city);
        mListView.setAdapter(mCityAdapter);

        mLocationTips = findViewById(R.id.tv_located_city);
        TextView overlay = (TextView) findViewById(R.id.tv_letter_overlay);
        mLetterBar = (SideLetterBar) findViewById(R.id.side_letter_bar);
        mLetterBar.setOverlay(overlay);
        mLetterBar.setOnLetterChangedListener(new SideLetterBar.OnLetterChangedListener() {
            @Override
            public void onLetterChanged(String letter) {
                int position = mCityAdapter.getLetterPosition(letter);
                mListView.setSelection(position);
            }
        });

        searchBox = (EditText) findViewById(R.id.et_search);
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String keyword = s.toString();
                if (TextUtils.isEmpty(keyword)) {
                    clearBtn.setVisibility(View.GONE);
                    emptyView.setVisibility(View.GONE);
                    mResultListView.setVisibility(View.GONE);
                } else {
                    clearBtn.setVisibility(View.VISIBLE);
                    mResultListView.setVisibility(View.VISIBLE);
                    mResultCities = dbManager.searchCity(keyword);
                    if (mResultCities == null || mResultCities.size() == 0) {
                        emptyView.setVisibility(View.VISIBLE);
                    } else {
                        emptyView.setVisibility(View.GONE);
                        mResultAdapter.changeData(mResultCities);
                    }
                }
            }
        });

        emptyView = (ViewGroup) findViewById(R.id.empty_view);
        mResultListView = (ListView) findViewById(R.id.listview_search_result);
        mResultListView.setAdapter(mResultAdapter);
        mResultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                backWithData(mResultAdapter.getItem(position).getName());
            }
        });

        clearBtn = (ImageView) findViewById(R.id.iv_search_clear);
        cancelBtn = (TextView) findViewById(R.id.tv_search_cancel);
        mBack = findViewById(R.id.iv_search_back);

        clearBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        mBack.setOnClickListener(this);
    }

    private void backWithData(String city){
        backWithData(city,latitude,longitude);
    }
    private void backWithData(String city,double lat,double lng){
        Intent data = new Intent();
        data.putExtra(KEY_PICKED_CITY, city);
        data.putExtra(KEY_LAT, lat);
        data.putExtra(KEY_LNG, lng);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_search_clear) {
            searchBox.setText("");
            clearBtn.setVisibility(View.GONE);
            emptyView.setVisibility(View.GONE);
            mResultListView.setVisibility(View.GONE);
            mResultCities = null;
        } else if (i == R.id.tv_search_cancel) {
            finish();
        }else if(i == R.id.iv_search_back){
            if(locateSuccess){
                if(city != null){
                    backWithData(city.substring(0,city.length()-1),latitude,longitude);
                }else{
                    finish();
                }
            }else {
                finish();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mLocationClient.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
        mLocationClient.unRegisterLocationListener(myListener);
    }


    /**
    * 定位初始化
    */
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("BD09LL");
        option.setIsNeedAddress(true);
        option.setScanSpan(1000);
        mLocationClient.setLocOption(option);
    }

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location){
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            city = location.getCity();
            if(city != null){
                //更新定位状态
                mCityAdapter.updateLocateState(LocateState.SUCCESS,city.substring(0,city.length()-1));
                locateSuccess = true;
            }else {
                mCityAdapter.updateLocateState(LocateState.FAILED,null);
                locateSuccess = false;
            }
            Log.d(TAG, city);
        }
    }

    @Override
    public void onBackPressed() {

        if(locateSuccess){
            if(city != null){
                backWithData(city.substring(0,city.length()-1),latitude,longitude);
            }else{
                finish();
            }
        }else {
            finish();
        }

    }
}
