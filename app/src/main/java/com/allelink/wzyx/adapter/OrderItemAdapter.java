package com.allelink.wzyx.adapter;

import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.allelink.wzyx.R;
import com.allelink.wzyx.app.GlideApp;
import com.allelink.wzyx.model.OrderItem;
import com.allelink.wzyx.net.RestConstants;
import com.allelink.wzyx.utils.log.LogUtil;
import com.allelink.wzyx.utils.storage.WzyxPreference;
import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 订单列表适配器
 * @author yangc
 * @version 1.0
 * @date 2017/12/20
 * @email yangchendev@qq.com
 */
public class OrderItemAdapter extends BaseItemDraggableAdapter<OrderItem,BaseViewHolder> {
    /**
    * 未付款
    */
    private static final String ORDER_UNPAID = "0";
    /**
    * 已完成
    */
    private static final String ORDER_PAID = "1";
    /**
    * 已取消
    */
    private static final String ORDER_CANCELED = "-1";
    /**
    * 订单退款审核中
    */
    private static final String ORDER_REFUND_APPLYING = "2";
    /**
    * 订单已退款
    */
    private static final String ORDER_REFUND_SUCCESS = "3";
    /**
    * 订单已完成
    */
    private static final String ORDER_COMPETED = "5";
    /**
     * 订单已完成
     */
    private static final String ORDER_COMMENTED = "6";
    /**
    * 30分钟倒计时
    */
    private static final long COUNTER_DOWN_TIME = 30*60*1000;
    /**
    * 当前时间
    */
    private long currentTime;
    /**
    * 订单创建时间
    */
    private long createTime;
    /**
    * 剩余时间
    */
    private long leftTime;
    /**
    * 倒计时结束接口
    */
    private OnCountTimeFinishListener listener;
    private String isPaySuccess = "";
    /**
    * 用于统一管理CountDownTimer集合，避免内存泄漏
    */
    private SparseArray<CountDownTimer> countDownMap;
    public CountDownTimer countDownTimer;
    public OrderItemAdapter(int layoutResId, @Nullable List<OrderItem> data) {
        super(layoutResId, data);
        countDownMap = new SparseArray<>();
    }

    @Override
    protected void convert(BaseViewHolder helper, final OrderItem item) {

        String orderState = item.getOrderState();
        //初始化时间
        initTime(item);
        View orderStateView = helper.getView(R.id.view_item_fragment_order);
        //订单状态标签
        TextView tvOrderState = helper.getView(R.id.tv_item_fragment_order_order_state);
        //订单费用
        TextView tvOrderCost = helper.getView(R.id.tv_item_fragment_order_total_cost);
        //右边的按钮
        Button btnRight = helper.getView(R.id.btn_item_fragment_order_right);
        //倒计时
        final TextView tvCounterDown = helper.getView(R.id.tv_item_fragment_order_counter_down);
        //提示
        final TextView leftTimeTip = helper.getView(R.id.tv_item_fragment_order_left_time_tip);
        switch (orderState){
            //未付款 需要显示倒计时
            case ORDER_UNPAID:

                leftTimeTip.setVisibility(View.VISIBLE);
                tvCounterDown.setVisibility(View.VISIBLE);
                orderStateView.setBackgroundColor(mContext.getResources().getColor(R.color.pay));
                tvOrderState.setText(mContext.getResources().getString(R.string.wait_for_pay));
                tvOrderState.setTextColor(mContext.getResources().getColor(R.color.pay));
                tvOrderCost.setTextColor(mContext.getResources().getColor(R.color.pay));
                btnRight.setVisibility(View.VISIBLE);
                btnRight.setText(mContext.getResources().getString(R.string.go_to_pay));
                btnRight.setTextColor(mContext.getResources().getColor(R.color.pay));
                btnRight.setBackground(mContext.getResources().getDrawable(R.drawable.btn_pay_border));
                if(leftTime == 0){
                    tvCounterDown.setText(mContext.getResources().getString(R.string.left_time,
                            "00:00"));
                    //回调
                    listener.onCountTimeFinish(item);
                }else{
                    if(countDownTimer != null){
                        countDownTimer.cancel();
                    }
                    countDownTimer = new CountDownTimer(leftTime,1000){
                        @Override
                        public void onTick(long millisUntilFinished) {
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
                            String ms = simpleDateFormat.format(millisUntilFinished);
                            LogUtil.d(TAG,ms);
                            tvCounterDown.setText(mContext.getResources().getString(R.string.left_time,
                                    ms));
                            LogUtil.d(TAG,item.getOrderIdStr()+" "
                                    +"\n"+"分："+millisUntilFinished/1000/60
                                    +"\n"+"秒："+millisUntilFinished / 1000 % 60
                            );
                            isPaySuccess = WzyxPreference.getCustomAppProfile(item.getOrderIdStr());
                            if(!isPaySuccess.isEmpty()){
                                WzyxPreference.addCustomAppProfile(item.getOrderIdStr(),"");
                                this.cancel();
                            }
                        }
                        @Override
                        public void onFinish() {
                            //回调
                            listener.onCountTimeFinish(item);
                            this.cancel();
                        }
                    }.start();
                    countDownMap.put(Integer.parseInt(item.getOrderId()),countDownTimer);
                }

                break;
            //已支付
            case ORDER_PAID:
                leftTimeTip.setVisibility(View.GONE);
                tvCounterDown.setVisibility(View.GONE);
                orderStateView.setBackgroundColor(mContext.getResources().getColor(R.color.brands_color));
                tvOrderState.setText(mContext.getResources().getString(R.string.paid));
                tvOrderState.setTextColor(mContext.getResources().getColor(R.color.brands_color));
                tvOrderCost.setTextColor(mContext.getResources().getColor(R.color.brands_color));
                btnRight.setVisibility(View.VISIBLE);
                btnRight.setText(mContext.getResources().getString(R.string.comment));
                btnRight.setTextColor(mContext.getResources().getColor(R.color.brands_color));
                btnRight.setBackground(mContext.getResources().getDrawable(R.drawable.btn_refund_border));
                break;
            //已评价
            case ORDER_COMMENTED:
                leftTimeTip.setVisibility(View.GONE);
                tvCounterDown.setVisibility(View.GONE);
                orderStateView.setBackgroundColor(mContext.getResources().getColor(R.color.brands_color));
                tvOrderState.setText(mContext.getResources().getString(R.string.commented));
                tvOrderState.setTextColor(mContext.getResources().getColor(R.color.brands_color));
                tvOrderCost.setTextColor(mContext.getResources().getColor(R.color.brands_color));
                btnRight.setVisibility(View.VISIBLE);
                btnRight.setText(mContext.getResources().getString(R.string.commented));
                btnRight.setTextColor(mContext.getResources().getColor(R.color.brands_color));
                btnRight.setBackground(mContext.getResources().getDrawable(R.drawable.btn_refund_border));
                break;
            //已取消
            case ORDER_CANCELED:

                leftTimeTip.setVisibility(View.GONE);
                tvCounterDown.setVisibility(View.GONE);
                orderStateView.setBackgroundColor(mContext.getResources().getColor(R.color.canceled));
                tvOrderState.setText(mContext.getResources().getString(R.string.canceled));
                tvOrderState.setTextColor(mContext.getResources().getColor(R.color.canceled));
                tvOrderCost.setTextColor(mContext.getResources().getColor(R.color.canceled));
                btnRight.setVisibility(View.GONE);
                break;
            //退款中
            case ORDER_REFUND_APPLYING:

                leftTimeTip.setVisibility(View.GONE);
                tvCounterDown.setVisibility(View.GONE);
                orderStateView.setBackgroundColor(mContext.getResources().getColor(R.color.brands_color));
                tvOrderState.setText(mContext.getResources().getString(R.string.refunding));
                tvOrderState.setTextColor(mContext.getResources().getColor(R.color.brands_color));
                tvOrderCost.setTextColor(mContext.getResources().getColor(R.color.brands_color));
                btnRight.setVisibility(View.GONE);
                break;
            //退款成功
            case ORDER_REFUND_SUCCESS:

                leftTimeTip.setVisibility(View.GONE);
                tvCounterDown.setVisibility(View.GONE);
                orderStateView.setBackgroundColor(mContext.getResources().getColor(R.color.brands_color));
                tvOrderState.setText(mContext.getResources().getString(R.string.refund_success));
                tvOrderState.setTextColor(mContext.getResources().getColor(R.color.brands_color));
                tvOrderCost.setTextColor(mContext.getResources().getColor(R.color.brands_color));
                btnRight.setVisibility(View.GONE);
                break;
            //已完成
            case ORDER_COMPETED:

                leftTimeTip.setVisibility(View.GONE);
                tvCounterDown.setVisibility(View.GONE);
                orderStateView.setBackgroundColor(mContext.getResources().getColor(R.color.brands_color));
                tvOrderState.setText(mContext.getResources().getString(R.string.completed));
                tvOrderState.setTextColor(mContext.getResources().getColor(R.color.brands_color));
                tvOrderCost.setTextColor(mContext.getResources().getColor(R.color.brands_color));
                btnRight.setVisibility(View.GONE);
                break;
            default:
                break;
        }
        //设置订单费用
        tvOrderCost.setText(mContext.getResources().getString(R.string.activity_cost,item.getCost()));
        //设置订单号
        helper.setText(R.id.tv_item_fragment_order_order_id, mContext.getResources().getString(R.string.order_id,item.getOrderIdStr()));
        //设置订单图片
        ImageView imageView = helper.getView(R.id.iv_fragment_order_item_pic);
        GlideApp.with(mContext)
                .load(RestConstants.IMAGE_ROOT_URL+item.getImageUrl().replace("\\","/"))
                .placeholder(R.drawable.activity_default_pic)
                .error(R.drawable.activity_default_pic)
                .into(imageView);
        //设置订单名称
        helper.setText(R.id.tv_item_fragment_order_order_name, item.getActivityName());
        //设置订单的创建时间
        helper.setText(R.id.tv_item_fragment_order_create_time,item.getCreateTime());
        //绑定需要实现点击的按钮
        helper.addOnClickListener(R.id.btn_item_fragment_order_right);
        helper.addOnClickListener(R.id.ll_right_delete);
        helper.addOnClickListener(R.id.ll_right_cancel);
    }

    /**
     * 初始化时间
     * @param item 订单项
     */
    private void initTime(OrderItem item) {
        currentTime = System.currentTimeMillis();
        LogUtil.d(TAG,currentTime+"");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            createTime = simpleDateFormat.parse(item.getCreateTime()).getTime();
            LogUtil.d(TAG,createTime+"");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(currentTime - createTime > COUNTER_DOWN_TIME){
            leftTime = 0;
        }else{
            leftTime = COUNTER_DOWN_TIME - (currentTime - createTime);
        }
    }

    /**
     * 设置倒计时结束监听器
     * @param listener OnCountTimeFinishListener
     */
    public void setOnCountTimeFinishListener(OnCountTimeFinishListener listener){
        this.listener = listener;
    }
    /**
    * 倒计时结束接口
    */
    public interface OnCountTimeFinishListener{
        /**
        * 倒计时结束
         * @param orderItem 订单项
        */
        void onCountTimeFinish(OrderItem orderItem);
    }

    /**
     * 清空所有计时器
     */
    public void cancelAllTimers() {
        if (countDownMap == null) {
            return;
        }
        Log.e(TAG,  "size :  " + countDownMap.size());
        for (int i = 0,length = countDownMap.size(); i < length; i++) {
            CountDownTimer cdt = countDownMap.get(countDownMap.keyAt(i));
            if (cdt != null) {
                cdt.cancel();
            }
        }
    }

}
