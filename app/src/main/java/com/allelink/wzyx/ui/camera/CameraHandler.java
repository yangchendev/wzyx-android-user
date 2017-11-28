package com.allelink.wzyx.ui.camera;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.allelink.wzyx.R;
import com.allelink.wzyx.utils.file.FileUtil;
import com.blankj.utilcode.util.FileUtils;

import java.io.File;

/**
 * @author yangc
 * @version 1.0
 * @filename CameraHandler
 * @date 2017/11/27
 * @description 照片处理类
 * @email 1048027353@qq.com
 */

public class CameraHandler implements View.OnClickListener {
    /**
    * 对话框
    */
    private final AlertDialog DIALOG;
    /**
    * 上下文
    */
    private final Activity ACTIVITY;
    public CameraHandler(Activity activity){
        this.ACTIVITY = activity;
        DIALOG = new AlertDialog.Builder(ACTIVITY).create();
    }
    /**
    * 开启选择拍照还是从相册取照片的对话框
    */
    final void beginCameraDialog(){
        DIALOG.show();
        //获取window，方便设置属性
        final Window window = DIALOG.getWindow();
        if(window != null){
            //设置dialog的自定义布局
            window.setContentView(R.layout.dialog_camera_panel);
            //设置dialog在底部
            window.setGravity(Gravity.BOTTOM);
            //设置动画
            window.setWindowAnimations(R.style.anim_panel_up_from_bottom);
            //设置透明背景
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //获取属性
            final WindowManager.LayoutParams params = window.getAttributes();
            //宽度
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            //弹出对话框背景为灰色
            params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            params.dimAmount = 0.5f;
            window.setAttributes(params);
            //设置按钮点击事件
            window.findViewById(R.id.btn_dialog_cancel).setOnClickListener(this);
            window.findViewById(R.id.btn_dialog_take_photo).setOnClickListener(this);
            window.findViewById(R.id.btn_dialog_pick_photo).setOnClickListener(this);
        }

    }
    /**
    * 根据时间获取图片名
    */
    private String getPhotoName(){
        return FileUtil.getFileNameByTime("IMG", "jpg");
    }
    /**
    * 拍照
    */
    private void takePhoto() {
        final String currentPhotoName = getPhotoName();
        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        final File tempFile = new File(FileUtil.CAMERA_PHOTO_DIR, currentPhotoName);

        //兼容7.0及以上的写法
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            final ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, tempFile.getPath());
            final Uri uri = ACTIVITY.getApplicationContext().getContentResolver().
                    insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            //需要讲Uri路径转化为实际路径
            final File realFile =
                    FileUtils.getFileByPath(FileUtil.getRealFilePath(ACTIVITY.getApplicationContext(), uri));
            final Uri realUri = Uri.fromFile(realFile);
            CameraImageBean.getInstance().setPath(realUri);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        } else {
            final Uri fileUri = Uri.fromFile(tempFile);
            CameraImageBean.getInstance().setPath(fileUri);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        }
        ACTIVITY.startActivityForResult(intent, RequestCodes.TAKE_PHOTO);
    }
    /**
    * 从相册选取图片
    */
    private void pickPhoto() {
        final Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        ACTIVITY.startActivityForResult
                (Intent.createChooser(intent, "选择获取图片的方式"), RequestCodes.PICK_PHOTO);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        //取消
        if(id == R.id.btn_dialog_cancel){
            DIALOG.cancel();
        }else if(id == R.id.btn_dialog_take_photo){
            //拍照
            takePhoto();
            DIALOG.cancel();
        }else if(id == R.id.btn_dialog_pick_photo){
            //选取本地相册
            pickPhoto();
            DIALOG.cancel();
        }
    }
}
