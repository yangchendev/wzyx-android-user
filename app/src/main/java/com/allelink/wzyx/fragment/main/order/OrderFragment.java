package com.allelink.wzyx.fragment.main.order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.allelink.wzyx.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * @author yangc
 * @version 1.0
 * @filename OrderFragment
 * @date 2017/11/22
 * @description 订单 fragment
 * @email 1048027353@qq.com
 */

public class OrderFragment extends SupportFragment{
    private static final String TAG = OrderFragment.class.getSimpleName();
    private Unbinder mUnbinder = null;
    /**
     * tab标签数据
     */
    private String[] mTabTitles = new String[]{
            "全部",
            "已完成",
            "待付款",
            "已取消"
    };
    /**
    * UI
    */
    public static OrderFragment newInstance() {
        Bundle args = new Bundle();
        OrderFragment fragment = new OrderFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }
}
