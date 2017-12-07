package com.allelink.wzyx.fragment.main.near;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.allelink.citypicker.CityPickerActivity;
import com.allelink.wzyx.R;
import com.allelink.wzyx.adapter.ActivityItemAdapter;
import com.allelink.wzyx.model.ActivityItem;
import com.allelink.wzyx.utils.log.LogUtil;
import com.allelink.wzyx.utils.toast.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportFragment;
import qiu.niorgai.StatusBarCompat;

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
    private static final int REQUEST_CODE_PICK_CITY = 4001;
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

    public static NearFragment newInstance() {
        Bundle args = new Bundle();
        NearFragment fragment = new NearFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
        generateTestData();
    }

    /**
    * 处理tab的切换事件
    */
    private void handleTabEvent() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                ToastUtil.toastShort(_mActivity,tab.getText());
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
        }else if(mCostArrow == Arrow.UP){
            tvCostOrder.setTextColor(getResources().getColor(R.color.brands_color));
            ivCostUpArrow.setImageResource(R.drawable.ic_up_arrow_nor);
            ivCostDownArrow.setImageResource(R.drawable.ic_down_arrow_pressed);
            //3.进行网络请求更新
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
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case REQUEST_CODE_PICK_CITY:
                    tvPosition.setText(data.getStringExtra(CityPickerActivity.KEY_PICKED_CITY));
                    break;
                default:
                    break;
            }
        }
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        StatusBarCompat.setStatusBarColor(_mActivity,getResources().getColor(R.color.white));
    }
}
