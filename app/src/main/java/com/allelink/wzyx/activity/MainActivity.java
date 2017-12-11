package com.allelink.wzyx.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;

import com.allelink.wzyx.R;
import com.allelink.wzyx.activity.base.BaseActivity;
import com.allelink.wzyx.app.WzyxApplication;
import com.allelink.wzyx.app.user.GetUserInfoHandler;
import com.allelink.wzyx.app.user.IGetUserInfoListener;
import com.allelink.wzyx.fragment.main.forum.ForumFragment;
import com.allelink.wzyx.fragment.main.home.HomeFragment;
import com.allelink.wzyx.fragment.main.mine.MineFragment;
import com.allelink.wzyx.fragment.main.near.NearFragment;
import com.allelink.wzyx.fragment.main.order.OrderFragment;
import com.allelink.wzyx.model.User;
import com.allelink.wzyx.ui.loader.WzyxLoader;
import com.allelink.wzyx.utils.log.LogUtil;
import com.allelink.wzyx.utils.storage.WzyxPreference;
import com.allelink.wzyx.utils.toast.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author yangc
 * @filename MainActivity.java
 * @date 2017/11/2
 * @version 1.0
 * @description 管理5个fragment
 * @email 1048027353@qq.com
 */
public class MainActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int FIRST = 0;
    private long mExitTime = 0;
    private static final int EXIT_TIME = 2000;
    private static String ACTION_FINISH = "ACTION_FINISH";
    /**
    * UI
    */
    @BindView(R.id.iv_bottom_bar_home)
    ImageView ivHome = null;
    @BindView(R.id.iv_bottom_bar_near)
    ImageView ivNear = null;
    @BindView(R.id.iv_bottom_bar_forum)
    ImageView ivForum = null;
    @BindView(R.id.iv_bottom_bar_order)
    ImageView ivOrder = null;
    @BindView(R.id.iv_bottom_bar_mine)
    ImageView ivMine = null;
    @BindView(R.id.tv_bottom_bar_home)
    TextView tvHome = null;
    @BindView(R.id.tv_bottom_bar_near)
    TextView tvNear = null;
    @BindView(R.id.tv_bottom_bar_forum)
    TextView tvForum = null;
    @BindView(R.id.tv_bottom_bar_order)
    TextView tvOrder = null;
    @BindView(R.id.tv_bottom_bar_mine)
    TextView tvMine = null;
    private HomeFragment mHomeFragment = null;
    private NearFragment mNearFragment = null;
    private ForumFragment mForumFragment = null;
    private OrderFragment mOrderFragment = null;
    private MineFragment mMineFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //隐藏actionBar
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        //绑定ButterKnife
        ButterKnife.bind(this);
        initFragment();
        ivHome.setImageResource(R.drawable.ic_home_pressed);
        tvHome.setTextColor(getResources().getColor(R.color.brands_color));
        //请求用户信息
        initUserInfo();
        //注册finish广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_FINISH);
        registerReceiver(mFinishReceiver, filter);
    }
    /**
    * 请求用户信息
    */
    private void initUserInfo() {
        //加载动画开始
        WzyxLoader.showLoading(MainActivity.this);
        params.clear();
        params.put("phoneNumber", WzyxPreference.getCustomAppProfile(WzyxPreference.KEY_PHONE_NUMBER));
        GetUserInfoHandler.getUserInfo(params, new IGetUserInfoListener() {
            @Override
            public void onGetUserInfoSuccess(User user) {
                WzyxLoader.stopLoading();
                storeUserInfo(user);
            }

            @Override
            public void onGetUserInfoFailure(String error) {
                WzyxLoader.stopLoading();
                ToastUtil.toastShort(MainActivity.this,getResources().getString(R.string.get_user_info_failure));
            }
        });
    }
    /**
     * 存储用户信息到本地
     * @param user 用户实体类
     */
    private void storeUserInfo(User user) {
        WzyxPreference.addCustomAppProfile(WzyxPreference.KEY_PHONE_NUMBER,user.getPhoneNumber());
        WzyxPreference.addCustomAppProfile(WzyxPreference.KEY_NICKNAME,user.getNickname());
        WzyxPreference.addCustomAppProfile(WzyxPreference.KEY_GENDER,user.getGender());
        WzyxPreference.addCustomAppProfile(WzyxPreference.KEY_AVATAR_URL,user.getAvatar());
        WzyxPreference.addCustomAppProfile(WzyxPreference.KEY_BIRTHDAY,user.getBirthday());
        WzyxPreference.addCustomAppProfile(WzyxPreference.KEY_USER_ID,user.getUserId());
    }
    /**
    * 初始化fragment
    */
    private void initFragment() {
        mHomeFragment = HomeFragment.newInstance();
        mNearFragment = NearFragment.newInstance();
        mForumFragment = ForumFragment.newInstance();
        mOrderFragment = OrderFragment.newInstance();
        mMineFragment = MineFragment.newInstance();
        //初始时显示第一个fragment
        loadMultipleRootFragment(R.id.fl_container,FIRST,
                mHomeFragment,
                mNearFragment,
                mForumFragment,
                mOrderFragment,
                mMineFragment
        );
    }

    /**
    * 切换到主页
    */
    @OnClick(R.id.ll_bottom_bar_home_tab_layout)
    void selectHomeFragment(){
        resetTabStates();
        ivHome.setImageResource(R.drawable.ic_home_pressed);
        tvHome.setTextColor(getResources().getColor(R.color.brands_color));
        showHideFragment(mHomeFragment);
    }

    /**
     * 切换到附近
     */
    @OnClick(R.id.ll_bottom_bar_near_tab_layout)
    void selectNearFragment(){
        resetTabStates();
        ivNear.setImageResource(R.drawable.ic_nearby_pressed);
        tvNear.setTextColor(getResources().getColor(R.color.brands_color));
        showHideFragment(mNearFragment);
    }
    /**
     * 切换到论坛
     */
    @OnClick(R.id.ll_bottom_bar_forum_tab_layout)
    void selectForumFragment(){
        resetTabStates();
        ivForum.setImageResource(R.drawable.ic_forum_pressed);
        tvForum.setTextColor(getResources().getColor(R.color.brands_color));
        showHideFragment(mForumFragment);
    }
    /**
     * 切换到订单
     */
    @OnClick(R.id.ll_bottom_bar_order_tab_layout)
    void selectOrderFragment(){
        resetTabStates();
        ivOrder.setImageResource(R.drawable.ic_order_pressed);
        tvOrder.setTextColor(getResources().getColor(R.color.brands_color));
        showHideFragment(mOrderFragment);
    }
    /**
     * 切换到我的
     */
    @OnClick(R.id.ll_bottom_bar_mine_tab_layout)
    void selectMineFragment(){
        resetTabStates();
        ivMine.setImageResource(R.drawable.ic_mine_pressed);
        tvMine.setTextColor(getResources().getColor(R.color.brands_color));
        showHideFragment(mMineFragment);
    }
    /**
    * 重置tab的状态
    */
    private void resetTabStates() {
        ivHome.setImageResource(R.drawable.ic_home_nor);
        tvHome.setTextColor(getResources().getColor(R.color.black_666));
        ivNear.setImageResource(R.drawable.ic_nearby_nor);
        tvNear.setTextColor(getResources().getColor(R.color.black_666));
        ivForum.setImageResource(R.drawable.ic_forum_nor);
        tvForum.setTextColor(getResources().getColor(R.color.black_666));
        ivOrder.setImageResource(R.drawable.ic_order_nor);
        tvOrder.setTextColor(getResources().getColor(R.color.black_666));
        ivMine.setImageResource(R.drawable.ic_mine_nor);
        tvMine.setTextColor(getResources().getColor(R.color.black_666));
    }

    /**
    * 广播接收器
    */
    public BroadcastReceiver mFinishReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(ACTION_FINISH.equals(intent.getAction())){
                LogUtil.d(TAG,intent.getAction());
                ((WzyxApplication)getApplication()).finishSingleActivity(MainActivity.this);
                System.gc();
            }
        }
    };

    /**
    * 应用销毁时取消注册广播
    */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mFinishReceiver);
    }
    /**
    * 按两次退出应用
    */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if(System.currentTimeMillis() - mExitTime > EXIT_TIME){
                ToastUtil.toastShort(MainActivity.this,getResources().getString(R.string.twice_back));
                mExitTime = System.currentTimeMillis();
            }else {
                ((WzyxApplication)getApplication()).finishSingleActivity(MainActivity.this);
                System.gc();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
