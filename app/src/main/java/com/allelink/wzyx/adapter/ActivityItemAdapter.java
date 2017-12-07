package com.allelink.wzyx.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.allelink.wzyx.R;
import com.allelink.wzyx.app.GlideApp;
import com.allelink.wzyx.model.ActivityItem;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 活动列表适配器
 * @author yangc
 * @date 2017/12/7
 * @version 1.0
 * @email 1048027353@qq.com
 */

public class ActivityItemAdapter extends BaseQuickAdapter<ActivityItem,BaseViewHolder> {
    public ActivityItemAdapter(int layoutResId, @Nullable List data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, ActivityItem item) {
        helper.setText(R.id.tv_fragment_near_item_activity_name, item.getActivityName());
        helper.setText(R.id.tv_fragment_near_item_distance, item.getDistance());
        helper.setText(R.id.tv_fragment_near_item_price, item.getCost());
        helper.setText(R.id.tv_fragment_near_enroll_number, item.getEnrollNumber());
        ImageView imageView = helper.getView(R.id.iv_fragment_near_item_pic);
        GlideApp.with(mContext)
                .load(item.getImageUrl())
                .placeholder(R.drawable.activity_default_pic)
                .error(R.drawable.activity_default_pic)
                .into(imageView);
    }
}
