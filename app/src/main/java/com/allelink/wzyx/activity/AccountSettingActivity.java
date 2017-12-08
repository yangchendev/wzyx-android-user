package com.allelink.wzyx.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.allelink.wzyx.R;
import com.allelink.wzyx.activity.base.BaseActivity;
import com.allelink.wzyx.app.user.GetUserInfoHandler;
import com.allelink.wzyx.app.user.IGetUserInfoListener;
import com.allelink.wzyx.app.user.IUploadAvatarListener;
import com.allelink.wzyx.app.user.UploadAvatarHandler;
import com.allelink.wzyx.model.User;
import com.allelink.wzyx.ui.TitleBar;
import com.allelink.wzyx.ui.camera.CameraImageBean;
import com.allelink.wzyx.ui.camera.RequestCodes;
import com.allelink.wzyx.ui.camera.WzyxCamera;
import com.allelink.wzyx.ui.loader.WzyxLoader;
import com.allelink.wzyx.utils.activity.ActivityUtils;
import com.allelink.wzyx.utils.callback.CallbackManager;
import com.allelink.wzyx.utils.callback.CallbackType;
import com.allelink.wzyx.utils.callback.IGlobalCallback;
import com.allelink.wzyx.utils.log.LogUtil;
import com.allelink.wzyx.utils.storage.WzyxPreference;
import com.allelink.wzyx.utils.toast.ToastUtil;
import com.bigkoo.pickerview.TimePickerView;
import com.bumptech.glide.Glide;
import com.wang.avi.AVLoadingIndicatorView;
import com.yalantis.ucrop.UCrop;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;
import qiu.niorgai.StatusBarCompat;


/**
 * 账号设置
 * @author yangc
 * @date 2017/11/24
 * @version 1.0
 * @email 1048027353@qq.com
 */
@RuntimePermissions
public class AccountSettingActivity extends BaseActivity {
    private static final String TAG = "AccountSettingActivity";
    private static final int REQUEST_CODE_NICK_NAME = 1001;
    private static final int REQUEST_CODE_GENDER = 1002;
    /**
    * UI
    */
    @BindView(R.id.tb_account_setting)
    TitleBar titleBar = null;
    @BindView(R.id.tv_account_setting_nickname)
    AppCompatTextView tvNickname = null;
    @BindView(R.id.tv_account_setting_gender)
    AppCompatTextView tvGender = null;
    @BindView(R.id.tv_account_setting_birthday)
    AppCompatTextView tvBirthday = null;
    @BindView(R.id.tv_account_setting_phone_number)
    AppCompatTextView tvPhoneNumber = null;
    TimePickerView timePickerView = null;
    @BindView(R.id.rl_account_setting_loader_container)
    RelativeLayout flLoaderContainer = null;
    @BindView(R.id.avi_account_setting_loader)
    AVLoadingIndicatorView avi = null;
    @BindView(R.id.iv_account_setting_avatar)
    ImageView ivAvatar = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);
        StatusBarCompat.setStatusBarColor(this,getResources().getColor(R.color.brands_color));
        //隐藏actionBar
        android.app.ActionBar actionBar = getActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        //绑定ButterKnife
        ButterKnife.bind(this);
        initUserInfo();
        initEvent();
        initTimePicker();
    }
    /**
    * 配置图片裁剪工具
    */
    private UCrop.Options getUCropOptions() {
        UCrop.Options options = new UCrop.Options();
        //设置工具栏颜色
        options.setToolbarColor(getResources().getColor(R.color.brands_color));
        //设置工具类标题
        options.setToolbarTitle(getResources().getString(R.string.crop_avatar));
        options.setActiveWidgetColor(getResources().getColor(R.color.brands_color));
        options.setStatusBarColor(getResources().getColor(R.color.brands_color));
        options.setLogoColor(getResources().getColor(R.color.brands_color));
        return options;
    }

    /**
    * 初始化用户信息
    */
    private void initUserInfo() {
        //加载load动画
        WzyxLoader.showLoading(this);
        params.clear();
        params.put("phoneNumber",WzyxPreference.getCustomAppProfile(WzyxPreference.KEY_PHONE_NUMBER));
        GetUserInfoHandler.getUserInfo(params, new IGetUserInfoListener() {
            @Override
            public void onGetUserInfoSuccess(User user) {
                //停止load动画
                WzyxLoader.stopLoading();
                ToastUtil.toastShort(AccountSettingActivity.this,user.toString());
                LogUtil.d(TAG,user.toString());
                if(!user.getNickname().isEmpty()){
                    tvNickname.setText(user.getNickname());
                    LogUtil.d(TAG,user.getNickname());
                }
                if(!user.getGender().isEmpty()){
                    tvGender.setText(user.getGender());
                    LogUtil.d(TAG,user.getGender());
                }
                if(!user.getAvatar().isEmpty()){
                    //设置头像
                    Glide.with(AccountSettingActivity.this)
                            .load(user.getAvatar())
                            .into(ivAvatar);
                    LogUtil.d(TAG,user.getAvatar());
                }
                if(!user.getPhoneNumber().isEmpty()){
                    String phoneNumber = user.getPhoneNumber().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
                    tvPhoneNumber.setText(phoneNumber);
                    LogUtil.d(TAG,user.getPhoneNumber());
                }
                //更新本地用户信息
                storeUserInfo(user);
            }

            @Override
            public void onGetUserInfoFailure(String error) {
                //停止load动画
                WzyxLoader.stopLoading();
                LogUtil.d(TAG,error);
                ToastUtil.toastShort(AccountSettingActivity.this,getResources().getString(R.string.get_user_info_failure));
                setUserInfoFromLocal();
            }
        });
    }
    /**
    * 如果获取服务器信息失败则加载本地的用户信息
    */
    private void setUserInfoFromLocal() {
        String phoneNumber = WzyxPreference.getCustomAppProfile(WzyxPreference.KEY_PHONE_NUMBER);
        String nickname = WzyxPreference.getCustomAppProfile(WzyxPreference.KEY_NICKNAME);
        String gender = WzyxPreference.getCustomAppProfile(WzyxPreference.KEY_GENDER);
        String avatar = WzyxPreference.getCustomAppProfile(WzyxPreference.KEY_AVATAR_URL);
        if(!phoneNumber.isEmpty()){
            String newPhoneNumber = WzyxPreference.getCustomAppProfile(WzyxPreference.KEY_PHONE_NUMBER).replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
            tvPhoneNumber.setText(newPhoneNumber);
        }
        if(!nickname.isEmpty()){
            tvNickname.setText(nickname);
        }
        if(!gender.isEmpty()){
            tvGender.setText(gender);
        }
        if(!avatar.isEmpty()){
            Glide.with(AccountSettingActivity.this)
                    .load(avatar)
                    .into(ivAvatar);
        }

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
    }

    /**
    * 初始化点击事件
    */
    private void initEvent() {
        titleBar.setOnTitleBarButtonClickListener(new TitleBar.onTitleBarButtonClickListener() {
            @Override
            public void onLeftClick() {
                LogUtil.d(TAG,"back");
                //退出
                AccountSettingActivity.this.finish();
            }

            @Override
            public void onRightClick() {

            }
        });
    }
    /**
    * 修改头像
    */
    @OnClick(R.id.rl_account_setting_avatar)
    void editAvatar(){
        LogUtil.d(TAG,"修改头像");
        CallbackManager.getInstance()
                .addCallback(CallbackType.ON_CROP, new IGlobalCallback<Uri>() {
                    @Override
                    public void executeCallback(@Nullable final Uri args) {
                        LogUtil.d(TAG,args.toString());

                        WzyxLoader.showLoading(AccountSettingActivity.this);
                        //还需要上传头像到服务器上
                        UploadAvatarHandler.uploadAvatar(WzyxPreference.getCustomAppProfile(WzyxPreference.KEY_PHONE_NUMBER
                        ), args.getPath(), new IUploadAvatarListener() {
                            //头像上传成功
                            @Override
                            public void onUploadAvatarSuccess(String avatarUrl) {
                                WzyxLoader.stopLoading();
                                ToastUtil.toastShort(AccountSettingActivity.this,getResources().getString(R.string.upload_avatar_success));
                                //保存头像地址到本地
                                Glide.with(AccountSettingActivity.this)
                                        .load(args)
                                        .into(ivAvatar);
                                WzyxPreference.addCustomAppProfile(WzyxPreference.KEY_AVATAR_URL,avatarUrl);
                            }
                            //头像上传失败
                            @Override
                            public void onUploadAvatarFailure(String error) {
                                WzyxLoader.stopLoading();
                                ToastUtil.toastShort(AccountSettingActivity.this,getResources().getString(R.string.upload_avatar_failure));
                                //上传失败加载之前的头像
                                String avatar = WzyxPreference.getCustomAppProfile(WzyxPreference.KEY_AVATAR_URL);
                                LogUtil.d(TAG,avatar);
                                if(avatar.isEmpty()){
                                    ivAvatar.setImageResource(R.drawable.avatar_default);
                                }else{
                                    Glide.with(AccountSettingActivity.this)
                                            .load(avatar)
                                            .into(ivAvatar);
                                }
                            }
                        });
                    }
                });
        startCameraWithCheck();
    }
    /**
     * 修改昵称
     */
    @OnClick(R.id.rl_account_setting_nickname)
    void editNickname(){
        LogUtil.d(TAG,"修改用户名");
        Intent intent = new Intent(AccountSettingActivity.this, EditNicknameActivity.class);
        startActivityForResult(intent,REQUEST_CODE_NICK_NAME);
    }
    /**
     * 修改生日
     */
    @OnClick(R.id.rl_account_setting_birthday)
    void editBirthday(){
        LogUtil.d(TAG,"修改生日");
        timePickerView.show();
    }
    /**
     * 修改性别
     */
    @OnClick(R.id.rl_account_setting_gender)
    void editGender(){
        LogUtil.d(TAG,"修改性别");
        Intent intent = new Intent(AccountSettingActivity.this, EditGenderActivity.class);
        startActivityForResult(intent,REQUEST_CODE_GENDER);
    }
    /**
     * 修改密码
     */
    @OnClick(R.id.rl_account_setting_change_password)
    void changePassword(){
        LogUtil.d(TAG,"修改密码");
        ActivityUtils.startActivity(AccountSettingActivity.this,EditPasswordActivity.class);
    }
    /**
     * 绑定手机号
     */
    @OnClick(R.id.rl_account_setting_phone_number)
    void bindPhone(){
        LogUtil.d(TAG,"绑定手机号");
    }
    /**
     * 绑定微信
     */
    @OnClick(R.id.rl_account_setting_we_chat)
    void bindWechat(){
        LogUtil.d(TAG,"绑定微信");
    }
    /**
     * 绑定qq
     */
    @OnClick(R.id.rl_account_setting_qq)
    void bindQQ(){
        LogUtil.d(TAG,"绑定qq");
    }
    /**
     * 绑定微博
     */
    @OnClick(R.id.rl_account_setting_wei_bo)
    void bindWeibo(){
        LogUtil.d(TAG,"绑定微博");
    }
    /**
     * 绑定支付宝
     */
    @OnClick(R.id.rl_account_setting_a_li_pay)
    void bindAlipay(){
        LogUtil.d(TAG,"绑定支付宝");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case REQUEST_CODE_NICK_NAME:
                    tvNickname.setText(data.getStringExtra("nickname"));
                    WzyxPreference.addCustomAppProfile(WzyxPreference.KEY_NICKNAME,data.getStringExtra("nickname"));
                    break;
                case REQUEST_CODE_GENDER:
                    tvGender.setText(data.getStringExtra("gender"));
                    WzyxPreference.addCustomAppProfile(WzyxPreference.KEY_GENDER,data.getStringExtra("gender"));
                    break;
                case RequestCodes.TAKE_PHOTO:
                    final Uri resultUri = CameraImageBean.getInstance().getPath();
                    UCrop.of(resultUri, resultUri)
                            .withMaxResultSize(400, 400)
                            .withOptions(getUCropOptions())
                            .start(this);
                    break;
                case RequestCodes.PICK_PHOTO:
                    if (data != null) {
                        final Uri pickPath = data.getData();
                        //从相册选择后需要有个路径存放剪裁过的图片
                        final String pickCropPath =WzyxCamera.createCropFile().getPath();
                        UCrop.of(pickPath, Uri.parse(pickCropPath))
                                .withMaxResultSize(400, 400)
                                .withOptions(getUCropOptions())
                                .start(this);
                    }
                    break;
                case RequestCodes.CROP_PHOTO:
                    final Uri cropUri = UCrop.getOutput(data);
                    //拿到剪裁后的数据进行处理
                    @SuppressWarnings("unchecked")
                    final IGlobalCallback<Uri> callback = CallbackManager
                            .getInstance()
                            .getCallback(CallbackType.ON_CROP);
                    if (callback != null) {
                        callback.executeCallback(cropUri);
                    }
                    break;
                case RequestCodes.CROP_ERROR:
                    ToastUtil.toastShort(this,getResources().getString(R.string.crop_error));
                    break;
                default:
                    break;
            }
        }

    }
    /**
    * 初始化日期选择器
    */
    private void initTimePicker() {
        //控制时间范围(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
        //因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(1900, 0, 23);
        Calendar endDate = Calendar.getInstance();
        //时间选择器
        timePickerView = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
                ToastUtil.toastShort(AccountSettingActivity.this,date.toString());
            }
        })
                //年月日时分秒 的显示与否，不设置则默认全部显示
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("", "", "", "", "", "")
                .isCenterLabel(false)
                .setDividerColor(Color.DKGRAY)
                .setContentSize(21)
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setSubmitColor(getResources().getColor(R.color.brands_color))
                .setCancelColor(getResources().getColor(R.color.brands_color))
                .setDecorView(null)
                .isCyclic(true)
                .build();
    }
    /**
    * 开始loader动画
    */
    private void startLoadAnim(){
        flLoaderContainer.setVisibility(View.VISIBLE);
        avi.show();
    }
    /**
     * 结束loader动画
     */
    private void stopLoadAnim(){
        flLoaderContainer.setVisibility(View.GONE);
        avi.hide();
    }

    /**
    * 需要相机权限的方法，不直接调用
    */
    @NeedsPermission({Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void startCamera(){
        WzyxCamera.start(this);
    }
    /**
    * 真正的调用方法
    */
    private void startCameraWithCheck(){
        AccountSettingActivityPermissionsDispatcher.startCameraWithPermissionCheck(this);
    }
    /**
    * 用户拒绝相机权限时调用
    */
    @OnPermissionDenied({Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void onCameraDenied() {
        ToastUtil.toastShort(this,getResources().getString(R.string.camera_permission_denied));
    }

    /**
     * 用户永久拒绝相机权限时调用
     */
    @OnNeverAskAgain({Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void onCameraNever() {
        ToastUtil.toastShort(this,getResources().getString(R.string.camera_permission_never_ask));
    }


    /**
    * 权限允许或拒绝时的回调
    */
    @SuppressLint("NeedOnRequestPermissionsResult")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AccountSettingActivityPermissionsDispatcher.onRequestPermissionsResult(this,requestCode,grantResults);
    }

}
