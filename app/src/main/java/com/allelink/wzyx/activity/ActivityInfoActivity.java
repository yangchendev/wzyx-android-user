package com.allelink.wzyx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.widget.Button;

import com.allelink.wzyx.R;
import com.allelink.wzyx.activity.base.BaseActivity;
import com.allelink.wzyx.app.WzyxApplication;
import com.allelink.wzyx.app.activityinfo.ActivityInfoHandler;
import com.allelink.wzyx.app.activityinfo.GetOneActivityDetailInfoListener;
import com.allelink.wzyx.model.ActivityDetailItem;
import com.allelink.wzyx.ui.TitleBar;
import com.allelink.wzyx.ui.banner.GlideImageLoader;
import com.allelink.wzyx.ui.loader.WzyxLoader;
import com.allelink.wzyx.utils.log.LogUtil;
import com.allelink.wzyx.utils.storage.WzyxPreference;
import com.allelink.wzyx.utils.toast.ToastUtil;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import qiu.niorgai.StatusBarCompat;

/**
 * 活动详情页面
 * @author yangc
 * @date 2017/12/9
 * @version 1.0
 * @email yangchendev@qq.com
 */

public class ActivityInfoActivity extends BaseActivity {
    private static final String TAG = "ActivityInfoActivity";
    private static final String ACTIVITY_ID = "activityId";
    private static final String USER_ID = "userId";
    private static final String SELLER_ID = "sellerId";
    private static final String ACTIVITY_NAME = "activityName";
    private static final String ACTIVITY_COST = "cost";
    /**
    * UI
    */
    @BindView(R.id.banner_activity_info)
    Banner banner = null;
    @BindView(R.id.tb_activity_info)
    TitleBar titleBar = null;
    @BindView(R.id.tv_activity_info_activity_name)
    AppCompatTextView tvActivityName = null;
    @BindView(R.id.tv_activity_info_enroll_number)
    AppCompatTextView tvEnrollNumber = null;
    @BindView(R.id.tv_activity_info_activity_address)
    AppCompatTextView tvActivityAddress = null;
    @BindView(R.id.tv_activity_info_activity_intro)
    AppCompatTextView tvActivityIntro = null;
    @BindView(R.id.tv_activity_info_activity_price)
    AppCompatTextView tvActivityPrice = null;
    @BindView(R.id.tv_activity_info_begin_time)
    AppCompatTextView tvBeginTime = null;
    @BindView(R.id.tv_activity_info_end_time)
    AppCompatTextView tvEndTime = null;
    @BindView(R.id.btn_activity_info_enroll_activity)
    Button btnEnroll = null;
    /**
    * data
    */
    private String activityId = null;
    private String[] imageUrls = null;
    private String mActivityName = null;
    private String mActivityEnrollNumber = null;
    private String mActivityAddress = null;
    private String mActivityIntro = null;
    private String mActivityBeginTime = null;
    private String mActivityEndTime = null;
    private String mActivityPrice = null;
    private String mImageUrl = null;
    private ActivityDetailItem detailItem = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        WzyxApplication.addDestroyActivity(this,"ActivityInfoActivity");
        StatusBarCompat.setStatusBarColor(this,getResources().getColor(R.color.white));
        //隐藏actionBar
        android.app.ActionBar actionBar = getActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        //绑定ButterKnife
        ButterKnife.bind(this);
        //初始化banner
        initBanner();
        initTitleBarEvent();
        //接收活动ID
        activityId = getIntent().getStringExtra(ACTIVITY_ID);
        //disable掉报名按钮
        btnEnroll.setEnabled(false);
        //请求活动详细信息
        getActivityDetail(activityId);
    }
    /**
    * 初始化titlebar点击事件
    */
    private void initTitleBarEvent() {
        titleBar.setOnTitleBarButtonClickListener(new TitleBar.onTitleBarButtonClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {

            }
        });

    }

    /**
    * 获取活动详细信息
    */
    private void getActivityDetail(String activityId) {
        WzyxLoader.showLoading(this);
        params.clear();
        params.put(ACTIVITY_ID,activityId);
        ActivityInfoHandler.getOneActivityDetailInfo(params, new GetOneActivityDetailInfoListener() {
            @Override
            public void onSuccess(ActivityDetailItem activityDetailItem) {
                WzyxLoader.stopLoading();
                //设置数据到界面上
                setActivityInfoToView(activityDetailItem);
                detailItem = activityDetailItem;
                //enable报名按钮
                btnEnroll.setEnabled(true);
            }

            @Override
            public void onFailure(String errorMessage) {
                WzyxLoader.stopLoading();
                ToastUtil.toastShort(ActivityInfoActivity.this,getResources().getString(R.string.activity_load_failure));
            }
        });
    }

    /**
     * 设置数据到界面上
     * @param activityDetailItem 活动详细信息
     */
    private void setActivityInfoToView(ActivityDetailItem activityDetailItem) {
        //获取数据
        mActivityName = activityDetailItem.getActivityName();
        mActivityEnrollNumber = activityDetailItem.getEnrollNumber();
        mActivityAddress = activityDetailItem.getAddress();
        mActivityIntro = activityDetailItem.getActivityInfo();
        mActivityBeginTime = activityDetailItem.getBeginTime();
        mActivityEndTime = activityDetailItem.getEndTime();
        mActivityPrice = activityDetailItem.getCost();
        mImageUrl = activityDetailItem.getImageUrl();
        mImageUrl = activityDetailItem.getImageUrl().replace("\\","/");
        //TODO
        //imageUrls = mImageUrl.split(",");
        //设置到界面上
        banner.setImages(Arrays.asList(mImageUrl));
        banner.start();
        tvActivityName.setText(mActivityName);
        tvEnrollNumber.setText(getResources().getString(R.string.enroll_number,mActivityEnrollNumber));
        tvActivityAddress.setText(mActivityAddress);
        tvActivityIntro.setText(mActivityIntro);
        tvActivityPrice.setText(mActivityPrice);
        tvBeginTime.setText(mActivityBeginTime);
        tvEndTime.setText(mActivityEndTime);
    }

    /**
    * 初始化banner
    */
    private void initBanner() {
        //设置banner样式
        banner.setBannerStyle(BannerConfig.NUM_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
    }

    /**
    * 立即报名
    */
    @OnClick(R.id.btn_activity_info_enroll_activity)
    void enrollActivity(){
        LogUtil.d(TAG,"enrollActivity");
        if(detailItem != null){
            Bundle bundle = new Bundle();
            bundle.putString(ACTIVITY_ID,detailItem.getActivityId());
            bundle.putString(USER_ID, WzyxPreference.getCustomAppProfile(WzyxPreference.KEY_USER_ID));
            bundle.putString(SELLER_ID,detailItem.getSellerId());
            bundle.putString(ACTIVITY_NAME,detailItem.getActivityName());
            bundle.putString(ACTIVITY_COST,detailItem.getCost());

            Intent intent = new Intent(ActivityInfoActivity.this, SubmitOrderActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @OnClick(R.id.tv_evaluate)
    void onEvaluateList(){
        Intent intent=new Intent(ActivityInfoActivity.this, ActivityEvaluateActivity.class);
        intent.putExtra(ACTIVITY_ID,activityId);
        startActivity(intent);
    }
    @Override
    protected void onStart() {
        super.onStart();
        //开始轮播
        banner.startAutoPlay();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //结束轮播
        banner.stopAutoPlay();
    }
}
