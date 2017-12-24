package com.allelink.wzyx.fragment.main.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.allelink.wzyx.R;
import com.allelink.wzyx.activity.PayOrderActivity;
import com.allelink.wzyx.activity.RefundActivity;
import com.allelink.wzyx.adapter.OrderItemAdapter;
import com.allelink.wzyx.app.order.IGetOrderListListener;
import com.allelink.wzyx.app.order.IOrderListener;
import com.allelink.wzyx.app.order.OrderHandler;
import com.allelink.wzyx.model.OrderItem;
import com.allelink.wzyx.ui.dialog.DialogManager;
import com.allelink.wzyx.ui.dialog.IDialogListener;
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
import qiu.niorgai.StatusBarCompat;

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
    private static final String ACTIVITY_NAME = "activityName";
    private static final String ACTIVITY_COST = "cost";
    private static final String ORDER_ID = "orderId";
    /**
     * tab标签数据
     */
    private String[] mTabTitles = new String[]{
            "全部",
            "待付款",
            "已付款",
            "退款中",
            "已完成"
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
    private static final int ORDER_ALL = 6;
    private static final int ORDER_PAID = 1;
    private static final int ORDER_UNPAID = 0;
    private static final int ORDER_CANCELED = -1;
    private static final int ORDER_REFUNDING = 2;
    private static final int ORDER_REFUND_SUCCESS = 3;
    private static final int ORDER_COMPLETED = 5;
    /**
    * 订单的状态
    */
    private int mOrderState = ORDER_ALL;
    private static int REFRESH = 3;
    private static int LOAD_MORE = 4;

    private OrderItem mOrderItem = null;
    private int mPosition;
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
        initItemEvent();
        initRefreshEvent();
    }

    /**
    * 在fragment可见时调用
    */
    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        LogUtil.d(TAG,"订单页可见");
        //设置沉浸式状态栏
        StatusBarCompat.setStatusBarColor(_mActivity,getResources().getColor(R.color.white));
        refreshLayout.autoRefresh();

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
            public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
                OrderItem orderItem = (OrderItem) adapter.getItem(position);
                mOrderItem = orderItem;
                mPosition = position;
                String orderState = orderItem.getOrderState();
                switch (view.getId()){
                    //订单删除
                    case R.id.ll_right_delete:
                        //待付款和退款中的订单都不能删除
                        if(orderState.equals(String.valueOf(ORDER_REFUNDING))){
                            ToastUtil.toastShort(_mActivity,getString(R.string.order_cannot_delete));
                        }else{
                            showDialog(R.layout.dialog_common,
                                    R.id.btn_dialog_confirm,"确定",
                                    R.id.btn_dialog_cancel,"取消",
                                    R.id.tv_dialog_title,getString(R.string.confirm_delete_order),
                                    R.id.tv_dialog_content,getString(R.string.confirm_delete_order_tip),
                                    orderDeleteListener);

                        }
                        break;
                    //订单取消
                    case R.id.ll_right_cancel:
                        //已付款订单,退款成功订单和已完成订单不可取消
                        if(orderState.equals(String.valueOf(ORDER_PAID))
                                || orderState.equals(String.valueOf(ORDER_REFUND_SUCCESS))
                                || orderState.equals(String.valueOf(ORDER_COMPLETED))){
                            ToastUtil.toastShort(_mActivity,getString(R.string.order_cannot_cancel));
                        }else{
                            showDialog(R.layout.dialog_common,
                                    R.id.btn_dialog_confirm,"确定",
                                    R.id.btn_dialog_cancel,"取消",
                                    R.id.tv_dialog_title,getString(R.string.confirm_cancel_order),
                                    R.id.tv_dialog_content,getString(R.string.confirm_cancel_order_tip),
                                    orderCancelListener);
                        }
                        break;
                    case R.id.btn_item_fragment_order_right:

                        if(orderState.equals(String.valueOf(ORDER_PAID))){
                            //退款
                            LogUtil.d(TAG,"退款");
                            Intent intent = new Intent(_mActivity, RefundActivity.class);
                            intent.putExtra(ACTIVITY_COST, orderItem.getCost());
                            intent.putExtra(ORDER_ID, orderItem.getOrderIdStr());
                            startActivity(intent);
                        }else if(orderState.equals(String.valueOf(ORDER_UNPAID))){
                            //去付款
                            LogUtil.d(TAG,"去付款");
                            Intent intent = new Intent(_mActivity, PayOrderActivity.class);
                            intent.putExtra(ACTIVITY_COST, orderItem.getCost());
                            intent.putExtra(ACTIVITY_NAME, orderItem.getActivityName());
                            intent.putExtra(ORDER_ID, orderItem.getOrderIdStr());
                            startActivity(intent);
                        }
                        break;
                    default:
                        break;
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
                LogUtil.d(TAG,position+"");
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
                    mOrderState = ORDER_UNPAID;
                }else if(mTabTitles[2].equals(tab.getText())){
                    mOrderState = ORDER_PAID;
                }else if(mTabTitles[3].equals(tab.getText())){
                    mOrderState = ORDER_REFUNDING;
                }else if(mTabTitles[4].equals(tab.getText())){
                    mOrderState = ORDER_COMPLETED;
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
                    mOrderState = ORDER_UNPAID;
                }else if(mTabTitles[2].equals(tab.getText())){
                    mOrderState = ORDER_PAID;
                }else if(mTabTitles[3].equals(tab.getText())){
                    mOrderState = ORDER_REFUNDING;
                }else if(mTabTitles[4].equals(tab.getText())){
                    mOrderState = ORDER_COMPLETED;
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
        if(orderState != 6){
            params.put("orderState", orderState);
        }
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
        //WzyxLoader.showLoading(_mActivity);
        HashMap<String, Object> params = new HashMap<>();
        params.put(WzyxPreference.KEY_USER_ID, WzyxPreference.getCustomAppProfile(WzyxPreference.KEY_USER_ID));
        if(orderState != 6){
            params.put("orderState", orderState);
        }
        OrderHandler.getOrderList(params, new IGetOrderListListener() {
            @Override
            public void onSuccess(List<OrderItem> orderItems) {
                //WzyxLoader.stopLoading();
                mOrderItems = orderItems;
                if(type == REFRESH){
                    mAdapter.replaceData(mOrderItems);
                    refreshLayout.finishRefresh(1000,true);
                }else if(type == LOAD_MORE){
                    mAdapter.replaceData(mOrderItems);
                    refreshLayout.finishLoadmore(1000,true);
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                //WzyxLoader.stopLoading();
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
    * 处理 dialog 按钮点击事件
    */
    private IDialogListener orderDeleteListener = new IDialogListener() {
        @Override
        public void onPositiveButtonClick() {
            //确定按钮点击事件
            LogUtil.d(TAG,"确定");
            updateOrderState(mOrderItem,-2,mPosition);
        }

        @Override
        public void onNegativeButtonClick() {
            //取消按钮点击事件
            LogUtil.d(TAG,"取消");
        }
    };
    /**
     * 处理 dialog 按钮点击事件
     */
    private IDialogListener orderCancelListener = new IDialogListener() {
        @Override
        public void onPositiveButtonClick() {
            //确定按钮点击事件
            LogUtil.d(TAG,"确定");
            updateOrderState(mOrderItem,-1,mPosition);
        }

        @Override
        public void onNegativeButtonClick() {
            //取消按钮点击事件
            LogUtil.d(TAG,"取消");
        }
    };
    /**
     * 显示对话框
     * @param dialogLayout 对话框布局
     * @param positiveButtonId 确定按钮id
     * @param positiveButtonText 确定按钮文字
     * @param negativeButtonId  取消按钮id
     * @param negativeButtonText  取消按钮文字
     * @param titleId 标题id
     * @param title 标题文字
     * @param contentId 内容id
     * @param content 内容文字
     * @param dialogListener 按钮回调接口
     */
    private void showDialog(@LayoutRes int dialogLayout,
                            @IdRes int positiveButtonId, CharSequence positiveButtonText,
                            @IdRes int negativeButtonId, CharSequence negativeButtonText,
                            @IdRes int titleId,String title,
                            @IdRes int contentId,String content,
                            IDialogListener dialogListener
                            ){
        DialogManager.create()
                .with(_mActivity)
                .setDialogListener(dialogListener)
                .setDialogLayout(dialogLayout)
                .setButtonResId(positiveButtonId,positiveButtonText,
                        negativeButtonId,negativeButtonText)
                .setTitle(titleId,title)
                .setContent(contentId,content)
                .show();
    }

    /**
     * 更新订单状态
     * @param orderItem 订单项
     * @param orderState 订单状态
     * @param position 订单项位置
     */
    private void updateOrderState(OrderItem orderItem, final int orderState, final int position){
        WzyxLoader.showLoading(_mActivity);
        HashMap<String, Object> params = new HashMap<>();
        params.put("orderIdStr", orderItem.getOrderIdStr());
        params.put("orderState",orderState);
        OrderHandler.delete(params, new IOrderListener() {
            @Override
            public void onSuccess(String orderId) {
                WzyxLoader.stopLoading();
                if(orderState == -2){
                    ToastUtil.toastShort(_mActivity,getResources().getString(R.string.order_delete_success));
                }else if(orderState == -1){
                    ToastUtil.toastShort(_mActivity,getResources().getString(R.string.order_cancel_success));
                }
                mAdapter.remove(position);
                refreshLayout.autoRefresh();
            }
            @Override
            public void onFailure(String errorMessage) {
                WzyxLoader.stopLoading();
                if(orderState == -2){
                    ToastUtil.toastShort(_mActivity,getResources().getString(R.string.order_delete_failure));
                }else if(orderState == -1){
                    ToastUtil.toastShort(_mActivity,getResources().getString(R.string.order_cancel_failure));
                }
            }
        });
    }

}
