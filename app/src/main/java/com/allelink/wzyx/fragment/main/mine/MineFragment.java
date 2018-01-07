package com.allelink.wzyx.fragment.main.mine;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.allelink.wzyx.R;
import com.allelink.wzyx.activity.AboutActivity;
import com.allelink.wzyx.activity.AccountSettingActivity;
import com.allelink.wzyx.activity.SettingActivity;
import com.allelink.wzyx.activity.UserEvaluateListActivity;
import com.allelink.wzyx.activity.baidunav.BaiduNavActivity;
import com.allelink.wzyx.utils.activity.ActivityUtils;
import com.allelink.wzyx.utils.log.LogUtil;
import com.allelink.wzyx.utils.storage.WzyxPreference;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportFragment;
import qiu.niorgai.StatusBarCompat;

/**
 * @author yangc
 * @version 1.0
 * @filename MineFragment
 * @date 2017/11/22
 * @description 个人中心 fragment
 * @email 1048027353@qq.com
 */

public class MineFragment extends SupportFragment{
    private static final String TAG = "MineFragment";
    private Unbinder mUnbinder = null;
    /**
    * 需要显示的余额
    */
    @BindView(R.id.tv_mine_spec_balance)
    AppCompatTextView specBalance = null;
    /**
     * 需要显示的抵用券
     */
    @BindView(R.id.tv_mine_spec_discount_coupon)
    AppCompatTextView specDiscountCoupon = null;
    /**
     * 需要显示的积分
     */
    @BindView(R.id.tv_mine_spec_point)
    AppCompatTextView specPoint = null;
    /**
    * 昵称
    */
    @BindView(R.id.tv_mine_nickname)
    TextView tvNickname = null;
    /**
    * 头像
    */
    @BindView(R.id.iv_mine_avatar)
    ImageView ivAvatar = null;
    public static MineFragment newInstance() {
        Bundle args = new Bundle();
        MineFragment fragment = new MineFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        LogUtil.d(TAG,"我的页面可见");
        //设置沉浸式状态栏
        StatusBarCompat.setStatusBarColor(_mActivity,getResources().getColor(R.color.brands_color));
        //获取本地保存的昵称
        String nickname = WzyxPreference.getCustomAppProfile(WzyxPreference.KEY_NICKNAME);
        if(nickname.isEmpty()){
            tvNickname.setText(getResources().getString(R.string.not_set_nickname));
        }else{
            tvNickname.setText(nickname);
        }
        //获取本地保存的头像
        String avatar = WzyxPreference.getCustomAppProfile(WzyxPreference.KEY_AVATAR_URL);
        if(avatar.isEmpty()){
            ivAvatar.setBackgroundResource(R.drawable.avatar_default);
        }else{
            Glide.with(this)
                    .load(avatar)
                    .into(ivAvatar);
        }
        String totalPoints = WzyxPreference.getCustomAppProfile(WzyxPreference.KEY_TOTAL_POINTS);
        specPoint.setText(getResources().getString(R.string.spec_point,totalPoints));
    }


    /**
    * 设置按钮点击事件
    */
    @OnClick(R.id.iv_mine_settings)
    void setting(){
        LogUtil.d(TAG,"settings");
        ActivityUtils.startActivity(_mActivity, SettingActivity.class);
    }
    /**
     * 通知按钮点击事件
     */
    @OnClick(R.id.iv_mine_notification)
    void notification(){
        LogUtil.d(TAG,"notification");
    }
    /**
     * 收藏点击事件
     */
    @OnClick(R.id.ll_mine_collection_layout)
    void collection(){
        LogUtil.d(TAG,"collection");

    }
    /**
     * 评论点击事件
     */
    @OnClick(R.id.ll_mine_comment_layout)
    void comment(){
        LogUtil.d(TAG,"comment");
        Intent intent=new Intent(_mActivity,UserEvaluateListActivity.class);
        startActivity(intent);
    }
    /**
     * 账单点击事件
     */
    @OnClick(R.id.ll_mine_bill_layout)
    void bill(){
        LogUtil.d(TAG,"bill");
    }
    /**
     * 我的钱包点击事件
     */
    @OnClick(R.id.ll_mine_wallet_layout)
    void wallet(){
        LogUtil.d(TAG,"wallet");
    }
    /**
     * 我的余额点击事件
     */
    @OnClick(R.id.ll_mine_balance_layout)
    void balance(){
        LogUtil.d(TAG,"balance");
    }
    /**
     * 我的抵用券点击事件
     */
    @OnClick(R.id.ll_mine_discount_coupon_layout)
    void coupon(){
        LogUtil.d(TAG,"coupon");
    }
    /**
     * 我的积分点击事件
     */
    @OnClick(R.id.ll_mine_point_layout)
    void point(){
        LogUtil.d(TAG,"point");
    }
    /**
     * 分享好友点击事件
     */
    @OnClick(R.id.ll_mine_share_layout)
    void share(){
        LogUtil.d(TAG,"share");
    }
    /**
     * 客服中心点击事件
     */
    @OnClick(R.id.ll_mine_call_center_layout)
    void callCenter(){
        LogUtil.d(TAG,"callCenter");
    }
    /**
     * 我要合作点击事件
     */
    @OnClick(R.id.ll_mine_cooperation_layout)
    void cooperation(){
        LogUtil.d(TAG,"cooperation");
    }
    /**
     * 关于万众艺兴点击事件
     */
    @OnClick(R.id.ll_mine_about_layout)
    void about(){
        LogUtil.d(TAG,"about");
        ActivityUtils.startActivity(_mActivity, AboutActivity.class);
    }
    /**
    * 进入个人中心
    */
    @OnClick(R.id.iv_mine_avatar)
    void personalInfo(){
        LogUtil.d(TAG,"personalInfo");
        ActivityUtils.startActivity(_mActivity, AccountSettingActivity.class);
    }
    @OnClick(R.id.tv_mine_personal_info)
    void personalInfoTv(){
        personalInfo();
    }
    @OnClick(R.id.ll_mine_personal_info)
    void personalInfoLl(){
        personalInfo();
    }
    /**
    * fragment销毁时解绑
    */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }


}
