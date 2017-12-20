package com.allelink.wzyx.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.allelink.wzyx.R;
import com.allelink.wzyx.app.GlideApp;
import com.allelink.wzyx.model.OrderItem;
import com.allelink.wzyx.net.RestConstants;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 订单列表适配器
 * @author yangc
 * @version 1.0
 * @date 2017/12/20
 * @email yangchendev@qq.com
 */
public class OrderItemAdapter extends BaseQuickAdapter<OrderItem,BaseViewHolder>{
    /**
    * 未付款
    */
    private static final String ORDER_UNPAID = "0";
    /**
    * 已完成
    */
    private static final String ORDER_COMPLETED = "1";
    /**
    * 已取消
    */
    private static final String ORDER_CANCELED = "-1";
    public OrderItemAdapter(int layoutResId, @Nullable List<OrderItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderItem item) {
        String orderState = item.getOrderState();
        View orderStateView = helper.getView(R.id.view_item_fragment_order);
        //订单状态标签
        TextView tvOrderState = helper.getView(R.id.tv_item_fragment_order_order_state);
        //订单费用
        TextView tvOrderCost = helper.getView(R.id.tv_item_fragment_order_total_cost);
        //右边的按钮
        Button btnRight = helper.getView(R.id.btn_item_fragment_order_right);
        switch (orderState){
            //未付款
            case ORDER_UNPAID:
                orderStateView.setBackgroundColor(mContext.getResources().getColor(R.color.pay));
                tvOrderState.setText(mContext.getResources().getString(R.string.go_to_pay));
                tvOrderState.setTextColor(mContext.getResources().getColor(R.color.pay));
                tvOrderCost.setTextColor(mContext.getResources().getColor(R.color.pay));
                btnRight.setText(mContext.getResources().getString(R.string.go_to_pay));
                btnRight.setTextColor(mContext.getResources().getColor(R.color.pay));
                btnRight.setBackground(mContext.getResources().getDrawable(R.drawable.btn_pay_border));
                break;
            //已完成
            case ORDER_COMPLETED:
                orderStateView.setBackgroundColor(mContext.getResources().getColor(R.color.brands_color));
                tvOrderState.setText(mContext.getResources().getString(R.string.completed));
                tvOrderState.setTextColor(mContext.getResources().getColor(R.color.brands_color));
                tvOrderCost.setTextColor(mContext.getResources().getColor(R.color.brands_color));
                btnRight.setText(mContext.getResources().getString(R.string.refund));
                btnRight.setTextColor(mContext.getResources().getColor(R.color.brands_color));
                btnRight.setBackground(mContext.getResources().getDrawable(R.drawable.btn_refund_border));
                break;
            case ORDER_CANCELED:
                orderStateView.setBackgroundColor(mContext.getResources().getColor(R.color.canceled));
                tvOrderState.setText(mContext.getResources().getString(R.string.canceled));
                tvOrderState.setTextColor(mContext.getResources().getColor(R.color.canceled));
                tvOrderCost.setTextColor(mContext.getResources().getColor(R.color.canceled));
                btnRight.setVisibility(View.GONE);
                break;
            default:
                break;
        }
        //设置订单费用
        tvOrderCost.setText(mContext.getResources().getString(R.string.activity_cost,item.getCost()));
        //设置订单号
        helper.setText(R.id.tv_item_fragment_order_order_id, mContext.getResources().getString(R.string.order_id));
        //设置订单图片
        ImageView imageView = helper.getView(R.id.iv_fragment_order_item_pic);
        GlideApp.with(mContext)
                .load(RestConstants.IMAGE_ROOT_URL+item.getImageUrl())
                .placeholder(R.drawable.activity_default_pic)
                .error(R.drawable.activity_default_pic)
                .into(imageView);
        //设置订单名称
        helper.setText(R.id.tv_item_fragment_order_order_name, item.getActivityName());
        //设置订单的创建时间
        helper.setText(R.id.tv_item_fragment_order_create_time,item.getCreateTime());
        //绑定需要实现点击的按钮
        helper.addOnClickListener(R.id.btn_item_fragment_order_right);
    }
}
