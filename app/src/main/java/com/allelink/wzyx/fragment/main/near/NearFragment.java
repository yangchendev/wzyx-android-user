package com.allelink.wzyx.fragment.main.near;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.allelink.citypicker.CityPickerActivity;
import com.allelink.wzyx.R;
import com.allelink.wzyx.activity.ActivityInfoActivity;
import com.allelink.wzyx.adapter.ActivityItemAdapter;
import com.allelink.wzyx.app.WzyxApplication;
import com.allelink.wzyx.app.activityinfo.ActivityInfoHandler;
import com.allelink.wzyx.app.activityinfo.GetActivityInfoListener;
import com.allelink.wzyx.model.ActivityItem;
import com.allelink.wzyx.net.RestConstants;
import com.allelink.wzyx.ui.loader.WzyxLoader;
import com.allelink.wzyx.utils.log.LogUtil;
import com.allelink.wzyx.utils.toast.ToastUtil;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * @author yangc
 * @version 1.0
 * @filename NearFragment
 * @date 2017/11/22
 * @description 附近 fragment
 * @email 1048027353@qq.com
 */

public class NearFragment extends SupportFragment {
    private static final String TAG = "NearFragment";
    private static final String ACTIVITY_ID = "activityId";
    private static final int REQUEST_CODE_PICK_CITY = 4001;
    private static final int SHOW = 1;
    private static final int HIDE = 2;
    private boolean isFirstLoading = true;
    private static int REFRESH = 3;
    private static int LOAD_MORE = 4;
    /**
    * 排序类型
    */
    private static final int COMPREHENSIVE_ORDERING = 0;
    private static final int COST_ORDERING = 1;
    private static final int DISTANCE_ORDERING = 2;
    /**
    * tab标签数据
    */
    private String[] mTabTitles = new String[]{
            "所有类型",
            "外语培训",
            "音乐培训",
            "美术培训"
    };
    /**
    * 各种类型
    */
    private static final int ALL_TYPE = 0;
    private static final int FOREIGN_LANGUAGE_TYPE = 1;
    private static final int MUSIC_TYPE = 2;
    private static final int ART_TYPE = 3;
    /**
    * 用户选取的活动类型 默认为所有类型
    */
    private int mSelectedActivityType = ALL_TYPE;
    /**
    * 各种排序类型
    */
    private static final int COMPREHENSIVE_RANKING_TYPE = 0;
    private static final int DISTANCE_ASC_TYPE = 1;
    private static final int COST_ASC_TYPE = 2;
    private static final int COST_DESC_TYPE = 3;
    /**
    * 用户选择的排序类型
    */
    private int mSelectedRankingType = COMPREHENSIVE_RANKING_TYPE;
    private Unbinder mUnbinder = null;
    /**
    * UI
    */
    @BindView(R.id.tab_fragment_near_tab_layout)
    TabLayout tabLayout = null;
    @BindView(R.id.tv_fragment_near_comprehensive_ordering)
    AppCompatTextView tvComprehensivOrdering = null;
    @BindView(R.id.tv_fragment_near_cost_order)
    AppCompatTextView tvCostOrder = null;
    @BindView(R.id.tv_fragment_near_distance_order)
    AppCompatTextView tvDistanceOrder = null;
    @BindView(R.id.iv_fragment_near_cost_up_arrow)
    ImageView ivCostUpArrow = null;
    @BindView(R.id.iv_fragment_near_cost_down_arrow)
    ImageView ivCostDownArrow = null;
    @BindView(R.id.rv_fragment_near_activity_list)
    RecyclerView rvActivityList = null;
    @BindView(R.id.tv_fragment_near_position)
    AppCompatTextView tvPosition = null;
    @BindView(R.id.include_locate_failure)
    RelativeLayout layoutLocateFailure = null;
    @BindView(R.id.srl_fragment_near)
    SmartRefreshLayout refreshLayout = null;
    /**
    * 适配器
    */
    private ActivityItemAdapter mAdapter = null;
    /**
    * 布局管理器
    */
    private LinearLayoutManager mLayoutManager = null;
    /**
    * 存放活动信息的集合
    */
    private List<ActivityItem> mActivityItems = new ArrayList<>();
    /**
    * 费用排序选择
    */
    private enum Arrow{
        NORMAL,
        UP,
        DOWM
    }
    private Arrow mCostArrow = Arrow.NORMAL;

    private int mCostCount = 0;
    /**
    * 定位相关
    */
    private double lat = 0;
    private double lng = 0;
    private String city = null;
    private LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();

    public static NearFragment newInstance() {
        Bundle args = new Bundle();
        NearFragment fragment = new NearFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //初始化百度地图SDK
        SDKInitializer.initialize(WzyxApplication.getContext());
        View view = inflater.inflate(R.layout.fragment_near, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initTabData();
        handleTabEvent();
        initData();
        bindAdapter();
        initItemEvent();
        //声明LocationClient类
        mLocationClient = new LocationClient(WzyxApplication.getContext());
        //注册监听函数
        mLocationClient.registerLocationListener(myListener);
        initLocation();
        initRefresh();
    }
    /**
    * 初始化下拉刷新事件
    */
    private void initRefresh() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                if(isFirstLoading){
                    getActivityList(lat,lng,refreshlayout,REFRESH);
                    isFirstLoading = false;
                }else{
                    getActivityListByType(mSelectedActivityType,mSelectedRankingType,lat,lng,refreshlayout,REFRESH);
                }
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                if(isFirstLoading){
                    getActivityList(lat,lng,refreshlayout,LOAD_MORE);
                    isFirstLoading = false;
                }else{
                    getActivityListByType(mSelectedActivityType,mSelectedRankingType,lat,lng,refreshlayout,LOAD_MORE);
                }
            }
        });

    }

    /**
    * 列表项点击事件
    */
    private void initItemEvent() {
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //进入活动详情页面
                Intent intent = new Intent(_mActivity, ActivityInfoActivity.class);
                ActivityItem activityItem = (ActivityItem) adapter.getItem(position);
                intent.putExtra(ACTIVITY_ID, activityItem.getActivityId());
                startActivity(intent);
            }
        });
    }

    /**
    * 初始化列表数据
    */
    private void initData() {
        mAdapter = new ActivityItemAdapter(R.layout.item_fragment_near_acitvity, mActivityItems);
        mLayoutManager = new LinearLayoutManager(_mActivity);
    }

    /**
    * 将recyclerview和adapter绑定
    */
    private void bindAdapter() {
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvActivityList.setLayoutManager(mLayoutManager);
        rvActivityList.setAdapter(mAdapter);
        mAdapter.bindToRecyclerView(rvActivityList);
        mAdapter.setEmptyView(R.layout.fragment_near_empty);
        //generateTestData();
    }

    /**
    * 处理tab的切换事件
    */
    private void handleTabEvent() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //根据选择确定所选类型
                if(mTabTitles[0].equals(tab.getText())){
                    mSelectedActivityType = ALL_TYPE;
                }else if(mTabTitles[1].equals(tab.getText())){
                    mSelectedActivityType = FOREIGN_LANGUAGE_TYPE;
                }else if(mTabTitles[2].equals(tab.getText())){
                    mSelectedActivityType = MUSIC_TYPE;
                }else if(mTabTitles[3].equals(tab.getText())){
                    mSelectedActivityType = ART_TYPE;
                }
                mSelectedRankingType = COMPREHENSIVE_RANKING_TYPE;
                //清除样式
                clearSortStyle(COMPREHENSIVE_ORDERING);
                clearSortStyle(DISTANCE_ORDERING);
                clearSortStyle(COST_ORDERING);
                //设置综合样式
                tvComprehensivOrdering.setTextColor(getResources().getColor(R.color.brands_color));
                LogUtil.d(TAG,"lat: "+lat+"\n"+"lng: "+lng);
                if(lat == 0 || lng == 0){
                    ToastUtil.toastShort(_mActivity,getResources().getString(R.string.get_activity_list_failure));
                }else{
                    getActivityListByType(mSelectedActivityType,mSelectedRankingType,lat,lng);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    /**
     * 根据类型获取活动信息
     * @param activityType 活动类型
     * @param rankingType  排序类型
     * @param lat 纬度
     * @param lng 经度
     */
    private void getActivityListByType(int activityType, int rankingType, double lat, double lng) {
        WzyxLoader.showLoading(_mActivity);
        HashMap<String, Object> params = new HashMap<>();
        //现阶段不需要
        //params.put("searchContent", "搜索内容");
        params.put("distance","20000");
        //现阶段需要
        params.put("lat", lat);
        params.put("lng", lng);
        params.put("activityType", activityType);
        params.put("sortParams", rankingType);
        //网络请求获取数据
        ActivityInfoHandler.getActivityList(params, RestConstants.GET_ACTIVITY_INFO_LIST_URL
                , new GetActivityInfoListener() {
                    @Override
                    public void onSuccess(List<ActivityItem> activityItems) {
                        mActivityItems.clear();
                        mActivityItems = activityItems;
                        mAdapter.replaceData(mActivityItems);
                        WzyxLoader.stopLoading();
                    }
                    @Override
                    public void onFailure(String errorMessage) {
                        mActivityItems.clear();
                        mAdapter.replaceData(mActivityItems);
                        WzyxLoader.stopLoading();
                        ToastUtil.toastShort(_mActivity,getResources().getString(R.string.get_activity_list_failure));
                    }
                });
    }
    /**
     * 根据类型获取活动信息
     * @param activityType 活动类型
     * @param rankingType  排序类型
     * @param lat 纬度
     * @param lng 经度
     * @param refreshlayout 下拉刷新
     */
    private void getActivityListByType(int activityType, int rankingType, double lat, double lng, final RefreshLayout refreshlayout, final int type) {
        HashMap<String, Object> params = new HashMap<>();
        //现阶段不需要
        //params.put("searchContent", "搜索内容");
        params.put("distance","20000");
        //现阶段需要
        params.put("lat", lat);
        params.put("lng", lng);
        params.put("activityType", activityType);
        params.put("sortParams", rankingType);
        //网络请求获取数据
        ActivityInfoHandler.getActivityList(params, RestConstants.GET_ACTIVITY_INFO_LIST_URL
                , new GetActivityInfoListener() {
                    @Override
                    public void onSuccess(List<ActivityItem> activityItems) {
                        mActivityItems.clear();
                        mActivityItems = activityItems;
                        //更新布局
                        if(type == REFRESH){
                            mAdapter.replaceData(mActivityItems);
                            refreshlayout.finishRefresh(1000,true);
                        }else if(type == LOAD_MORE){
                            mAdapter.addData(mActivityItems);
                            refreshlayout.finishLoadmore(1000, true);
                        }
                    }
                    @Override
                    public void onFailure(String errorMessage) {
                        mActivityItems.clear();
                        //更新布局
                        if(type == REFRESH){
                            mAdapter.replaceData(mActivityItems);
                            refreshlayout.finishRefresh(1000,true);
                        }else if(type == LOAD_MORE){
                            mAdapter.addData(mActivityItems);
                            refreshlayout.finishLoadmore(1000, true);
                        }
                    }
                });
    }
    /**
    * 初始化tab标签数据
    */
    private void initTabData() {
        for(int i = 0; i < mTabTitles.length; i++){
            //创建tab
            TabLayout.Tab tab = tabLayout.newTab();
            //设置标题
            tab.setText(mTabTitles[i]);
            //添加tab到tablayout
            tabLayout.addTab(tab);
        }
    }
    /**
    * 获取位置信息
    */
    @OnClick(R.id.ll_fragment_near_position)
    void locate(){
        LogUtil.d(TAG,"locate");
    }
    /**
    * 搜索活动
    */
    @OnClick(R.id.iv_fragment_near_search_bar)
    void searchActivity(){
        LogUtil.d(TAG,"searchActivity");
    }
    /**
    * 进入到地图模式
    */
    @OnClick(R.id.iv_fragment_near_map)
    void mapMode(){
        LogUtil.d(TAG,"mapMode");
    }
    /**
    * 综合排序
    */
    @OnClick(R.id.tv_fragment_near_comprehensive_ordering)
    void comprehensiveOrdering(){
        LogUtil.d(TAG,"comprehensiveOrdering");
        //1.清除样式
        clearSortStyle(COST_ORDERING);
        clearSortStyle(COMPREHENSIVE_ORDERING);
        clearSortStyle(DISTANCE_ORDERING);
        mCostCount = 0;
        //2.设置选中样式
        tvComprehensivOrdering.setTextColor(getResources()
        .getColor(R.color.brands_color));
        //3.进行网络请求获取数据
        mSelectedRankingType = COMPREHENSIVE_RANKING_TYPE;
        if(lat == 0 || lng == 0){
            ToastUtil.toastShort(_mActivity,getResources().getString(R.string.locate_error));
            showAndHideActivityList(HIDE);
        }else{
            showAndHideActivityList(SHOW);
            getActivityListByType(mSelectedActivityType,mSelectedRankingType,lat,lng);
        }
    }
    /**
    * 费用排序
    */
    @OnClick(R.id.ll_fragment_near_cost_ordering)
    void costOrdering(){
        LogUtil.d(TAG,"costOrdering");
        //1.清除样式
        clearSortStyle(COMPREHENSIVE_ORDERING);
        clearSortStyle(DISTANCE_ORDERING);
        clearSortStyle(COST_ORDERING);
        //2.根据CostArrow来控制样式
        mCostArrow = decideState();
        if(mCostArrow == Arrow.DOWM){
            tvCostOrder.setTextColor(getResources().getColor(R.color.brands_color));
            ivCostUpArrow.setImageResource(R.drawable.ic_up_arrow_pressed);
            ivCostDownArrow.setImageResource(R.drawable.ic_down_arrow_nor);
            //3.进行网络请求更新
            mSelectedRankingType = COST_ASC_TYPE;
            if(lat == 0 || lng == 0){
                ToastUtil.toastShort(_mActivity,getResources().getString(R.string.locate_error));
                showAndHideActivityList(HIDE);
            }else{
                showAndHideActivityList(SHOW);
                getActivityListByType(mSelectedActivityType,mSelectedRankingType,lat,lng);
            }
        }else if(mCostArrow == Arrow.UP){
            tvCostOrder.setTextColor(getResources().getColor(R.color.brands_color));
            ivCostUpArrow.setImageResource(R.drawable.ic_up_arrow_nor);
            ivCostDownArrow.setImageResource(R.drawable.ic_down_arrow_pressed);
            //3.进行网络请求更新
            mSelectedRankingType = COST_DESC_TYPE;
            if(lat == 0 || lng == 0){
                ToastUtil.toastShort(_mActivity,getResources().getString(R.string.locate_error));
                showAndHideActivityList(HIDE);
            }else{
                showAndHideActivityList(SHOW);
                getActivityListByType(mSelectedActivityType,mSelectedRankingType,lat,lng);
            }
        }
    }
    /**
    * 判断样式
    */
    private Arrow decideState() {
        mCostCount++;
        if(mCostCount == 1){
            return Arrow.DOWM;
        }
        if(mCostCount == 2){
            return Arrow.UP;
        }
        if(mCostCount == 3){
            mCostCount = 1;
        }
        return Arrow.DOWM;
    }

    /**
    * 距离排序
    */
    @OnClick(R.id.ll_fragment_near_distance_ordering)
    void distanceOrdering(){
        LogUtil.d(TAG,"distanceOrdering");
        //1.清除样式
        clearSortStyle(COMPREHENSIVE_ORDERING);
        clearSortStyle(DISTANCE_ORDERING);
        clearSortStyle(COST_ORDERING);
        mCostCount = 0;
        //2.设置选中样式
        tvDistanceOrder.setTextColor(getResources().getColor(R.color.brands_color));
        mSelectedRankingType = DISTANCE_ASC_TYPE;
        if(lat == 0 || lng == 0){
            ToastUtil.toastShort(_mActivity,getResources().getString(R.string.locate_error));
            showAndHideActivityList(HIDE);
        }else{
            showAndHideActivityList(SHOW);
            getActivityListByType(mSelectedActivityType,mSelectedRankingType,lat,lng);
        }
    }
    /**
    * 定位点击事件
    */
    @OnClick(R.id.ll_fragment_near_position)
    void locatePosition(){
        LogUtil.d(TAG,"locatePosition");
        Intent intent = new Intent(_mActivity, CityPickerActivity.class);
        startActivityForResult(intent,REQUEST_CODE_PICK_CITY);
    }
    /**
    * 清除排序选中的样式
    */
    private void clearSortStyle(int type){
        switch (type){
            case COMPREHENSIVE_ORDERING:
                tvComprehensivOrdering.setTextColor(getResources().getColor(R.color.black_666));
                break;
            case COST_ORDERING:
                tvCostOrder.setTextColor(getResources().getColor(R.color.black_666));
                ivCostDownArrow.setImageResource(R.drawable.ic_down_arrow_nor);
                ivCostUpArrow.setImageResource(R.drawable.ic_up_arrow_nor);
                break;
            case DISTANCE_ORDERING:
                tvDistanceOrder.setTextColor(getResources().getColor(R.color.black_666));
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_PICK_CITY){
            if(resultCode == RESULT_OK){
                //从CityPickerActivity获取位置信息
                tvPosition.setText(data.getStringExtra(CityPickerActivity.KEY_PICKED_CITY));
                lat = data.getDoubleExtra(CityPickerActivity.KEY_LAT, 0);
                lng = data.getDoubleExtra(CityPickerActivity.KEY_LNG, 0);
                LogUtil.d(TAG,"lat: "+lat+"\n"+"lng: "+lng);
                showAndHideActivityList(SHOW);
                getActivityList(lat, lng);
            }else {
                tvPosition.setText(getResources().getString(R.string.locate));
                lat = 0;
                lng = 0;
                //定位失败
                showAndHideActivityList(HIDE);
                ToastUtil.toastShort(_mActivity,getResources().getString(R.string.locate_error));
            }
        }

    }

    /**
     * 通过网络请求获取活动列表
     * @param lat 纬度
     * @param lng 经度
     */
    private void getActivityList(double lat, double lng) {
        //开启加载动画
        WzyxLoader.showLoading(_mActivity);
        HashMap<String, Object> params = new HashMap<>();
        params.put("lat", lat);
        params.put("lng", lng);
        ActivityInfoHandler.getActivityList(params, RestConstants.GET_DEFAULT_ACTIVITY_INFO_LIST_URL,
                new GetActivityInfoListener() {
                    @Override
                    public void onSuccess(List<ActivityItem> activityItems) {
                        mActivityItems.clear();
                        mActivityItems = activityItems;
                        //更新布局
                        mAdapter.replaceData(mActivityItems);
                        //停止加载动画
                        WzyxLoader.stopLoading();
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        //停止加载动画
                        WzyxLoader.stopLoading();
                        ToastUtil.toastShort(_mActivity,getResources().getString(R.string.get_activity_list_failure));
                    }
                });
    }

    /**
     * 通过网络请求获取活动列表
     * @param lat 纬度
     * @param lng 经度
     * @param refreshlayout 下拉刷新
     */
    private void getActivityList(double lat, double lng, final RefreshLayout refreshlayout, final int type) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("lat", lat);
        params.put("lng", lng);
        ActivityInfoHandler.getActivityList(params, RestConstants.GET_DEFAULT_ACTIVITY_INFO_LIST_URL,
                new GetActivityInfoListener() {
                    @Override
                    public void onSuccess(List<ActivityItem> activityItems) {
                        mActivityItems.clear();
                        mActivityItems = activityItems;
                        //更新布局
                        if(type == REFRESH){
                            mAdapter.replaceData(mActivityItems);
                            refreshlayout.finishRefresh(1000,true);
                        }else if(type == LOAD_MORE){
                            mAdapter.addData(mActivityItems);
                            refreshlayout.finishLoadmore(1000, true);
                        }
                    }
                    @Override
                    public void onFailure(String errorMessage) {
                        //停止加载动画
                        mActivityItems.clear();
                        //更新布局
                        if(type == REFRESH){
                            mAdapter.replaceData(mActivityItems);
                            refreshlayout.finishRefresh(1000,true);
                        }else if(type == LOAD_MORE){
                            mAdapter.addData(mActivityItems);
                            refreshlayout.finishLoadmore(1000, true);
                        }
                    }
                });
    }
    private void generateTestData(){
        for(int i = 0; i < 15; i++){
            ActivityItem activityItem = new ActivityItem();
            activityItem.setActivityName("第"+i+"个活动");
            activityItem.setCost(getString(R.string.activity_cost,i*200+""));
            activityItem.setDistance(getString(R.string.activity_distance,i*12+""));
            activityItem.setEnrollNumber(getString(R.string.enroll_number,i*15+""));
            mActivityItems.add(activityItem);
        }
    }
    /**
     * 定位初始化
     */
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("BD09LL");
        option.setIsNeedAddress(true);
        option.setScanSpan(10*60*1000);
        mLocationClient.setLocOption(option);
    }
    /**
    * 定位信息回调
    */
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location){
            lat = location.getLatitude();
            lng = location.getLongitude();
            city = location.getCity();
            if(city != null){
                //定位成功，更新定位状态
                tvPosition.setText(city.substring(0,city.length()-1));
                showAndHideActivityList(SHOW);
                if(isFirstLoading){
                    getActivityList(lat,lng);
                    isFirstLoading = false;
                }else {
                    getActivityListByType(mSelectedActivityType,mSelectedRankingType,lat,lng);
                }

            }else {
                //定位失败
                showAndHideActivityList(HIDE);
            }
            Log.d(TAG, city);
        }
    }
    /**
    * 隐藏或显示活动列表和定位失败界面
    */
    private void showAndHideActivityList(int isShow){
        switch (isShow){
            case SHOW:
                refreshLayout.setVisibility(View.VISIBLE);
                rvActivityList.setVisibility(View.VISIBLE);
                layoutLocateFailure.setVisibility(View.GONE);
                break;
            case HIDE:
                refreshLayout.setVisibility(View.GONE);
                rvActivityList.setVisibility(View.GONE);
                layoutLocateFailure.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }
    /**
    * 定位失败界面点击定位按钮事件
    */
    @OnClick(R.id.btn_locate_failure_locate)
    void cityPicker(){
        locatePosition();
    }
    @Override
    public void onStart() {
        super.onStart();
        mLocationClient.start();
    }

    /**
    * fragment销毁时停止监听位置信息
    */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
        mLocationClient.unRegisterLocationListener(myListener);
    }
}
