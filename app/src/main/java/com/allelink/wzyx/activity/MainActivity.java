package com.allelink.wzyx.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.widget.ImageView;
import android.widget.TextView;

import com.allelink.wzyx.R;
import com.allelink.wzyx.activity.base.BaseActivity;
import com.allelink.wzyx.fragment.main.forum.ForumFragment;
import com.allelink.wzyx.fragment.main.home.HomeFragment;
import com.allelink.wzyx.fragment.main.mine.MineFragment;
import com.allelink.wzyx.fragment.main.near.NearFragment;
import com.allelink.wzyx.fragment.main.order.OrderFragment;

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


}
