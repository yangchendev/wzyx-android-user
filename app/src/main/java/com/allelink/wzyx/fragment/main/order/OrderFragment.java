package com.allelink.wzyx.fragment.main.order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.allelink.wzyx.R;
import com.allelink.wzyx.adapter.OrderItemAdapter;
import com.allelink.wzyx.app.order.IGetOrderListListener;
import com.allelink.wzyx.app.order.OrderHandler;
import com.allelink.wzyx.model.OrderItem;
import com.allelink.wzyx.ui.loader.WzyxLoader;
import com.allelink.wzyx.utils.log.LogUtil;
import com.allelink.wzyx.utils.storage.WzyxPreference;
import com.allelink.wzyx.utils.toast.ToastUtil;
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
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * @author yangc
 * @version 1.0
 * @filename OrderFragment
 * @date 2017/11/22
 * @description 订单 fragment
 * @email 1048027353@qq.com
 */

public class OrderFragment extends SupportFragment{
    private static final String TAG = OrderFragment.class.getSimpleName();
    private Unbinder mUnbinder = null;
    /**
     * tab标签数据
     */
    private String[] mTabTitles = new String[]{
            "全部",
            "已完成",
            "待付款",
            "已取消"
    };
    /**
    * UI
    */
    @BindView(R.id.tab_fragment_order_tab_layout)
    TabLayout tabLayout = null;
    @BindView(R.id.rv_fragment_order_order_list)
    RecyclerView recyclerView = null;
    @BindView(R.id.srl_fragment_order)
    SmartRefreshLayout refreshLayout = null;
    /**
     * 适配器
     */
    private OrderItemAdapter mAdapter = null;
    /**
     * 布局管理器
     */
    private LinearLayoutManager mLayoutManager = null;
    /**
     * 存放活动信息的集合
     */
    private List<OrderItem> mOrderItems = new ArrayList<>();
    /**
     * 各种类型
     */
    private static final int ORDER_ALL = 2;
    private static final int ORDER_COMPLETED = 1;
    private static final int ORDER_UNPAID = 0;
    private static final int ORDER_CANCELED = -1;
    /**
    * 订单的状态
    */
    private int mOrderState = ORDER_ALL;
    private static int REFRESH = 3;
    private static int LOAD_MORE = 4;
    public static OrderFragment newInstance() {
        Bundle args = new Bundle();
        OrderFragment fragment = new OrderFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initTabData();
        handleTabEvent();
        initListData();
        bindAdapter();
        initItemChildEvent();
        initRefreshEvent();

    }

    @Override
    public void onResume() {
        super.onResume();
        getOrderList(mOrderState);
    }

    /**
    * 下拉刷新和上拉加载事件
    */
    private void initRefreshEvent() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getOrderList(mOrderState,refreshlayout,REFRESH);
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                getOrderList(mOrderState,refreshlayout,LOAD_MORE);
            }
        });
    }

    /**
    * 初始化订单项子View的点击事件
    */
    private void initItemChildEvent() {
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                OrderItem orderItem = (OrderItem) adapter.getItem(position);
                String orderState = orderItem.getOrderState();
                if(orderState.equals(String.valueOf(ORDER_COMPLETED))){
                    //退款
                    LogUtil.d(TAG,"退款");
                }else if(orderState.equals(String.valueOf(ORDER_UNPAID))){
                    //去付款
                    LogUtil.d(TAG,"去付款");
                }
            }
        });
    }

    /**
    * 将RecyclerView和adapter绑定
    */
    private void bindAdapter() {
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
        mAdapter.bindToRecyclerView(recyclerView);
        mAdapter.setEmptyView(R.layout.fragment_order_empty);
        //generateTestData();
    }

    /**
    * 初始化列表数据
    */
    private void initListData() {
        mAdapter = new OrderItemAdapter(R.layout.item_fragment_order_order, mOrderItems);
        mLayoutManager = new LinearLayoutManager(_mActivity);
    }

    /**
    * 处理tabLayout点击事件
    */
    private void handleTabEvent() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(mTabTitles[0].equals(tab.getText())){
                    mOrderState = ORDER_ALL;
                }else if(mTabTitles[1].equals(tab.getText())){
                    mOrderState = ORDER_COMPLETED;
                }else if(mTabTitles[2].equals(tab.getText())){
                    mOrderState = ORDER_UNPAID;
                }else if(mTabTitles[3].equals(tab.getText())){
                    mOrderState = ORDER_CANCELED;
                }
                //获取订单信息
                getOrderList(mOrderState);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if(mTabTitles[0].equals(tab.getText())){
                    mOrderState = ORDER_ALL;
                }else if(mTabTitles[1].equals(tab.getText())){
                    mOrderState = ORDER_COMPLETED;
                }else if(mTabTitles[2].equals(tab.getText())){
                    mOrderState = ORDER_UNPAID;
                }else if(mTabTitles[3].equals(tab.getText())){
                    mOrderState = ORDER_CANCELED;
                }
                //获取订单信息
                getOrderList(mOrderState);
            }
        });
    }


    /**
    * 初始化tabLayout的tab数据
    */
    private void initTabData() {
        for(int i = 0; i < mTabTitles.length; i++){
            //创建tab
            TabLayout.Tab tab = tabLayout.newTab();
            //设置标题
            tab.setText(mTabTitles[i]);
            //添加tab到tabLayout
            tabLayout.addTab(tab);
        }

    }

    /**
     * 获取订单列表
     * @param orderState 订单状态
     * @return 返回订单列表
     */
    private void getOrderList(int orderState){
        WzyxLoader.showLoading(_mActivity);
        HashMap<String, Object> params = new HashMap<>();
        params.put(WzyxPreference.KEY_USER_ID, WzyxPreference.getCustomAppProfile(WzyxPreference.KEY_USER_ID));
        params.put("orderState", orderState);
        OrderHandler.getOrderList(params, new IGetOrderListListener() {
            @Override
            public void onSuccess(List<OrderItem> orderItems) {
                WzyxLoader.stopLoading();
                mOrderItems = orderItems;
                mAdapter.replaceData(mOrderItems);
            }

            @Override
            public void onFailure(String errorMessage) {
                WzyxLoader.stopLoading();
                ToastUtil.toastShort(_mActivity,errorMessage);
            }
        });
    }
    /**
     * 获取订单列表
     * @param orderState 订单状态
     * @return 返回订单列表
     */
    private void getOrderList(int orderState,final RefreshLayout refreshLayout,final int type){
        WzyxLoader.showLoading(_mActivity);
        HashMap<String, Object> params = new HashMap<>();
        params.put(WzyxPreference.KEY_USER_ID, WzyxPreference.getCustomAppProfile(WzyxPreference.KEY_USER_ID));
        params.put("orderState", orderState);
        OrderHandler.getOrderList(params, new IGetOrderListListener() {
            @Override
            public void onSuccess(List<OrderItem> orderItems) {
                WzyxLoader.stopLoading();
                mOrderItems = orderItems;
                if(type == REFRESH){
                    mAdapter.replaceData(mOrderItems);
                    refreshLayout.finishRefresh(1000,true);
                }else if(type == LOAD_MORE){
                    mAdapter.addData(mOrderItems);
                    refreshLayout.finishLoadmore(1000,true);
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                WzyxLoader.stopLoading();
                ToastUtil.toastShort(_mActivity,errorMessage);
                if(type == REFRESH){
                    refreshLayout.finishRefresh(1000,false);
                }else if(type == LOAD_MORE){
                    refreshLayout.finishLoadmore(1000,false);
                }
            }
        });
    }
    /**
    * 测试数据用于模拟
    */
    private void generateTestData(){
        for(int i = 0; i < 10; i++){
            OrderItem orderItem = new OrderItem();
            orderItem.setActivityName("第"+i+"个活动");
            orderItem.setCost(i*200+"");
            orderItem.setImageUrl("0");
            orderItem.setOrderIdStr("1111"+i+"123456789");
            if(i%2 == 0){
                orderItem.setOrderState("1");
            }else{
                orderItem.setOrderState("0");
            }
            mOrderItems.add(orderItem);
        }
    }
}
