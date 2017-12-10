package com.allelink.wzyx.ui.banner;

import android.content.Context;
import android.widget.ImageView;

import com.allelink.wzyx.R;
import com.allelink.wzyx.app.GlideApp;
import com.allelink.wzyx.net.RestConstants;
import com.youth.banner.loader.ImageLoader;

/**
 * banner图片加载器
 * @author yangc
 * @version 1.0
 * @date 2017/12/9
 * @email yangchendev@qq.com
 */
public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        //加载图片
        GlideApp.with(context)
                .load(RestConstants.IMAGE_ROOT_URL+path)
                .placeholder(R.drawable.activity_detail_default_pic)
                .error(R.drawable.activity_detail_default_pic)
                .into(imageView);
    }
}
