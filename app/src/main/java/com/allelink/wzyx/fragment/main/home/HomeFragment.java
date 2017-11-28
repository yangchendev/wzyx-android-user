package com.allelink.wzyx.fragment.main.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.allelink.wzyx.R;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * @author yangc
 * @version 1.0
 * @filename HomeFragment
 * @date 2017/11/22
 * @description 首页 fragment
 * @email 1048027353@qq.com
 */

public class HomeFragment extends SupportFragment{
    private static final String TAG = "HomeFragment";
    /**
    * UI
    */

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initView(view);
        initEvent();
        return view;
    }

    /**
     * 初始化view
     * @param view
     */
    private void initView(View view) {

    }

    /**
    * 初始化事件
    */
    private void initEvent() {

    }

}
